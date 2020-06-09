package grade.xyj.com.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.ScrollView
import grade.xyj.com.R
import grade.xyj.com.util.Logger
import grade.xyj.com.util.createAllWeekView
import grade.xyj.com.util.extend.getViewBitmap
import grade.xyj.com.util.extend.layout
import org.jetbrains.anko.dip
import org.jetbrains.anko.find

class AllWeekWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory = WidgetFactory(this, intent)
}

class WidgetFactory(val context: Context, val intent: Intent) : RemoteViewsService.RemoteViewsFactory {

    override fun getViewAt(position: Int): RemoteViews {
        Logger.e("get view at $position")
        val view = createAllWeekView(context)
        Logger.e("createAllWeekView success")
        val scrollView = view.find<ScrollView>(0)
        scrollView.layout(context.dip(375), context.dip(375))

        val views = RemoteViews(context.packageName, R.layout.widget_item_allweek)
        views.setBitmap(R.id.iv_schedule, "setImageBitmap", getViewBitmap(scrollView))
        scrollView.removeAllViews()
        return views
    }


    override fun onDestroy() = Unit
    override fun onCreate() = Unit
    override fun onDataSetChanged() = Unit
    override fun getLoadingView(): RemoteViews? = null
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getCount(): Int = 1
    override fun getViewTypeCount(): Int = 1
    override fun hasStableIds(): Boolean = false

}