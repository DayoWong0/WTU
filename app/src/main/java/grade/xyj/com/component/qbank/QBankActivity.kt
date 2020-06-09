package grade.xyj.com.component.qbank

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import grade.xyj.com.util.Settings
import grade.xyj.com.util.extend.getViewModel
import grade.xyj.com.util.extend.setWindow
import grade.xyj.com.util.extend.showProgressDialog
import grade.xyj.com.util.extend.toastError
import grade.xyj.room.entity.QuestionEntity
import org.jetbrains.anko.alert
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.yesButton

class WorkActivity : AppCompatActivity() {

    lateinit var viewModel: QbankViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: QBankAdapter

    private var times = 0
    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setWindow()
        super.onCreate(savedInstanceState)
        initViewModel()
        viewModel.initFirstUse()
        QBankActivityUI().setContentView(this)
    }

    private fun initViewModel() {
        viewModel = getViewModel()
        viewModel.showDialog.observe(this, Observer {
            when{
                ++times == 1-> dialog = showProgressDialog("初次使用,初始化数据库...")
                it->{
                    Settings.firstUseBank = false
                    viewModel.isInit = true
                    dialog?.cancel()
                }
                else->{
                    dialog?.cancel()
                    toastError("初始化数据库失败.")
                    finish()
                }
            }
        })
        viewModel.qBanks.observe(this, Observer {
            adapter.run {
                data.clear()
                data.addAll(it)
                notifyDataSetChanged()
            }
        })
    }

    fun showQbankDialog(item:QuestionEntity){
        item.RES.split("")
        alert {
            message = """
                ${item.TOP}

                ${item.OPTIONS}

                答案:${item.RES}
            """.trimIndent()

            yesButton {}
        }.show()
    }
}