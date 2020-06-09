package grade.xyj.com

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.blankj.utilcode.util.LogUtils
import grade.xyj.com.util.Preferences
import grade.xyj.com.util.Settings
import grade.xyj.room.DataBaseManager
import grade.xyj.room.entity.GroupEntity
import grade.xyj.room.greendao.MigrationHelper
import grade.xyj.room.impl.GroupDaoImpl
import org.acra.annotation.AcraCore

@AcraCore
class App : Application() {

    companion object {
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Preferences.init(this)
        Settings.init(this)
        DataBaseManager.init(this)
        MigrationHelper.migration(this)
        initIfFirstStart()
        //夜间模式
        AppCompatDelegate.setDefaultNightMode(if (Settings.nightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }


    private fun initIfFirstStart() {
        val isFirst =
            Preferences.getBoolean("first_start", true)
        if (!isFirst) {
            return
        }
        Preferences.putBoolean("first_start", false)

        val default = GroupDaoImpl.getAll().firstOrNull()
        val insert = if (default == null) {
            GroupDaoImpl.insert(GroupEntity("默认课表", 100))
        } else {
            default.id!!
        }
        Preferences.putLong(getString(R.string.app_preference_current_cs_name_id), insert)
    }

}
