package grade.xyj.com.component.addcourse

import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import grade.xyj.com.R
import grade.xyj.com.util.Constant
import grade.xyj.room.entity.CourseEntity

class AddCourseAdapter(da:MutableList<CourseEntity>,val font:Typeface) :BaseQuickAdapter<CourseEntity, BaseViewHolder>(R.layout.item_add_course_detail,da){
    private val ids = intArrayOf(R.id.ic_1,R.id.ic_2,R.id.ic_3,R.id.ic_4,R.id.ib_delete)
    override fun convert(helper: BaseViewHolder, item: CourseEntity) {
        item.init()
        helper.run {
            ids.forEach { getView<TextView>(it).typeface = font }
            setText(R.id.et_room, item.location)
            setText(R.id.et_teacher, item.teacher)
            setText(R.id.et_time,getNodeInfo(item))
            setText(R.id.et_weeks, getWeekString(item))

            addOnClickListener(R.id.ib_delete)
            addOnClickListener(R.id.ll_weeks)
            addOnClickListener(R.id.ll_time)

            getView<EditText>(R.id.et_room).addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =Unit

                override fun afterTextChanged(s: Editable) {
                    item.location = s.toString()
                }
            })
            getView<EditText>(R.id.et_teacher).addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =Unit

                override fun afterTextChanged(s: Editable) {
                    item.teacher = s.toString()
                }
            })
        }
    }
    private fun getNodeInfo(course: CourseEntity): String {
        val nodeInfo = StringBuilder(Constant.WEEK[course.week!!]).append("   ")
        if (course.nodeCount == 1) {
            nodeInfo.append("第")
        }
        nodeInfo.append(course.startNode)
        if (course.nodeCount!! > 1) {
            nodeInfo.append("-")
            nodeInfo.append(course.startNode!! + course.nodeCount!! - 1)
        }
        nodeInfo.append("节")
        return nodeInfo.toString()
    }

    private fun getWeekString(course: CourseEntity):String{
        return course.showIndexes[0].toString() + " - " +
                course.showIndexes[course.showIndexes.size - 1] + " 周"
    }

}