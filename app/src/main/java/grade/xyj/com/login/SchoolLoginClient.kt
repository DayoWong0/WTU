@file:Suppress("BlockingMethodInNonBlockingContext")

package grade.xyj.com.login

import com.xyj.xnative.NativeUtils
import grade.xyj.com.util.Http
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import org.json.JSONObject

class SchoolLoginClient(
    private val userName: String,
    private val passWord: String
) {

    val cookieStore = mutableMapOf<String, MutableList<Cookie>>()

    private val cookieJar = object : CookieJar {
        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            cookieStore[url.host] = cookies.toMutableList()
        }

        override fun loadForRequest(url: HttpUrl): MutableList<Cookie> =
            cookieStore[url.host] ?: mutableListOf()
    }

    private val loginClient = OkHttpClient.Builder()
        .followRedirects(false)
        .followSslRedirects(false)
        .cookieJar(cookieJar)
        .build()

    suspend fun start(onCaptcha: suspend (ByteArray) -> String): LoginStatus {
        val initResp = loginClient.get(LOGIN_API)
        if(initResp.code == 302){
            return LoginStatus.SUCCESS
        }
        val html = initResp.body?.string() ?: return LoginStatus.NET_ERROR

        val initCookie = cookieStore["auth.wtu.edu.cn"]

        val loginParam = LoginParam.parse(html)

        val data = NativeUtils.encode(JSONObject().apply {
            put("p1", passWord)
            put("p2", loginParam.pwdDefaultEncryptSalt)
        }.toString())
        val url = "http://112.78.185.21:8080/wfh/aes?data=$data"

        val pwdEncrypt = Http.get(url)?.body?.string()?:return LoginStatus.WF_NET_ERROR

        val loginParams = mutableMapOf(
            "username" to userName,
            "password" to pwdEncrypt,
            "lt" to loginParam.lt,
            "rememberMe" to "on",
            "dllt" to "userNamePasswordLogin",
            "execution" to loginParam.execution,
            "_eventId" to "submit",
            "rmShown" to "1"
        )

        //判断是否需要验证码
        if (needCaptcha()) {
            //获取验证码图片 如果失败就返回登陆失败
            val array = getCaptcha() ?: return LoginStatus.NET_ERROR
            //获取验证码
            loginParams["captchaResponse"] = onCaptcha(array)
        }

        var loginResp = loginClient.post(loginParam.postUrl, loginParams)
        //用户名密码验证码错误
        if (loginResp.code == 200) {
            val html1 = loginResp.body!!.string()
            return if (html1.contains("无效的验证码")) {
                //println("验证码错误，重新登陆")
                start(onCaptcha)
            } else {
                LoginStatus.USER_OR_PWD_ERROR
            }
        }
        var modeCookie: String? = null
        while (loginResp.code == 302) {
            if (cookieStore["auth.wtu.edu.cn"]?.any { it.name == "route" } == false && initCookie != null) {
                cookieStore["auth.wtu.edu.cn"]?.addAll(initCookie)
            }
            val nextUrl = loginResp.headers["Location"]!!
            val setCookie = loginResp.headers("Set-Cookie").firstOrNull()
            if (setCookie != null && setCookie.startsWith("MOD_AUTH_CAS")) {
                modeCookie = setCookie
            }
            //println("跳转到 $nextUrl")
            loginResp = loginClient.get(nextUrl)
        }
        if (modeCookie == null) {
            //println("MODE COOKIE is null")
            return LoginStatus.NET_ERROR
        }
        cookieStore["ehall.wtu.edu.cn"]?.add(
            Cookie.parse(
                "http://ehall.wtu.edu.cn/new/index.html".toHttpUrlOrNull()!!,
                modeCookie
            )!!
        )

        return LoginStatus.SUCCESS
    }

    private fun needCaptcha(): Boolean {
        val url =
            "https://auth.wtu.edu.cn/authserver/needCaptcha.html?username=$userName&_=${System.currentTimeMillis()}"
        val resp = loginClient.get(url).body?.string()
        return resp != "false"
    }

    private fun getCaptcha(): ByteArray? = loginClient.get(CAPTCHA).body?.bytes()

    companion object {

        const val LOGIN_API =
            "https://auth.wtu.edu.cn/authserver/login?service=http%3A%2F%2Fehall.wtu.edu.cn%2Flogin%3Fservice%3Dhttp%3A%2F%2Fehall.wtu.edu.cn%2Fnew%2Findex.html"

        val CAPTCHA get() = "https://auth.wtu.edu.cn/authserver/captcha.html?ts=${System.currentTimeMillis() % 1000}"
        val CHECKLOGIN_URL get() = "http://ehall.wtu.edu.cn/jsonp/userDesktopInfo.json?amp_jsonp_callback=jQuery1&_=${System.currentTimeMillis()}"

    }
}
