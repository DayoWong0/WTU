package grade.xyj.com.component.news

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import grade.xyj.com.R
import grade.xyj.com.base.BaseFragment
import grade.xyj.com.component.WebActivity
import grade.xyj.com.util.extend.toastError
import kotlinx.android.synthetic.main.fragemnt_news_child.*
import org.jetbrains.anko.support.v4.startActivity

class NewsChildFragment : BaseFragment() {
    override fun bindLayout(): Int = R.layout.fragemnt_news_child

    companion object {
        fun newInstance(id: String, type: Int) = NewsChildFragment().apply {
            arguments = bundleOf("id" to id, "type" to type)
        }
    }

    var type = 0
    private lateinit var idd: String
    private lateinit var viewModel: NewChildViewModel
    private lateinit var adapter: NewsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments!!.run {
            idd = getString("id")!!
            type = getInt("type")
        }

        viewModel = ViewModelProviders.of(parentFragment!!).get(NewChildViewModel::class.java)
        viewModel.getLiveData(idd).observe(viewLifecycleOwner, Observer {
            smart_view.finishRefresh()
            adapter.run {
                data.clear()
                if (it != null)
                    data.addAll(it)
                else
                    toastError("获取数据失败")
                notifyDataSetChanged()
            }
        })


        adapter = NewsAdapter(viewModel.getItems(idd)).apply {
            setOnItemClickListener { _, _, i ->
                val url = "http://ehall.wtu.edu.cn/new/detail-word.html?type=$type?bulletinId=${adapter.data[i].WID}"
                startActivity<WebActivity>("title" to "校内新闻", "url" to url)
            }
        }
        recycler.run {
            layoutManager = LinearLayoutManager(this@NewsChildFragment.context)
            adapter = this@NewsChildFragment.adapter
        }

        smart_view.run {
            setOnRefreshListener {
                viewModel.getNews(idd)
            }
            setRefreshHeader(ClassicsHeader(context))
        }
    }

    override fun fetchData() {
        smart_view.autoRefresh()
    }
}
