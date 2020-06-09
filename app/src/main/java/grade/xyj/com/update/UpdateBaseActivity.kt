package grade.xyj.com.update

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.checkbox.checkBoxPrompt
import com.jaeger.library.StatusBarUtil
import grade.xyj.com.R
import grade.xyj.com.util.Preferences
import grade.xyj.com.util.extend.copyToClipboard
import grade.xyj.com.util.extend.defaultBus
import grade.xyj.com.util.extend.getColorByCompat
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.browse
import org.jetbrains.anko.startService
import org.jetbrains.anko.toast

abstract class UpdateBaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defaultBus.register(this)
        StatusBarUtil.setColor(this, getColorByCompat(R.color.light_text_color), 0)
        startService<UpdateService>()
    }

    override fun onDestroy() {
        super.onDestroy()
        defaultBus.unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdate(updateInfo: UpdateInfo) {
        val promptKey = "no_prompt_${updateInfo.version}"
        val prompt = updateInfo.force || !Preferences.getBoolean(promptKey, false)
        if (prompt) {
            MaterialDialog(this).show {
                title(text = "发现新版本 v${updateInfo.version}")
                message(text = updateInfo.info)
                if (updateInfo.force) {
                    cancelOnTouchOutside(false)
                    cancelable(false)
                } else {
                    checkBoxPrompt(text = "忽略本次升级", isCheckedDefault = false) {
                        Preferences.putBoolean(promptKey, it)
                    }
                    negativeButton {}
                }
                positiveButton {
                    val url = "https://www.coolapk.com/apk/grade.xyj.com"
                    if (!browse(url)) {
                        url.copyToClipboard()
                        toast("打开失败，已将地址复制到剪切板.")
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCaptcha(){

    }
}