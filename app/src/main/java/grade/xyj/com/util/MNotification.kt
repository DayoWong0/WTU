package grade.xyj.com.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import grade.xyj.com.App
import grade.xyj.com.R

class MNotification(val title:String,val id:Int) {
    companion object{
        const val UPDATE_ID = 666
    }
    private val manager: NotificationManager by lazy { App.instance.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val channelId = "666"

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, App.instance.packageName, NotificationManager.IMPORTANCE_DEFAULT).apply {
                enableLights(false)
                lockscreenVisibility = Notification.VISIBILITY_SECRET
            }
            manager.createNotificationChannel(channel)
        }
    }

    fun setProgress(progress: Float,max:Int = 100) {
        val notification = NotificationCompat
                .Builder(App.instance, channelId)
                .setAutoCancel(false)
                .setContentText(title)
                .setContentTitle(String.format("进度:%.2f%%", progress))
                .setSmallIcon(R.drawable.ic_arrow_downward_black_24dp)
                .setProgress(100, progress.toInt(), false)
                .setWhen(System.currentTimeMillis())
                .build()

        manager.notify(id, notification)
    }

    fun cancel() {
        manager.cancel(id)
    }


}