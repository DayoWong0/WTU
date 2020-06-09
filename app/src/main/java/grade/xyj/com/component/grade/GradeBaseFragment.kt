package grade.xyj.com.component.grade

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.entity.MultiItemEntity
import grade.xyj.com.base.NewBaseFragment
import grade.xyj.com.util.extend.getViewModel
import grade.xyj.com.util.extend.toastError
import grade.xyj.com.util.extend.toastInfo
import grade.xyj.com.util.extend.toastSuccess

class GradeBaseFragment : NewBaseFragment() {

    companion object {

        private lateinit var instance: GradeBaseFragment

        fun getInstance(): GradeBaseFragment {
            if (!this::instance.isInitialized) {
                instance = GradeBaseFragment()
            }
            return instance
        }
    }

    override fun setTitle(): String = "成绩查询"

    lateinit var xqSpinner: Spinner
    lateinit var xnSpinner: Spinner
    lateinit var recyclerView: RecyclerView
    lateinit var queryButton: Button
    var dialog: ProgressDialog? = null

    lateinit var viewModel: GradeViewModel
    lateinit var adapter: GradeAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = getViewModel()

        viewModel.nGrades.observe(viewLifecycleOwner, Observer {
            dialog?.cancel()
            adapter.data.clear()
            when {
                it == null -> toastError("网络错误,请重试.")
                it.isEmpty() -> toastInfo("没有获取到成绩")
                else -> {
                    adapter.data.addAll(it)
                    toastSuccess("获取到${it.size}条成绩")
                }
            }
            adapter.notifyDataSetChanged()
            queryButton.isEnabled = true
        })

        initView()
    }

    private fun initView() {
        mContentView.addView(GradeUI.getView(this))

        adapter = GradeAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as MultiItemEntity
                if (item.itemType == 0) {
                    if ((item as Level0Item).isExpanded)
                        adapter.collapse(position)
                    else
                        adapter.expand(position)
                }
            }
        }
        recyclerView.adapter = adapter
    }

}