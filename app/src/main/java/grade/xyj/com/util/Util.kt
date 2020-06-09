package grade.xyj.com.util

import android.os.Build
import com.chad.library.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import java.util.regex.Pattern

object Util {
    suspend fun logMobile() = withContext(Dispatchers.IO) {
        val map = mapOf("cname" to Account.name,
                "cno" to Account.cno,
                "firm" to Build.BRAND,
                "acode" to Build.VERSION.RELEASE,
                "model" to Build.MODEL,
                "time" to Date().time.toString(),
                "ver" to BuildConfig.VERSION_NAME)
        val url = Http.urlBuild("${URL.SERVER_ADDRESS}mobile", map)
        Http.get(url)?.body?.string()
    }


    fun isPass(grade: String?): Boolean {
        if (grade.isNullOrBlank()) return false
        return when (grade.toIntOrNull()) {
            null -> grade != "不及格" && grade != "缺考"
            in 60..200 -> true
            else -> false
        }
    }

    fun getRegex(content: String, mMatcher: String): List<String> =
            Pattern.compile(mMatcher).matcher(content).run {
                mutableListOf<String>().apply {
                    while (find()) add(group())
                }
            }


    fun createUUID() = UUID.randomUUID().toString().replace("-", "")
}