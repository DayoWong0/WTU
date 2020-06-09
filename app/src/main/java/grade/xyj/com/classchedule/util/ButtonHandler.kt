package grade.xyj.com.classchedule.util

import android.content.DialogInterface
import android.os.Handler
import android.os.Message

import java.lang.ref.WeakReference

/**
 * 点击确定dialog不消失
 */
class ButtonHandler(dialog: DialogInterface) : Handler() {

    private val mDialog: WeakReference<DialogInterface> = WeakReference(dialog)

    override fun handleMessage(msg: Message) {
        when (msg.what) {

            DialogInterface.BUTTON_POSITIVE, DialogInterface.BUTTON_NEGATIVE, DialogInterface.BUTTON_NEUTRAL -> (msg.obj as DialogInterface.OnClickListener).onClick(mDialog
                    .get(), msg.what)
        }
    }
}