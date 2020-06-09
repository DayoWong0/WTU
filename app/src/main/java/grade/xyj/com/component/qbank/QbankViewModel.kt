package grade.xyj.com.component.qbank

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ResourceUtils
import grade.xyj.com.bean.All
import grade.xyj.com.bean.Work
import grade.xyj.com.util.Settings
import grade.xyj.com.util.extend.toBean
import grade.xyj.room.entity.QuestionEntity
import grade.xyj.room.impl.QuestionDaoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QbankViewModel : ViewModel() {
    var isInit = false

    val showDialog = MutableLiveData<Boolean>()

    val qBanks = MutableLiveData<List<QuestionEntity>>()

    fun initFirstUse() {
        if (!Settings.firstUseBank) {
            isInit = true
            return
        }
        showDialog.value = true
        fun doWork(sub: String, work: Work) {
            work.dx.forEach {

                QuestionDaoImpl.insert(QuestionEntity(sub, "dx", it.topic, it.option, it.res))
            }
            work.ddx.forEach {
                QuestionDaoImpl.insert(QuestionEntity(sub, "ddx", it.topic, it.option, it.res))
            }
            work.pd.forEach {
                QuestionDaoImpl.insert(QuestionEntity(sub, "pd", it.topic, "", it.res))
            }
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val all = ResourceUtils.readAssets2String("json2.txt").toBean<All>()
                    doWork("jds", all.jds)
                    doWork("mks", all.mks)
                    doWork("mg", all.mg)
                    doWork("sx", all.sx)
                    showDialog.postValue(true)
                } catch (e: Exception) {
                    showDialog.postValue(false)
                }
            }
        }
    }

    fun query(sub: String, key: String) {
        if (key.isEmpty()) {
            qBanks.value = listOf()
            return
        }
        viewModelScope.launch {
            qBanks.postValue(QuestionDaoImpl.query(sub, key))
        }
    }


}