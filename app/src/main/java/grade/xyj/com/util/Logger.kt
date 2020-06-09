package grade.xyj.com.util

import android.util.Log

object Logger {

    fun e(msg: String) {
        Log.e("WTU ERROR", msg)
    }
    fun i(msg: String) {
        Log.i("WTU INFO", msg)
    }
}