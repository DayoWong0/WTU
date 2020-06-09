package grade.xyj.com.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import grade.xyj.com.util.extend.log

/**
 * Implementation of App Widget functionality.
 */
class WeekWidget : AppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        log("onReceive ${intent.action}")
        if(intent.action == "android.appwidget.action.APPWIDGET_UPDATE"){
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val com = ComponentName("grade.xyj.com","grade.xyj.com.widget.WeekWidget")

            appWidgetManager.getAppWidgetIds(com).forEach {
                AppWidgetUtil.updateWeekWidget(context, appWidgetManager, it)
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        log("onUpdate")
        appWidgetIds.forEach {
            AppWidgetUtil.updateWeekWidget(context, appWidgetManager, it)
        }
    }

    override fun onEnabled(context: Context) {
        log("onEnabled")
    }

    override fun onDisabled(context: Context) {
        log("onDisabled")
    }

}

