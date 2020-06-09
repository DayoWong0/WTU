package grade.xyj.com.component.groupmg

import androidx.lifecycle.ViewModel
import grade.xyj.com.util.Settings
import grade.xyj.room.impl.GroupDaoImpl

class ScheduleManageViewModel :ViewModel(){
    val groups = GroupDaoImpl.getAll()
    var cgId = Settings.groupId

    fun setId(id:Long){
        cgId = id
        Settings.groupId = id
    }
}