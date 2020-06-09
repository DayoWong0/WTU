package grade.xyj.com.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.TextView
import grade.xyj.com.R
import grade.xyj.com.util.Constant
import grade.xyj.com.util.Settings
import grade.xyj.com.util.extend.layout
import grade.xyj.com.util.extend.runNoResult
import grade.xyj.room.entity.CourseEntity
import grade.xyj.room.impl.CourseDaoImpl
import org.jetbrains.anko.dip
import org.jetbrains.anko.find
import org.jetbrains.anko.textColor
import kotlin.math.roundToInt

class TodayWidgetServer : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory = TodayWidgetFactory(this)
}

class TodayWidgetFactory(val context: Context) : RemoteViewsService.RemoteViewsFactory {

    val courses = mutableListOf<CourseEntity>()

    override fun getViewAt(position: Int): RemoteViews = getTodayItemView(position)

    override fun getCount(): Int = courses.size

    @SuppressLint("SetTextI18n", "InflateParams")
    private fun getTodayItemView(position: Int): RemoteViews =
        RemoteViews(context.packageName, R.layout.widget_item_today).apply {

            val course = courses[position]

            val view = LayoutInflater.from(context).inflate(R.layout.widget_item_today_layout, null)
                .apply {

                    val font = Typeface.createFromAsset(context.assets, "fonts/iconfont.ttf")
                    val color = Settings.widgetTodayTextColor
                    val mAlpha = (255 * (Settings.widgetTodayAlpha.toFloat() / 100)).roundToInt()
                    find<TextView>(R.id.widget_today_time1).runNoResult {
                        //typeface = font
                        text =
                            "${course.startNode}   ${Constant.STIMES[course.startNode - 1]}"
                        textColor = color
                    }
                    find<TextView>(R.id.widget_today_coursename).runNoResult {
                        text = course.name
                        textColor = color
                    }
                    find<TextView>(R.id.widget_today_tv2).run {
                        //typeface = font
                        textColor = color
                        val t = course.startNode + course.nodeCount - 1
                        text =
                            "$t   ${Constant.XTIMES[t - 1]}    \uE6B2  ${course.location}    \uE6EB  ${course.teacher}"
                    }
                    (find<LinearLayout>(R.id.widget_today_bg).background as GradientDrawable).runNoResult {
                        setColor(course.courseColor)
                        alpha = mAlpha
                    }
                }
            view.layout(context.dip(375), 0)
            val bitmap = Bitmap.createBitmap(
                view.measuredWidth, view.measuredHeight,
                Bitmap.Config.ARGB_8888
            )
            view.draw(Canvas(bitmap))
            setBitmap(R.id.widget_today_img, "setImageBitmap", bitmap)
            (view as ViewGroup).removeAllViews()
        }

    private fun emptyRemoteViews() =
        RemoteViews(context.packageName, R.layout.widget_item_empty).apply {
            setTextColor(R.id.widget_item_empty_tv, Settings.widgetTodayTitleTextColor)
        }

    override fun onCreate() {
        courses.addAll(CourseDaoImpl.getTodayCourse())
    }


    override fun onDataSetChanged() {
        courses.clear()
        courses.addAll(CourseDaoImpl.getTodayCourse())
    }


    override fun hasStableIds(): Boolean = false
    override fun getViewTypeCount(): Int = 1
    override fun getLoadingView(): RemoteViews? = null
    override fun getItemId(position: Int): Long = position.toLong()
    override fun onDestroy() {
        courses.clear()
    }

}