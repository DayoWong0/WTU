package grade.xyj.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import grade.xyj.com.bean.CourseAncestor
import grade.xyj.com.classchedule.util.Utils
import grade.xyj.com.util.Settings

@Entity(tableName = "tb_course")
data class CourseEntity(
    var name: String,
    var location: String,
    var teacher: String,
    var week: Int,
    var startNode: Int,
    var nodeCount: Int,
    var allWeek: String,
    var courseColor: Int,
    var groupId: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
) : CourseAncestor() {

    companion object {
        val DELFAULT_COUSE
            get() = CourseEntity(
                "",
                "",
                "",
                1,
                1,
                1,
                "1",
                Utils.getRandomColor(),
                Settings.groupId
            )

        fun list2WeekString(list: List<Int>): String {
            val builder = StringBuilder()
            for (i in list) {
                builder.append(i).append(",")
            }
            val str = builder.toString()
            return str.substring(0, str.length - 1)
        }
    }

    fun init() {
        setRow(week!!)
        setColor(courseColor!!)
        showIndexes.clear()
        kotlin.runCatching {
            allWeek!!.split(",").forEach {
                addIndex(it.toInt())
            }
        }
        if (nodeCount != 0) {
            setCol(startNode!!)
            setRowNum(nodeCount!!)
        }
        if (location!!.isBlank()) {
            setText(name)
        } else {
            setText("$name\n@$location")
        }
    }
}