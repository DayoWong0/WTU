package grade.xyj.com.component.course

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import grade.xyj.com.R
import grade.xyj.com.classchedule.event.CourseDataChangeEvent
import grade.xyj.com.util.DataUtil
import grade.xyj.com.util.Preferences
import grade.xyj.com.util.extend.defaultBus
import grade.xyj.com.util.extend.toastError
import grade.xyj.com.util.extend.toastSuccess
import grade.xyj.com.view.LittleCourseView
import grade.xyj.com.view.LittleParse
import grade.xyj.room.entity.CourseEntity
import grade.xyj.room.impl.CourseDaoImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.*

object CourseUtil {
    fun getWeekSelectView(context: Context, courses: List<CourseEntity>, week: Int): View {
        val littleParse = LittleParse(courses,week)
        val littleView = LittleCourseView(context, littleParse).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    .apply {
                        topMargin = 5
                        gravity = Gravity.CENTER
                    }

        }
        val view =context.UI {
            verticalLayout {
                padding = dip(5)
                frameLayout {
                    textView("第") {
                        textSize = 9f
                    }.lparams {
                        marginStart = dip(2)
                        gravity = Gravity.BOTTOM }

                    textView(week.toString()) {
                        textSize = 13f
                    }.lparams { gravity = Gravity.CENTER }

                    textView("周") {
                        textSize = 9f
                    }.lparams {
                        gravity = (Gravity.BOTTOM or Gravity.END)
                        marginEnd = dip(2)
                    }

                }.lparams {
                    width = matchParent
                    height = wrapContent
                }
            }
        }.view as LinearLayout

        return view.apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                leftMargin = 10
                rightMargin = 10
                gravity = LinearLayout.VERTICAL
            }
            tag = week
            addView(littleView)
        }
    }

    fun doImportCourse(context: Context, year: String, xq: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val currentCsNameId = Preferences.getLong(
                    context.getString(R.string.app_preference_current_cs_name_id), 0L)
            val courses = DataUtil.getCourse(year, xq, currentCsNameId)
            if (courses == null)
                context.toastError("网络错误")
            else {
                CourseDaoImpl.deleteByGroupId(currentCsNameId)
                CourseDaoImpl.insert(courses)
                defaultBus.post(CourseDataChangeEvent)
                context.toastSuccess("导入成功")
            }
        }
    }


}