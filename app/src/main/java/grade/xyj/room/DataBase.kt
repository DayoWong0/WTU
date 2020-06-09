package grade.xyj.room

import androidx.room.Database
import androidx.room.RoomDatabase
import grade.xyj.room.dao.CourseDao
import grade.xyj.room.dao.GroupDao
import grade.xyj.room.dao.QuestionkDao
import grade.xyj.room.entity.CourseEntity
import grade.xyj.room.entity.GroupEntity
import grade.xyj.room.entity.QuestionEntity

@Database(entities = [CourseEntity::class, GroupEntity::class, QuestionEntity::class], version = 1,exportSchema = false)
abstract class DataBase : RoomDatabase(){

    abstract fun groupDao():GroupDao

    abstract fun courseDao():CourseDao

    abstract fun qBankDao():QuestionkDao
}