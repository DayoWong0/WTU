package grade.xyj.com.repository

import com.google.gson.JsonParser
import grade.xyj.com.util.Http
import grade.xyj.com.util.URL

object UserInfoRepository {

    fun getUserInfo(): Pair<String, String>? {
        val json = Http.get(URL.CHECKLOGIN_URL)?.body?.string()?.substring(8)
            ?.run { substring(0, length - 1) }
        try {
            val obj = JsonParser.parseString(json).asJsonObject
            if (obj["hasLogin"].asBoolean) {
                val userDepartment = obj["userDepartment"].asString
                val userName = obj["userName"].asString
                return userName to userDepartment
            }
        } catch (e: Throwable) {

        }
        return null
    }
}