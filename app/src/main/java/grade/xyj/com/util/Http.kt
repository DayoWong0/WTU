package grade.xyj.com.util

import android.webkit.CookieManager
import grade.xyj.com.util.extend.CookieJarKt
import grade.xyj.com.login.get
import grade.xyj.com.login.post
import okhttp3.Cookie
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.Response

object Http {

    val USER_AGENT = "User-Agent"
    val COOKIE = "Cookie"
    val REFERER = "Referer"
    val ORIGIN = "Origin"
    val CONTENT_TYPE = "Content-Type"
    val ACCEPT_LANGUAGE = "Accept-Language"
    val ACCEPT_ENCODING = "Accept-Encoding"
    val ACCEPT = "Accept"

    val cookieManager: CookieManager = CookieManager.getInstance()


    val cookieJar = CookieJarKt {
        onSaveFromResponse { url, cookies ->
            cookies.forEach {
                cookieManager.setCookie(url.toString(), it.toString())
            }
        }
        onloadForRequest { httpUrl ->
            val cookieStr = cookieManager.getCookie(httpUrl.toString())
            if (!cookieStr.isNullOrEmpty()) {
                cookieStr.split(";").map { cookie ->
                    Cookie.parse(httpUrl, cookie)!!
                }
            } else {
                emptyList()
            }
        }
    }
    private val client = OkHttpClient.Builder().cookieJar(cookieJar).build()

    /********************************************POST***********************************************/

    fun post(url: String, headers: Map<String, String>?, requestBody: RequestBody): Response? =
        kotlin.runCatching { post(url, headers, requestBody) }.getOrNull()

    fun post(
        url: String,
        forms: Map<String, String>? = null,
        headers: Map<String, String>? = null
    ): Response? =
        kotlin.runCatching { client.post(url, forms, headers) }.getOrNull()

    fun post(url: String, json: String, headers: Map<String, String>? = null): Response? =
        kotlin.runCatching { client.post(url, json, headers) }.getOrNull()


    /********************************************GET***********************************************/
    fun get(url: String, headers: Map<String, String>? = null): Response? =
        kotlin.runCatching { client.get(url, headers) }.getOrNull()

    /********************************************OTHER***********************************************/

    fun clear() {
        cookieManager.removeAllCookies {}
        cookieManager.removeSessionCookies { }
    }


    fun urlBuild(url: String, map: Map<String, String>): String {
        var url2 = "$url?"
        for ((k, v) in map) {
            url2 = "$url2$k=$v&"
        }
        return url2.subSequence(0, url2.length - 1).toString()
    }

}