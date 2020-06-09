package grade.xyj.com.component.course

import android.os.Bundle
import android.view.View
import androidx.fragment.app.BaseDialogFragment
import grade.xyj.com.R
import grade.xyj.com.classchedule.event.CourseDataChangeEvent
import grade.xyj.com.util.TimeUtils
import grade.xyj.com.util.extend.defaultBus
import grade.xyj.com.util.extend.toastSuccess
import kotlinx.android.synthetic.main.fragment_choose_week.*

class ChooseWeekFragment :BaseDialogFragment(){
    private val currentWeek = TimeUtils.getCurrentWeek()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wp_week.run{
            selectedItemPosition = currentWeek - 1
            data = mutableListOf<String>().apply {
                for (i in 1..20) add("第$i 周")
            }
            setOnItemSelectedListener { _, _, position ->
                tv_title.text = if (position + 1 == currentWeek) "当前第是$currentWeek 周" else "修改当前周为第${position + 1}周"
            }
        }
        btn_cancel.setOnClickListener { dialog?.cancel() }
        btn_save.setOnClickListener {
            val week = wp_week.currentItemPosition + 1
            if (week != currentWeek) {
                TimeUtils.setCurrentWeek(week)
                defaultBus.post(CourseDataChangeEvent)
                context!!.toastSuccess("修改成功")
            }
            dialog?.cancel()
        }
    }

    override val layoutId: Int
        get() = R.layout.fragment_choose_week

    companion object{
        fun newInstance() = ChooseWeekFragment()
    }

}