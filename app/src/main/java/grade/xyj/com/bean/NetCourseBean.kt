package grade.xyj.com.bean

import grade.xyj.com.classchedule.util.Utils
import grade.xyj.room.entity.CourseEntity
import org.jsoup.internal.StringUtil

class NetCourseBean {
    //位置
    var cdmc: String? = null
    //节
    var jcs: String? = null
    //课名
    var kcmc: String? = null
    //教师姓名
    var xm: String? = null
    //星期
    var xqj: String? = null
    //周
    var zcd: String? = null

    fun toCourse(cgId: Long) =
        CourseEntity(
            kcmc!!,
            cdmc!!,
            xm!!,
            xqj!!.toInt(),
            getStartNode(),
            getNodeCount(),
            zcd2AllWeekString(),
            Utils.getRandomColor(),
            cgId
        )


    private fun zcd2AllWeekString(): String {
        val list = mutableListOf<String>()
        zcd!!.split(",").forEach {

            val contStr = it.substring(0, it.length - 1).split("-")
            val start = contStr[0].toInt()
            when {
                contStr.size == 1 -> {
                    list.add(contStr[0])
                }
                contStr[1].toIntOrNull() != null -> {
                    val end = contStr[1].toInt()
                    for (i in start..end) {
                        list.add(i.toString())
                    }
                }
                contStr[1].endsWith("单") -> {
                    val c = contStr[1].subSequence(0, contStr[1].length - 3)
                    val end = c.toString().toInt()
                    for (i in start..end) {
                        if (i % 2 == 1) {
                            list.add(i.toString())
                        }
                    }
                }
                contStr[1].endsWith("双") -> {
                    val c = contStr[1].subSequence(0, contStr[1].length - 3)
                    val end = c.toString().toInt()
                    for (i in start..end) {
                        if (i % 2 == 0) {
                            list.add(i.toString())
                        }
                    }
                }
            }
        }
        return StringUtil.join(list.toTypedArray(), ",")
    }

    private fun getStartNode() = jcs!!.split("-")[0].toInt()

    private fun getNodeCount(): Int {
        val ss = jcs!!.split("-")
        val s = ss[0].toInt()
        val e = ss[1].toInt()
        return e - s + 1
    }


}
