package grade.xyj.com.util.extend

import android.graphics.Bitmap
import android.view.View
import android.webkit.*
import android.widget.CompoundButton
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener
import com.mikepenz.materialdrawer.model.SecondarySwitchDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

//drawer
inline fun SecondarySwitchDrawerItem.withOnCheckedChangeListener(crossinline action: (Boolean) -> Unit): SecondarySwitchDrawerItem = this.apply {
    withOnCheckedChangeListener(object : OnCheckedChangeListener {

        override fun onCheckedChanged(drawerItem: IDrawerItem<*>, buttonView: CompoundButton, isChecked: Boolean) =
                action(isChecked)
    })
}

inline fun AccountHeaderBuilder.withOnAccountHeaderListener(crossinline action: () -> Unit): AccountHeaderBuilder = this.apply {
    withOnAccountHeaderListener(object : AccountHeader.OnAccountHeaderListener {
        override fun onProfileChanged(view: View?, profile: IProfile<*>, current: Boolean): Boolean =
                true.apply { action() }
    })
}

inline fun DrawerBuilder.withOnDrawerItemClickListener(crossinline action: (Int) -> Unit): DrawerBuilder = this.apply {
    withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
        override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean =
                true.apply { action(drawerItem.identifier.toInt()) }
    })
}

//Webview

class WebChromeClientKt(listenerBuilder: ListenerBuilder.() -> Unit) : WebChromeClient() {

    private val mListener: ListenerBuilder = ListenerBuilder().also(listenerBuilder)

    override fun onJsBeforeUnload(view: WebView, url: String, message: String, result: JsResult): Boolean =
            mListener.mOnJsBeforeUnloadAction?.invoke(view,url,result)?:false

    override fun onProgressChanged(view: WebView, newProgress: Int) =
            mListener.mOnProgressChangedAction?.invoke(view, newProgress)?:Unit

    inner class ListenerBuilder {
        internal var mOnJsBeforeUnloadAction: ((WebView, String, JsResult) -> Boolean)? = null
        internal var mOnProgressChangedAction: ((WebView, Int) -> Unit)? = null

        fun onJsBeforeUnload(action: (WebView,String,JsResult) -> Boolean) {
            mOnJsBeforeUnloadAction = action
        }

        fun onProgressChanged(action: (WebView, Int) -> Unit) {
            mOnProgressChangedAction = action
        }
    }
}

class WebViewClientKt(listenerBuilder: ListenerBuilder.() -> Unit) : WebViewClient() {

    private val mListener = ListenerBuilder().also(listenerBuilder)

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean =
            mListener.mShouldOverrideUrlLoadingAction.run { this?.invoke(view, request) ?: false }

    override fun onPageFinished(view: WebView?, url: String?) {
        mListener.mOnPageFinishedAction?.invoke()
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        mListener.mOnPageStartedAction?.invoke()
    }

    inner class ListenerBuilder {
        internal var mShouldOverrideUrlLoadingAction: ((WebView, WebResourceRequest) -> Boolean)? = null
        internal var mOnPageFinishedAction: (() -> Unit)? = null
        internal var mOnPageStartedAction: (() -> Unit)? = null

        fun onShouldOverrideUrlLoading(action: (WebView, WebResourceRequest) -> Boolean) {
            mShouldOverrideUrlLoadingAction = action
        }
        fun onPageFinished(action: () -> Unit){
            mOnPageFinishedAction = action
        }
        fun onPageStarted(action: () -> Unit){
            mOnPageStartedAction = action
        }
    }
}

//okHttp cookieJar
class CookieJarKt(listenerBuilder: ListenerBuilder.() -> Unit):CookieJar{
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
       mListener.msaveFromResponseAction?.invoke(url,cookies)
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> =
            mListener.mloadForRequestAction?.invoke(url)?: listOf()

    private val mListener: ListenerBuilder = ListenerBuilder().also(listenerBuilder)


    inner class ListenerBuilder {
        internal var msaveFromResponseAction: ((HttpUrl,List<Cookie>) -> Unit)? = null
        internal var mloadForRequestAction: ((HttpUrl) -> List<Cookie>)? = null

        fun onSaveFromResponse(action: (HttpUrl, List<Cookie>) -> Unit) {
            msaveFromResponseAction = action
        }

        fun onloadForRequest(action: (HttpUrl) -> List<Cookie>) {
            mloadForRequestAction = action
        }
    }



}