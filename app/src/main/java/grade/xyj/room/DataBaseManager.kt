package grade.xyj.room

import android.app.Application
import androidx.room.Room
import com.xyj.xnative.NativeUtils
import java.io.File

object DataBaseManager {

    lateinit var dataBase: DataBase

    val courseDao get() = dataBase.courseDao()
    val groupDao get() = dataBase.groupDao()
    val questionDao get() = dataBase.qBankDao()

    fun init(application: Application) {
        dataBase = Room.databaseBuilder(application, DataBase::class.java, "main")
            .allowMainThreadQueries()
            .build()
    }
}