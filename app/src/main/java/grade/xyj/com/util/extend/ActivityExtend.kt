package grade.xyj.com.util.extend

import android.app.Activity
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.jaeger.library.StatusBarUtil
import grade.xyj.com.util.Settings
import grade.xyj.com.util.extend.dsl.LifecycleObserverDSL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.*

//viewModel
inline fun <reified T : ViewModel> FragmentActivity.getViewModel(): T = ViewModelProviders.of(this).get(T::class.java)

//delayed
inline fun <reified T : Activity> ComponentActivity.startActivityDelayed(delayMillis: Long) = delayed(delayMillis) { startActivity<T>() }

fun ComponentActivity.delayed(delayMillis: Long, action: suspend CoroutineScope.() -> Unit) {
    launchWithLifecycle {
        delay(delayMillis)
        action()
    }
}

fun ComponentActivity.autoStatusBarModel(){
    if(Settings.nightMode){
        StatusBarUtil.setDarkMode(this)
    }else{
        StatusBarUtil.setLightMode(this)
    }
}


//dialog
fun Activity.showErrorDialog(text: String) = alert(text, "错误") { yesButton { } }.show()

fun Activity.showDialog(title: String, msg: String) = alert(msg, title) { yesButton { } }.show()
fun Activity.showProgressDialog(msg: String) = progressDialog(msg) {
    setProgressStyle(ProgressDialog.STYLE_SPINNER)
    setCancelable(false)
    isIndeterminate = true
}

fun ComponentActivity.launchWithLifecycle(block: suspend CoroutineScope.() -> Unit) {
    val job = CoroutineScope(Dispatchers.Main).launch {
        block()
    }
    lifecycle.addObserver(LifecycleObserverDSL(this) {
        onDestroy {
            job.cancel()
        }
    })
}

fun AppCompatActivity.setWindow() {
    supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
        else -> {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
}