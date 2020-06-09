package grade.xyj.com.component.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import grade.xyj.com.bean.NewsBean
import grade.xyj.com.util.DataUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class NewChildViewModel : ViewModel() {
    val data = mutableMapOf<String, MutableLiveData<List<NewsBean.BulletinListBean>?>>()

    val images = MutableLiveData<List<String>?>()

    fun getLiveData(id: String): MutableLiveData<List<NewsBean.BulletinListBean>?> {
        if (data[id] == null) {
            data[id] = MutableLiveData()
        }
        return data[id]!!
    }

    fun getItems(idd: String) = getLiveData(idd).value

    fun getNews(id: String) = viewModelScope.launch {
        data[id]?.value = DataUtil.getNews(id)
    }

    fun getBannerImages() = viewModelScope.launch {
        images.value = withContext(Dispatchers.IO) {
            try {
                Jsoup.connect("https://www.wtu.edu.cn")
                        .get()
                        .getElementsByClass("imgbox")
                        .first()
                        .getElementsByClass("img")
                        .map {
                            "https://www.wtu.edu.cn/" + it.attr("src")
                        }
            } catch (e: Exception) {
                null
            }
        }
    }
}