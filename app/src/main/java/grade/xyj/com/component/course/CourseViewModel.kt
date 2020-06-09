package grade.xyj.com.component.course

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import grade.xyj.com.App
import grade.xyj.com.util.Settings
import grade.xyj.com.util.TimeUtils
import grade.xyj.com.widget.AppWidgetUtil
import grade.xyj.room.entity.CourseEntity
import grade.xyj.room.impl.CourseDaoImpl
import grade.xyj.room.impl.GroupDaoImpl
import kotlinx.coroutines.launch

class CourseViewModel : ViewModel() {
    val currentWeek = MutableLiveData<Int>()
    val courses = MutableLiveData<List<CourseEntity>>()
    val cgName = MutableLiveData<String>()
    val days = MutableLiveData<IntArray>()

    private fun setCourses() {
        val cgId = Settings.groupId
        //修改课表名称
        cgName.value = GroupDaoImpl.getGroupById(cgId)!!.name
        //设置课程
        courses.value = CourseDaoImpl.getCoursesByGroupId(cgId).apply { forEach { it.init() } }
    }

    fun setDays() {
        days.value = TimeUtils.getDays(TimeUtils.getFirstWeekDay(), currentWeek.value!!)
    }

    fun doImport(year: String, xq: String) = viewModelScope.launch {
        val cgId = Settings.groupId

    }

    fun updateCoursePreference() {
        currentWeek.value = TimeUtils.getCurrentWeek()
        setCourses()
        AppWidgetUtil.updateWidget(App.instance)
    }


}