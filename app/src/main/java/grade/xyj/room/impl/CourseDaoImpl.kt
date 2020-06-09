package grade.xyj.room.impl

import grade.xyj.com.util.Settings
import grade.xyj.com.util.TimeUtils
import grade.xyj.room.DataBaseManager
import grade.xyj.room.dao.CourseDao
import grade.xyj.room.entity.CourseEntity

object CourseDaoImpl : CourseDao {

    private val dao = DataBaseManager.courseDao

    override fun getCoursesByGroupId(cgId: Long): MutableList<CourseEntity> {
        return dao.getCoursesByGroupId(cgId)
    }

    override fun delete(courseEntity: CourseEntity) {
        dao.delete(courseEntity)
    }

    override fun update(courseEntity: CourseEntity) {
        dao.update(courseEntity)
    }

    override fun insert(courseEntity: CourseEntity) {
        dao.insert(courseEntity)
    }

    fun insert(courseEntitys: List<CourseEntity>) {
        courseEntitys.forEach {
            dao.insert(it)
        }
    }

    fun deleteByGroupId(groupId: Long) {
        getCoursesByGroupId(groupId).forEach {
            delete(it)
        }
    }

    fun getCourseByCgIdAndName(groupId: Long, name: String): List<CourseEntity> {
        return getCoursesByGroupId(groupId)
            .filter { it.name == name }
    }

    fun deleteCourseByGroupIdAndName(groupId: Long, name: String) {
        getCourseByCgIdAndName(groupId, name)
            .forEach {
                delete(it)
            }
    }

    fun getTodayCourse(): List<CourseEntity> {
        val today = TimeUtils.dayOfWeek
        //val week = TimeUtils.getCurrentWeek()
        val groupId = Settings.groupId
        return getCoursesByGroupId(groupId)
            .filter { it.week == today }
            .apply {
                forEach {
                    it.init()
                }
            }
    }

}