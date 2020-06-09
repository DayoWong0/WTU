package grade.xyj.com.component.addcourse

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.BaseDialogFragment
import androidx.lifecycle.ViewModelProviders
import grade.xyj.com.R
import kotlinx.android.synthetic.main.dialog_select_time.btn_cancel
import kotlinx.android.synthetic.main.fragment_select_time.*

class SelectTimeFragment : BaseDialogFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_select_time

    var position = -1
    private val dayList = listOf("周一", "周二", "周三", "周四", "周五", "周六", "周日")
    private val nodeList = arrayListOf<String>()
    private lateinit var viewModel: AddCourseViewModel
    var day = 1
    var start = 1
    var end = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt("position")
        }
        viewModel = ViewModelProviders.of(activity!!).get(AddCourseViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for (i in 1..12) {
            nodeList.add("第 $i 节")
        }
        wp_day.data = dayList
        wp_start.data = nodeList
        wp_end.data = nodeList

        val course = viewModel.courses[position]
        day = course.week!!
        start = if (course.startNode!! > 12) 12 else course.startNode!!
        end = (course.startNode!! + course.startNode!! - 1).run { if (this > 12) 12 else this }
        initEvent()
    }


    private fun initEvent() {
        wp_day.selectedItemPosition = day - 1
        wp_start.selectedItemPosition = start - 1
        wp_end.selectedItemPosition = end - 1

        wp_day.setOnItemSelectedListener { _, _, position ->
            day = position + 1
        }
        wp_start.setOnItemSelectedListener { _, _, position ->
            start = position + 1
            if (end < start) {
                wp_end.selectedItemPosition = start - 1
                end = start
            }
        }
        wp_end.setOnItemSelectedListener { _, _, position ->
            end = position + 1
            if (end < start) {
                wp_end.selectedItemPosition = start - 1
                end = start
            }
        }

        btn_cancel.setOnClickListener {
            dismiss()
        }

        btn_save.setOnClickListener {
            if (activity is OnTimeChangedListener)
                (activity as OnTimeChangedListener).onChanged(day, start, end - start + 1, position)
            dismiss()
        }
    }

    interface OnTimeChangedListener {
        fun onChanged(day: Int, start: Int, count: Int, pos: Int)
    }

    companion object {
        @JvmStatic
        fun newInstance(arg: Int) =
                SelectTimeFragment().apply {
                    arguments = bundleOf("position" to arg)
                }
    }
}
