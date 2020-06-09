package grade.xyj.com.util.extend

import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import grade.xyj.com.App
import org.greenrobot.eventbus.EventBus


fun String.toClipboard() {
    val clip = App.instance.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clip.text = this
}

inline fun <T> T.runNoResult(block: T.() -> Unit) = block()

fun Any.log(msg: String) = Log.e(this::class.java.simpleName, msg)

val defaultBus = EventBus.getDefault()


