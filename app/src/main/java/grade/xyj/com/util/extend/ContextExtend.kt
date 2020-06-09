package grade.xyj.com.util.extend

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Vibrator
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import grade.xyj.com.App
import grade.xyj.com.util.URL
import grade.xyj.com.util.toast.Toasty
import org.jetbrains.anko.browse


fun Context.toastError(s: String) = Toasty.error(this, s).show()
fun Context.toastSuccess(s: String) = Toasty.success(this, s).show()
fun Context.toastInfo(s: String) = Toasty.info(this, s).show()


fun Context.vibrate(time: Long) =
    (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(time)

fun Context.getColorByCompat(id: Int) = ContextCompat.getColor(this, id)

fun Context.widthPixels() = resources.displayMetrics.widthPixels
fun Context.heightPixels() = resources.displayMetrics.heightPixels

fun String.copyToClipboard() {
    val clipboardManager =
        App.instance.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.setPrimaryClip(ClipData.newPlainText("Content", this))
}