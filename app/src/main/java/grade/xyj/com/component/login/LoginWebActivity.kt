package grade.xyj.com.component.login

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import grade.xyj.com.R
import grade.xyj.com.base.BaseTitleActivity
import grade.xyj.com.repository.UserInfoRepository
import grade.xyj.com.util.Account
import grade.xyj.com.util.Http
import grade.xyj.com.util.Logger
import grade.xyj.com.util.URL
import grade.xyj.com.util.extend.*
import kotlinx.android.synthetic.main.fragment_web.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk27.coroutines.onClick

class LoginWebActivity(override val title: String = "登陆") : BaseTitleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.fragment_web,mContentView)
        floatbutton.setOnClickListener {
            when (progressBar.visibility) {
                View.INVISIBLE -> {
                    webView.reload()
                    progressBar.visibility = View.VISIBLE
                }

                View.VISIBLE -> {
                    webView.stopLoading()
                    progressBar.visibility = View.INVISIBLE
                }
            }
        }
        webView.run {
            settings.run {
                userAgentString = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Mobile/15E148 Safari/604.1"
                javaScriptEnabled = true
                //自适应屏幕
                useWideViewPort = true
                loadWithOverviewMode = false

                cacheMode = WebSettings.LOAD_NO_CACHE
                domStorageEnabled = true
            }
            webChromeClient = WebChromeClientKt {
                onProgressChanged { _, i -> progressBar.progress = i }
            }
            webViewClient = WebViewClientKt {
                onShouldOverrideUrlLoading { webView, request ->
                    val url = request.url.toString()
                    webView.loadUrl(url)
                    if(url == "http://ehall.wtu.edu.cn/new/index.html"){
                        toastSuccess("登陆成功,5秒后跳转")
                        delayed(5_000){
                            withContext(Dispatchers.IO){
                                UserInfoRepository.getUserInfo()?.run {
                                    Account.name = first
                                    Account.department = second
                                }
                                Http.get(URL.GET_PERMISSION_URL)
                            }
                            setResult(888)
                            finish()
                        }
                    }
                    true
                }
                onPageFinished {
                    progressBar.visibility = View.INVISIBLE
                    floatbutton.imageResource = R.drawable.ic_autorenew_black_24dp
                }
                onPageStarted {
                    progressBar.visibility = View.VISIBLE
                    floatbutton.imageResource = R.drawable.ic_close_white_24dp
                }
            }
            loadUrl(URL.LOGIN_API)
        }
    }

    override fun onDestroy() {
        mContentView.removeAllViews()
        webView?.destroy()
        super.onDestroy()
    }
}