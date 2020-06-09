package grade.xyj.com.component

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import grade.xyj.com.R
import grade.xyj.com.base.BaseTitleActivity
import grade.xyj.com.util.extend.WebChromeClientKt
import grade.xyj.com.util.extend.WebViewClientKt
import grade.xyj.com.util.extend.runNoResult
import kotlinx.android.synthetic.main.fragment_web.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk27.coroutines.onClick

class WebActivity : BaseTitleActivity() {

    override val title: String
        get() = "网页"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.fragment_web, mContentView)

        mTitle.text = intent.getStringExtra("title")

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
        webView.runNoResult {
            //clearCache(true)
            settings.run {
                javaScriptEnabled = true
                //缩放
                builtInZoomControls = true
                setSupportZoom(true)
                //自适应屏幕
                useWideViewPort = true
                loadWithOverviewMode = false

                cacheMode = WebSettings.LOAD_NO_CACHE
                domStorageEnabled = true
            }

            webChromeClient = WebChromeClientKt {
                onJsBeforeUnload { _, _, jsResult ->
                    jsResult.confirm()
                    true
                }

                onProgressChanged { _, i -> progressBar.progress = i }
            }

            webViewClient = WebViewClientKt {
                onShouldOverrideUrlLoading { webView, request ->
                    webView.loadUrl(request.url.toString())
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

            loadUrl(intent.getStringExtra("url"))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webView.destroy()
        frameLayout.removeAllViews()
    }

    override fun onBackPressed() {
        if (webView.canGoBack())
            webView.goBack()
        else
            finish()
    }
}