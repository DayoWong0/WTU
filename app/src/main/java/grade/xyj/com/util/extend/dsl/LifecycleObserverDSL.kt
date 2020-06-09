package grade.xyj.com.util.extend.dsl

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import grade.xyj.com.util.extend.log
import grade.xyj.com.util.extend.runNoResult
import java.lang.ref.WeakReference

class LifecycleObserverDSL(lifecycleOwner: LifecycleOwner, listenerBuilder: ListenerBuilder.() -> Unit) : LifecycleObserver {

    private val lifecycleOwner: WeakReference<LifecycleOwner> = WeakReference(lifecycleOwner)

    private val mListener: ListenerBuilder = ListenerBuilder().also(listenerBuilder)

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mListener.mDestroyAction?.invoke()
        lifecycleOwner.get()?.lifecycle?.removeObserver(this@LifecycleObserverDSL)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() = mListener.mCreateAction?.invoke()


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() = mListener.mStartAction?.invoke()


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() = mListener.mResumeAction?.invoke()


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() = mListener.mPauseAction?.invoke()


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() = mListener.mStopAction?.invoke()


    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAny() = mListener.mAnyAction?.invoke()


    inner class ListenerBuilder {
        internal var mPauseAction: (() -> Unit)? = null
        internal var mStartAction: (() -> Unit)? = null
        internal var mStopAction: (() -> Unit)? = null
        internal var mCreateAction: (() -> Unit)? = null
        internal var mDestroyAction: (() -> Unit)? = null
        internal var mResumeAction: (() -> Unit)? = null
        internal var mAnyAction: (() -> Unit)? = null


        fun onAny(action: () -> Unit) {
            mAnyAction = action
        }

        fun onCreate(action: () -> Unit) {
            mCreateAction = action
        }

        fun onStart(action: () -> Unit) {
            mStartAction = action
        }

        fun onResume(action: () -> Unit) {
            mResumeAction = action
        }

        fun onPause(action: () -> Unit) {
            mPauseAction = action
        }

        fun onStop(action: () -> Unit) {
            mStopAction = action
        }

        fun onDestroy(action: () -> Unit) {
            mDestroyAction = action
        }

    }

}