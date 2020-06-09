package grade.xyj.com.component.addcourse

import android.graphics.Color
import androidx.lifecycle.ViewModel
import grade.xyj.com.util.Settings
import grade.xyj.room.entity.CourseEntity
import grade.xyj.room.impl.CourseDaoImpl

class AddCourseViewModel :ViewModel(){
    val courses = mutableListOf<CourseEntity>()
    val cgId = Settings.groupId
    lateinit var key:String
    lateinit var courseName:String
    var color = Color.RED
    fun init(key:String){
        this.key = key
        courseName = key
        courses.addAll(CourseDaoImpl.getCourseByCgIdAndName(cgId,key))
        if(courses.isNotEmpty()) color = courses[0].courseColor
        else courses.add(CourseEntity.DELFAULT_COUSE)
    }

    fun save(){
        CourseDaoImpl.deleteCourseByGroupIdAndName(cgId,key)
        courses.forEach {
            it.courseColor = color
            it.name = courseName
            it.groupId = cgId
            CourseDaoImpl.insert(it)
        }
    }

}