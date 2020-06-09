package grade.xyj.com.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent

class TodayWidget : AppWidgetProvider() {
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if(intent.action == "android.appwidget.action.APPWIDGET_UPDATE"){
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val com = ComponentName("grade.xyj.com","grade.xyj.com.widget.TodayWidget")

            appWidgetManager.getAppWidgetIds(com).forEach {
                AppWidgetUtil.updateTodayWidget(context, appWidgetManager, it)
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

        for (appWidgetId in appWidgetIds) {
            AppWidgetUtil.updateTodayWidget(context,appWidgetManager,appWidgetId)
        }
    }

}

