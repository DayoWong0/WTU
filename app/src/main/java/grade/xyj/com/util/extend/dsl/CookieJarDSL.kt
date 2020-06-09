package grade.xyj.com.util.extend.dsl

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CookieJarDSL(listenerBuilder: ListenerBuilder.() -> Unit): CookieJar {
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        mListener.mSaveFromResponseAction?.invoke(url,cookies)
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> =
        mListener.mLoadForRequestAction?.invoke(url)?: listOf()

    private val mListener: ListenerBuilder = ListenerBuilder().also(listenerBuilder)


    inner class ListenerBuilder {
        internal var mSaveFromResponseAction: ((HttpUrl,List<Cookie>) -> Unit)? = null
        internal var mLoadForRequestAction: ((HttpUrl) -> List<Cookie>)? = null

        fun onSaveFromResponse(action: (HttpUrl,List<Cookie>) -> Unit) {
            mSaveFromResponseAction = action
        }

        fun onLoadForRequest(action: (HttpUrl) -> List<Cookie>) {
            mLoadForRequestAction = action
        }
    }



}