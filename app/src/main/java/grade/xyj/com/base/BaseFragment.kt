package grade.xyj.com.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xyj.xnative.NativeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment: Fragment(),CoroutineScope{

    private lateinit var job: Job

    var isViewInitiated: Boolean = false
    var isDataInitiated: Boolean = false

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isViewInitiated = true
        prepareFetchData(false)
    }

    open fun fetchData(){}

    private fun prepareFetchData(forceUpdate: Boolean) {
        if (userVisibleHint && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            fetchData()
            isDataInitiated = true
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        prepareFetchData(false)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(bindLayout(),null)

    abstract fun bindLayout():Int

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }
}