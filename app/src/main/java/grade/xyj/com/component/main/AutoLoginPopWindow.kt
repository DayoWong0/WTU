package grade.xyj.com.component.main

import android.annotation.SuppressLint
import android.app.Activity
import android.view.Gravity
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.afollestad.materialdialogs.MaterialDialog
import grade.xyj.com.App
import grade.xyj.com.R
import grade.xyj.com.util.*
import grade.xyj.com.util.extend.delayed
import grade.xyj.com.util.extend.launchWithLifecycle
import grade.xyj.com.util.extend.runNoResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.jetbrains.anko.*

@SuppressLint("ResourceType")
class AutoLoginPopWindow(val activity: AppCompatActivity) {
    private lateinit var textView: TextView
    private val popWindow: PopupWindow

    init {
        popWindow = PopupWindow(
            createView(activity),
            (App.instance.resources.displayMetrics.widthPixels * 0.88).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            isOutsideTouchable = false
            isTouchable = false
            elevation = App.instance.dip(12).toFloat()
            animationStyle = R.style.animZoomIn
        }
    }


    fun show(){
        popWindow.showAtLocation(activity.contentView, Gravity.TOP, 0, 0)
    }

    fun setText(msg: String) {
        textView.text = msg
    }

    fun dismiss() {
        if (popWindow.isShowing) {
            popWindow.dismiss()
        }
    }

    private fun createView(activity: Activity) = activity.UI {
        verticalLayout {
            backgroundColorResource = R.color.login_pop_bg
            textView = textView {
                textSize = 16f
                padding = dip(10)
                textColorResource = R.color.dark_text_color
            }.lparams(wrapContent, wrapContent) {
                gravity = Gravity.CENTER
            }
        }
    }.view
}