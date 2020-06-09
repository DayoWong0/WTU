package grade.xyj.com.update

import android.content.Intent
import android.os.IBinder
import grade.xyj.com.BuildConfig
import grade.xyj.com.base.BaseService
import grade.xyj.com.util.Http
import grade.xyj.com.util.Logger
import grade.xyj.com.util.extend.defaultBus
import grade.xyj.com.util.extend.toBean
import grade.xyj.com.util.extend.toJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateService : BaseService() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        launch(Dispatchers.IO) {
            //val updateInfo = UpdateInfo(91,"","哈哈哈\n嘿嘿嘿",false)
            val updateInfo = getUpdateInfo()
            if (updateInfo != null) {
                Logger.i("获取到更新信息:${updateInfo.toJson()}")
                if(updateInfo.version > BuildConfig.VERSION_CODE){
                    defaultBus.post(updateInfo)
                }
            }
        }
    }

    suspend fun getUpdateInfo(): UpdateInfo? = withContext(Dispatchers.IO) {
        val url = "http://112.74.185.21:8082/wfh/update"
        val json = Http.get(url)?.body?.string() ?: return@withContext null
        try {
            return@withContext json.toBean<UpdateInfo>()
        } catch (e: Throwable) {

        }
        null
    }
}