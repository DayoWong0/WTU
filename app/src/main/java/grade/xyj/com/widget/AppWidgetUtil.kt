package grade.xyj.com.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import grade.xyj.com.R
import grade.xyj.com.util.Constant
import grade.xyj.com.util.Settings
import grade.xyj.com.util.TimeUtils

object AppWidgetUtil {
    fun updateWidget(context: Context) =
            context.sendBroadcast(Intent().apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            })

    fun updateWeekWidget(context: Context, appWidgetManager: AppWidgetManager,
                         appWidgetId: Int) {

        val color = Settings.widgetTitleTextColor

        val currentWeek = TimeUtils.getCurrentWeek()
        val days = TimeUtils.getDays(TimeUtils.getFirstWeekDay(), currentWeek)

        val ids = intArrayOf(R.id.tv_title1, R.id.tv_title2, R.id.tv_title3, R.id.tv_title4, R.id.tv_title5, R.id.tv_title6, R.id.tv_title7)
        val views = RemoteViews(context.packageName, R.layout.widget_allweek).apply {
            setTextViewText(R.id.widget_day, TimeUtils.todayString)
            setTextColor(R.id.widget_day, color)

            setTextViewText(R.id.widget_week, "第${currentWeek}周")
            setTextColor(R.id.widget_week, color)

            setTextViewText(R.id.tv_title0, "${days[0]}\n月")
            setTextColor(R.id.tv_title0, color)
            for (i in 1..7) {
                setTextViewText(ids[i - 1], "${Constant.WEEK_SINGLE[i - 1]}\n${days[i]}")
                setTextColor(ids[i - 1], color)
            }
            setRemoteAdapter(R.id.widget_list, Intent(context, AllWeekWidgetService::class.java))
        }
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    fun updateTodayWidget(context: Context, appWidgetManager: AppWidgetManager,
                          appWidgetId: Int) {
        val currentWeek = TimeUtils.getCurrentWeek()
        val views = RemoteViews(context.packageName, R.layout.widget_today).apply {
            val color = Settings.widgetTodayTitleTextColor
            setTextViewText(R.id.widget_day, TimeUtils.todayString)
            setTextColor(R.id.widget_day,color)
            setTextViewText(R.id.widget_week, "第${currentWeek}周")
            setTextColor(R.id.widget_week,color)
            setRemoteAdapter(R.id.widget_today_list, Intent(context, TodayWidgetServer::class.java))
        }
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_today_list)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }


}