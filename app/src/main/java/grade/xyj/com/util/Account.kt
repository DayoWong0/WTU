package grade.xyj.com.util

import android.content.Context
import com.blankj.utilcode.util.SPUtils
import com.xyj.xnative.NativeUtils

object Account {

    private val sp get() = SPUtils.getInstance(Context.MODE_PRIVATE)

    var useWeb: Boolean
        get() {
            return sp.getBoolean("useWeb", false)
        }
        set(value) {
            sp.put("useWeb", value)
        }

    var cno: String
        get() {
            return sp.getString("cno", "")
        }
        set(value) {
            sp.put("cno", value)
        }

    var passWord: String
        get() {
            var pawd = sp.getString("password", "")
            if (pawd.isNotBlank()) {
                pawd = NativeUtils.decode(pawd)
            }
            return pawd
        }
        set(value) {
            sp.put("password", NativeUtils.encode(value))
        }

    var name: String
        get() {
            return sp.getString("name", "")
        }
        set(value) {
            sp.put("name", value)
        }
    var department: String?
        get() {
            return sp.getString("department", "")
        }
        set(value) {
            sp.put("department", value)
        }

    fun clear() {
        cno = ""
        passWord = ""
        name = ""
        department = ""
        useWeb = false
    }

    fun setAll(cno: String, pawd: String, name: String, department: String) {
        Account.cno = cno
        Account.name = name
        Account.passWord = pawd
        Account.department = department
    }

}