package grade.xyj.com.util

import android.os.Environment
import com.blankj.utilcode.util.LogUtils
import grade.xyj.com.bean.*
import grade.xyj.com.util.extend.log
import grade.xyj.com.util.extend.runNoResult
import grade.xyj.com.util.extend.toBean
import grade.xyj.com.util.extend.toBeanList
import grade.xyj.room.entity.CourseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.File
import java.util.*

object DataUtil {
    //获取成绩
    suspend fun getGrades(xq: String, xn: String): GradesBean? = withContext(Dispatchers.IO) {
        //构造请求参数
        val map = mutableMapOf<String, String>()
        map["xnm"] = xn
        map["xqm"] = xq
        map["_search"] = "false"
        map["nd"] = System.currentTimeMillis().toString()
        map["queryModel.showCount"] = "5000"
        map["queryModel.currentPage"] = "1"
        map["queryModel.sortName"] = ""
        map["queryModel.sortOrder"] = "asc"
        map["time"] = "1"

        try {
            //提交请求并解析为本地实体
            Http.post(URL.GRADES_API, map)?.body?.string()?.toBean<GradesBean>()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getNGrades(grades: GradesBean?): List<NGradeBean>? = withContext(Dispatchers.IO) {
        val mData = mutableListOf<NGradeBean>()
        if (grades == null)
            null
        else {
            val list = grades.items
            var i = list.size - 1
            var zBean: NGradeBean? = null
            while (i >= 0) {
                NGradeBean(list[i--]).runNoResult {
                    if (this.list != null)
                        zBean = apply { mData.add(this) }
                    else
                        zBean!!.list.add(this)
                }
            }

            mData.apply {
                sortBy { itt -> Util.isPass(itt.cj) }
            }
        }
    }

    //获取新闻
    suspend fun getNews(id: String): List<NewsBean.BulletinListBean>? = withContext(Dispatchers.IO) {
        try {
            Http.get(URL.NEWS_API(id))?.body?.string()?.toBean<NewsBean>()?.bulletinList
        } catch (e: Throwable) {
            null
        }
    }

    //获取课表
    suspend fun getCourse(xn: String, xq: String, cgId: Long): List<CourseEntity>? = withContext(Dispatchers.IO) {
        val map = mapOf("xnm" to xn, "xqm" to xq)
        try {
            val a = Http.post(URL.COURSE_API, map)?.body?.string() ?: return@withContext null

            JSONObject(a)
                    .getJSONArray("kbList")
                    .toString()
                    .toBeanList<NetCourseBean>()
                    .map { it.toCourse(cgId) }
        } catch (e: Throwable) {
            log("ERROR:" + e.message!!)
            null
        }
    }

}