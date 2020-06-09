package grade.xyj.room.dao

import androidx.room.*
import grade.xyj.room.entity.CourseEntity

@Dao
interface CourseDao {

    @Query("select * from tb_course where groupId = :cgId")
    fun getCoursesByGroupId(cgId: Long): MutableList<CourseEntity>

    @Delete(entity = CourseEntity::class)
    fun delete(courseEntity: CourseEntity)

    @Update(entity = CourseEntity::class)
    fun update(courseEntity: CourseEntity)

    @Insert(entity = CourseEntity::class)
    fun insert(courseEntity: CourseEntity)

}