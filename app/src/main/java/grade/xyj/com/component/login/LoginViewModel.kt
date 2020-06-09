package grade.xyj.com.component.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.xyj.xnative.NativeUtils
import grade.xyj.com.login.CaptchaData
import grade.xyj.com.login.LoginStatus
import grade.xyj.com.login.SchoolLoginClient
import grade.xyj.com.repository.UserInfoRepository
import grade.xyj.com.util.Account
import grade.xyj.com.util.Http
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginViewModel : ViewModel() {

    val captchaLiveData = MutableLiveData<CaptchaData>()
    val loginStatus = MutableLiveData<LoginStatus>()
    lateinit var userName: String
    lateinit var passWord: String


    fun login(userName: String, passWord: String) {
        this.userName = userName
        this.passWord = passWord
        viewModelScope.launch(
            CoroutineExceptionHandler { coroutineContext, throwable ->
                loginStatus.postValue(LoginStatus.NET_ERROR)
            } + Dispatchers.IO
        ) {
            val client =
                SchoolLoginClient(userName, passWord)
            val result = client.start {
                val channel = Channel<String>()
                captchaLiveData.postValue(
                    CaptchaData(
                        it,
                        channel
                    )
                )
                channel.receive()
            }
            if (result == LoginStatus.SUCCESS) {
                client.cookieStore.forEach { (url, cookies) ->
                    cookies.forEach {
                        Http.cookieManager.setCookie(url, it.toString())
                    }
                }
                UserInfoRepository.getUserInfo()?.run {
                    Account.name = first
                    Account.department = second
                }
            }
            loginStatus.postValue(result)
        }
    }

    fun submitCaptcha(code: String, channel: Channel<String>) = viewModelScope.launch {
        channel.send(code)
        channel.close()
    }
}