package grade.xyj.com.component.groupmg

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.EditText
import androidx.fragment.app.BaseDialogFragment
import grade.xyj.com.R
import kotlinx.android.synthetic.main.fragment_modify_table_name.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class ModifyTableNameFragment : BaseDialogFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_modify_table_name

    private var listener: TableNameChangeListener? = null
    private var tableName = ""
    private var title = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            listener = it.getParcelable("listener")
            tableName = it.getString("tableName")!!
            title = it.getString("title")!!
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_title.text = title
        et_table_name.setText(tableName)
        et_table_name.setSelection(tableName.length)
        initEvent()
    }

    private fun initEvent() {
        tv_save.onClick {
            listener?.onFinish(et_table_name, dialog!!)
        }

        tv_cancel.onClick {
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface TableNameChangeListener : Parcelable {
        fun onFinish(editText: EditText, dialog: Dialog)
    }

    companion object {
        fun newInstance(changeListener: TableNameChangeListener, string: String = "", titleStr: String = "课表名字") =
                ModifyTableNameFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable("listener", changeListener)
                        putString("tableName", string)
                        putString("title", titleStr)
                    }
                }
    }
}
