package grade.xyj.com.login

import org.jsoup.Jsoup

class LoginParam private constructor(
    val lt: String,
    val execution: String,
    val postUrl: String,
    val pwdDefaultEncryptSalt:String
) {
    companion object {
        fun parse(html: String): LoginParam {
            println(html)
            val lt: String
            val execution: String
            val loginPostUrl: String
            val pwdDefaultEncryptSalt:String

            Jsoup.parse(html).let {
                lt = it.getElementsByAttributeValue("name", "lt").first().attr("value")
                execution = it.getElementsByAttributeValue("name", "execution").first().attr("value")
                loginPostUrl = "https://auth.wtu.edu.cn" + it.selectFirst("form").attr("action")
                pwdDefaultEncryptSalt = it.getElementById("pwdDefaultEncryptSalt").attr("value")
            }
            return LoginParam(lt, execution, loginPostUrl,pwdDefaultEncryptSalt)
        }
    }
}