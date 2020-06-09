package grade.xyj.com.component.addcourse

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.BaseDialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.noober.background.drawable.DrawableCreator
import grade.xyj.com.R
import grade.xyj.com.util.extend.getColor
import grade.xyj.com.util.extend.toastError
import kotlinx.android.synthetic.main.fragment_select_week.*
import org.jetbrains.anko.support.v4.dip

class SelectWeekFragment : BaseDialogFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_select_week

    var position = -1
    private lateinit var viewModel: AddCourseViewModel
    private val liveData = MutableLiveData<MutableList<Int>>()
    private val result = mutableListOf<Int>()
    private val maxWeek = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt("position")
        }
        viewModel = ViewModelProviders.of(activity!!).get(AddCourseViewModel::class.java)
        liveData.observe(this, Observer {
            if (it?.size == maxWeek) {
                tv_all.setTextColor(Color.WHITE)
                tv_all.background = DrawableCreator.Builder()
                        .setShape(DrawableCreator.Shape.Rectangle)
                        .setCornersRadius(dip(32).toFloat())
                        .setSolidColor(getColor(R.color.red))
                        .setSizeWidth(dip(100).toFloat())
                        .setSizeHeight(dip(64).toFloat())
                        .build()
            } else {
                tv_all.setTextColor(Color.BLACK)
                tv_all.background = null
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        liveData.value = viewModel.courses[position].showIndexes
        result.addAll(liveData.value!!)
        showWeeks()
        initEvent()
    }

    private fun showWeeks() {
        ll_week.removeAllViews()
        val context = ll_week.context
        val margin = dip(4)
        val textViewSize = dip(32)
        val llHeight = dip(40)
        for (i in 0 until Math.ceil(maxWeek / 6.0).toInt()) {
            val linearLayout = LinearLayout(context)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            ll_week.addView(linearLayout)
            val params = linearLayout.layoutParams
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = llHeight
            linearLayout.layoutParams = params

            for (j in 0..5) {
                val week = i * 6 + j + 1
                if (week > maxWeek) {
                    break
                }
                val textView = TextView(context)
                val textParams = LinearLayout.LayoutParams(textViewSize, textViewSize)
                textParams.setMargins(margin, margin, margin, margin)
                textView.layoutParams = textParams
                textView.text = "$week"
                textView.gravity = Gravity.CENTER
                if (week in result) {
                    textView.setTextColor(Color.WHITE)
                    textView.background = DrawableCreator.Builder()
                            .setShape(DrawableCreator.Shape.Oval)
                            .setSolidColor(getColor(R.color.colorAccent))
                            .build()
                } else {
                    textView.setTextColor(Color.BLACK)
                    textView.background = null
                }

                textView.setOnClickListener {
                    if (textView.background == null) {
                        result.add(week)
                        liveData.value = result
                        textView.setTextColor(Color.WHITE)
                        textView.background = DrawableCreator.Builder()
                                .setShape(DrawableCreator.Shape.Oval)
                                .setSolidColor(getColor(R.color.colorAccent))
                                .build()
                    } else {
                        result.remove(week)
                        liveData.value = result
                        textView.setTextColor(Color.BLACK)
                        textView.background = null
                    }
                }
                textView.setLines(1)
                linearLayout.addView(textView)
            }
        }
    }

    private fun initEvent() {
        tv_all.setOnClickListener {
            if (tv_all.background == null) {
                result.clear()
                for (i in 1..maxWeek) {
                    result.add(i)
                }
                showWeeks()
                liveData.value = result
            } else {
                result.clear()
                showWeeks()
                liveData.value = result
            }
        }

        tv_fx.setOnClickListener {
            val list = mutableListOf<Int>()
            for (i in 1..maxWeek) if (!result.contains(i)) list.add(i)
            result.run {
                clear()
                addAll(list)
            }
            showWeeks()
            liveData.value = result
        }

        btn_cancel.setOnClickListener {
            dismiss()
        }

        btn_save.setOnClickListener {
            if (result.size == 0) {
                context!!.toastError("请至少选择一周")
            } else {
                if (activity is OnWeekChangedListener)
                    (activity as OnWeekChangedListener).onChanged(result, position)
                dismiss()
            }
        }
    }

    interface OnWeekChangedListener {
        fun onChanged(array: List<Int>, pos: Int)
    }

    companion object {
        @JvmStatic
        fun newInstance(arg: Int) = SelectWeekFragment().apply {
            arguments = bundleOf("position" to arg)
        }
    }
}
