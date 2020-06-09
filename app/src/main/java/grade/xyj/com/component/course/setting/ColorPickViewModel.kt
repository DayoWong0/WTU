package grade.xyj.com.component.course.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ColorPickViewModel :ViewModel(){
    val result = MutableLiveData<Pair<Int,Int>>()
}