package grade.xyj.room.greendao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import grade.xyj.com.util.Preferences
import grade.xyj.com.util.extend.toastError
import grade.xyj.room.entity.CourseEntity
import grade.xyj.room.entity.GroupEntity
import grade.xyj.room.impl.CourseDaoImpl
import grade.xyj.room.impl.GroupDaoImpl

object MigrationHelper {

    fun migration(context: Context) {
        val needMigrationKey = "needMigration"
        val needMigration = Preferences.getBoolean(needMigrationKey, true)
        if (!needMigration) {
            return
        }
        val courseDB = context.getDatabasePath("course.db")
        if (!courseDB.exists()) {
            Preferences.putBoolean(needMigrationKey, false)
            return
        }
        try {
            val dataBase =
                SQLiteDatabase.openDatabase(
                    courseDB.absolutePath,
                    null,
                    SQLiteDatabase.OPEN_READWRITE
                )
            var cursor = dataBase.rawQuery("select * from COURSE", null)
            cursor.moveToFirst()
            val courses = mutableListOf<CourseEntity>()
            do {
                val name = cursor.getString(1)
                val local = cursor.getString(2)
                val teacher = cursor.getString(3)
                val week = cursor.getInt(4)
                val start = cursor.getInt(5)
                val count = cursor.getInt(6)
                val allWeek = cursor.getString(7)
                val color = cursor.getInt(8)
                val gid = cursor.getLong(9).run {
                    if (this == 0L) 100 else this
                }
                courses.add(
                    CourseEntity(
                        name,
                        local,
                        teacher,
                        week,
                        start,
                        count,
                        allWeek,
                        color,
                        gid
                    )
                )
            } while (cursor.moveToNext())
            CourseDaoImpl.insert(courses)
            cursor.close()
            //group
            cursor = dataBase.rawQuery("select * from COURSE_GROUP", null)
            val groups = mutableListOf<GroupEntity>()
            cursor.moveToFirst()
            do {
                val id = cursor.getLong(0).run { if (this == 0L) 100L else 0L }
                val name = cursor.getString(1)
                groups.add(GroupEntity(name, id))
            } while (cursor.moveToNext())
            GroupDaoImpl.insert(groups)
            Preferences.putBoolean(needMigrationKey, false)
            cursor.close()
            dataBase.close()
            courseDB.delete()
        } catch (e: Exception) {
            val needMigration1 = Preferences.getBoolean(needMigrationKey, true)
            if (needMigration1) {
                context.toastError("迁移数据库失败\n$e")
            }
        }
    }


}