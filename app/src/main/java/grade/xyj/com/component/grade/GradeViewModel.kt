package grade.xyj.com.component.grade

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import grade.xyj.com.util.DataUtil
import kotlinx.coroutines.launch

class GradeViewModel : ViewModel() {

    val nGrades = MutableLiveData<List<Level0Item>?>()

    fun getGrades(xq: String, xn: String) = viewModelScope.launch {
        nGrades.value = DataUtil
                .getNGrades(DataUtil.getGrades(xq, xn))?.map {
                    Level0Item(it)
                }
    }
}