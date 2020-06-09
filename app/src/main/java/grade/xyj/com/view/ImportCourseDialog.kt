package grade.xyj.com.view

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import com.aigestudio.wheelpicker.WheelPicker
import grade.xyj.com.R
import grade.xyj.com.component.course.CourseUtil
import grade.xyj.com.util.TimeUtils
import org.jetbrains.anko.alert

class ImportCourseDialog(private val activity: FragmentActivity) {

    private val ys = TimeUtils.getCurrentSchoolYearAndSemester()
    private val year = ys[0]
    private val xq = ys[1]
    private val wheelView1: WheelPicker
    private val wheelView2: WheelPicker
    private val dialog1: DialogInterface

    private var yearPos = 0
    private var xqPos = xq - 1

    init {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_select_time, null)
        wheelView1 = view.findViewById(R.id.wp_xn)
        wheelView2 = view.findViewById(R.id.wp_xq)
        initWheelView()

        dialog1 = activity.alert {
            customView = view
        }.show()

        view.findViewById<View>(R.id.btn_cancel).setOnClickListener {
            dialog1.cancel()
        }


        view.findViewById<Button>(R.id.btn_sure).setOnClickListener {
            dialog1.cancel()
            doImport()
        }
    }

    private fun initWheelView() {
        //view1
        val arrays = mutableListOf<String>()
        for (i in 0..3) {
            arrays.add("${year - i}-${year + 1 - i}")
        }
        wheelView1.run {
            data = arrays
            selectedItemPosition = yearPos
            setOnItemSelectedListener { _, _, position ->
                yearPos = position
            }
        }
        wheelView2.run {
            data = arrayListOf("1", "2")
            selectedItemPosition = xqPos
            setOnItemSelectedListener { _, _, position ->
                xqPos = position
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun doImport() {

        fun getParm(xq: Int) = if (xq == 1) "3" else "12"

        val y = year - yearPos
        val x = xqPos + 1

        CourseUtil.doImportCourse(activity, y.toString(), getParm(x))
    }
}