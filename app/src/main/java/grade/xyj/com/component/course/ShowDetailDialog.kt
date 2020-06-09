package grade.xyj.com.component.course

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import grade.xyj.com.R
import grade.xyj.com.classchedule.event.CourseDataChangeEvent
import grade.xyj.com.component.addcourse.AddCourseActivity
import grade.xyj.com.util.Constant
import grade.xyj.com.util.ShapeUtils
import grade.xyj.com.util.extend.defaultBus
import grade.xyj.room.entity.CourseEntity
import grade.xyj.room.impl.CourseDaoImpl
import org.jetbrains.anko.*


class ShowDetailDialog {

    private lateinit var mPopupWindow: PopupWindow

    /**
     * @param course 时间信息必须完整
     */
    @SuppressLint("SetTextI18n")
    fun show(fragment: CourseFragment, course: CourseEntity?) {
        course ?: return

        val activity = fragment.activity

        //屏幕宽度
        val dm = activity?.resources?.displayMetrics!!

        activity.window.run {
            attributes = attributes.apply { alpha = 0.5f }
        }

        //加载字体
        val typeface = Typeface.createFromAsset(activity.assets, "fonts/iconfont.ttf")


        mPopupWindow = PopupWindow(
            (dm.widthPixels * 0.8).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT
        )


        val popupView =
            LayoutInflater.from(activity).inflate(R.layout.fragment_course_detail, null).apply {
                background = ShapeUtils.radius(R.color.light_text_color, 8, context)
            }


        //设置图标
        val ids = intArrayOf(
            R.id.ic_1,
            R.id.ic_2,
            R.id.ic_3,
            R.id.ic_4,
            R.id.ib_delete,
            R.id.ib_edit,
            R.id.ib_delete_course
        )
        ids.forEach {
            popupView.findViewById<TextView>(it).typeface = typeface
        }

        //标题
        popupView.findViewById<TextView>(R.id.tv_item).run {
            textSize = 12f
            text = course.name
        }
        //周
        popupView.findViewById<TextView>(R.id.et_weeks).run {
            text = course.showIndexes[0].toString() + " - " +
                    course.showIndexes[course.showIndexes.size - 1] + " 周"
        }
        //时间
        popupView.findViewById<TextView>(R.id.et_time).run {
            text = getNodeInfo(course)
        }
        //教室
        popupView.findViewById<EditText>(R.id.et_room).run {
            isEnabled = false
            setText(course.location)
        }
        //老师
        popupView.findViewById<EditText>(R.id.et_teacher).run {
            isEnabled = false
            setText(course.teacher)
        }

        //取消按钮
        popupView.findViewById<View>(R.id.ib_delete)
            .setOnClickListener { v -> mPopupWindow.dismiss() }
        //编辑按钮
        popupView.findViewById<View>(R.id.ib_edit)
            .setOnClickListener { v -> edit(activity, course) }

        //删除按钮
        popupView.findViewById<View>(R.id.ib_delete_course).setOnClickListener { v ->
            activity.alert("确定要删除该门课程吗", "提示") {
                cancelButton { dismiss() }
                yesButton {
                    CourseDaoImpl.delete(course)
                    defaultBus.post(CourseDataChangeEvent)
                    dismiss()
                }
            }.show()
        }

        initWindow(activity, popupView)
    }

    private fun edit(activity: Activity, course: CourseEntity) {
        activity.startActivity(
            Intent(activity, AddCourseActivity::class.java).putExtra(
                "key",
                course.name
            )
        )
        dismiss()
    }

    private fun initWindow(activity: Activity, popupView: View) {
        mPopupWindow.contentView = popupView
        mPopupWindow.setBackgroundDrawable(ColorDrawable(0))
        mPopupWindow.isFocusable = true
        mPopupWindow.isTouchable = true
        mPopupWindow.isOutsideTouchable = true
        mPopupWindow.isClippingEnabled = true
        mPopupWindow.animationStyle = R.style.animZoomIn

        mPopupWindow.contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        mPopupWindow.showAtLocation(activity.window.decorView, Gravity.CENTER, 0, 0)

        mPopupWindow.setOnDismissListener {
            activity.window.run {
                attributes = attributes.apply { alpha = 1f }
            }
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

    private fun dismiss() {
        mPopupWindow.dismiss()
    }
}