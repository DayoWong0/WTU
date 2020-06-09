package grade.xyj.com.component.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import grade.xyj.com.login.CaptchaData
import grade.xyj.com.login.LoginStatus
import grade.xyj.com.login.SchoolLoginClient
import grade.xyj.com.repository.UserInfoRepository
import grade.xyj.com.util.Account
import grade.xyj.com.util.Http
import grade.xyj.com.util.URL
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    val captchaLiveData = MutableLiveData<CaptchaData>()
    val loginStatus = MutableLiveData<LoginStatus>()

    private var loginJob: Job? = null

    fun autoLogin() {
        loginJob = viewModelScope.launch(
            CoroutineExceptionHandler { _, _ ->
                loginStatus.postValue(LoginStatus.NET_ERROR)
            } + Dispatchers.IO
        ) {
            if (!checkLogin()) {
                if (Account.useWeb) {
                    loginStatus.postValue(LoginStatus.NEED_OPEN_WEB)
                } else {
                    //需要重新登陆
                    val client = SchoolLoginClient(Account.cno, Account.passWord)
                    val result = client.start {
                        val channel = Channel<String>()
                        captchaLiveData.postValue(CaptchaData(it, channel))
                        channel.receive()
                    }
                    if (result == LoginStatus.SUCCESS) {
                        client.cookieStore.forEach { (url, cookies) ->
                            cookies.forEach {
                                Http.cookieManager.setCookie(url, it.toString())
                            }
                        }
                        Http.get(URL.GET_PERMISSION_URL)
                    }
                    loginStatus.postValue(result)
                }
            } else {
                val userInfoPair = UserInfoRepository.getUserInfo()
                if(userInfoPair != null){
                    Account.name = userInfoPair.first
                    Account.department = userInfoPair.second
                }
                Http.get(URL.GET_PERMISSION_URL)
                loginStatus.postValue(LoginStatus.SUCCESS)
            }
        }
    }

    fun checkLogin() = Http.get(URL.CARD_URL)?.body?.string()?.contains("圈存充值") == true

    fun submitCaptcha(code: String, channel: Channel<String>) = viewModelScope.launch {
        channel.send(code)
        channel.close()
    }

    fun cancel() {
        loginJob?.cancel()
    }
}