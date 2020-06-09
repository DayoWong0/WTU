package grade.xyj.com.component.addcourse

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import grade.xyj.com.R
import grade.xyj.com.base.BaseTitleActivity
import grade.xyj.com.classchedule.event.CourseDataChangeEvent
import grade.xyj.com.util.extend.defaultBus
import grade.xyj.com.util.extend.getViewModel
import grade.xyj.com.util.extend.toastError
import grade.xyj.com.util.extend.toastInfo
import grade.xyj.com.view.colorpicker.ColorPickerFragment
import grade.xyj.room.entity.CourseEntity
import org.jetbrains.anko.UI
import org.jetbrains.anko.find
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.textColor
import org.jetbrains.anko.textColorResource

class AddCourseActivity : BaseTitleActivity(), ColorPickerFragment.ColorPickerDialogListener, SelectTimeFragment.OnTimeChangedListener, SelectWeekFragment.OnWeekChangedListener {
    override fun onChanged(array: List<Int>, pos: Int) {
        
        viewModel.courses[pos].run {
            allWeek = CourseEntity.list2WeekString(array)
            init()
        }
        adapter.notifyDataSetChanged()
    }

    override fun onChanged(day: Int, start: Int, count: Int, pos: Int) {
        viewModel.courses[pos].run {
            week = day
            startNode = start
            nodeCount = count
        }
        adapter.notifyDataSetChanged()
    }

    private lateinit var viewModel: AddCourseViewModel
    private lateinit var adapter: AddCourseAdapter
    private lateinit var tv_color: TextView
    private lateinit var ic_color: TextView

    override val title: String
        get() =  "添加课程"

    override fun onSetupSubButton(tvButton: TextView): TextView? = tvButton.apply {
        typeface = Typeface.DEFAULT_BOLD
        text = "保存"
        textColorResource = R.color.red
        setOnClickListener {
            if (viewModel.key.isEmpty()) {
                toastError("课名不能为空")
                return@setOnClickListener
            }


            viewModel.save()
            defaultBus.post(CourseDataChangeEvent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val key = intent.getStringExtra("key")!!

        viewModel = getViewModel()
        viewModel.init(key)
        initAdapter()
        mContentView.addView(UI {
            recyclerView {
                layoutManager = LinearLayoutManager(this@AddCourseActivity)
                adapter = this@AddCourseActivity.adapter
            }
        }.view)

    }

    private fun initAdapter() {
        adapter = AddCourseAdapter(viewModel.courses, mFont)
                .apply {
                    addHeaderView(getHeaderView())
                    addFooterView(getFooterView())
                    setOnItemChildClickListener { adapter, view, position ->
                        when (view.id) {
                            R.id.ib_delete -> {
                                if (viewModel.courses.size <= 1) {
                                    toastInfo("必须保留一个标签哦")
                                } else {
                                    viewModel.courses.removeAt(position)
                                    adapter.notifyDataSetChanged()
                                }
                            }
                            R.id.ll_time -> {
                                SelectTimeFragment.newInstance(position).run {
                                    isCancelable = false
                                    show(supportFragmentManager, "selectWeek")
                                }
                            }
                            R.id.ll_weeks -> {
                                SelectWeekFragment.newInstance(position).run {
                                    isCancelable = false
                                    show(supportFragmentManager, "selectWeek")
                                }

                            }
                        }
                    }
                }
    }

    //底部标签
    private fun getFooterView(): View = layoutInflater.inflate(R.layout.item_add_course_btn, null).apply {
        findViewById<TextView>(R.id.tv_add).setOnClickListener {
            viewModel.courses.add(CourseEntity.DELFAULT_COUSE.apply {
                teacher = viewModel.courses[0].teacher
                groupId = viewModel.cgId
            })
            adapter.notifyDataSetChanged()
        }
    }

    private fun getHeaderView(): View = layoutInflater.inflate(R.layout.item_add_course_base, null).apply {
        intArrayOf(R.id.ic_1, R.id.ic_2).forEach { find<TextView>(it).typeface = mFont }
        //课名编辑器
        find<EditText>(R.id.et_name).run {
            val couName = viewModel.key
            setText(couName)
            setSelection(couName.length)
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
                override fun afterTextChanged(s: Editable) {
                    viewModel.courseName = s.toString()
                }
            })
        }
        //颜色选取
        find<LinearLayout>(R.id.ll_color).setOnClickListener {
            ColorPickerFragment.newBuilder()
                    .setColor(viewModel.color)
                    .setShowAlphaSlider(false)
                    .show(this@AddCourseActivity)
        }
        //设置颜色
        tv_color = findViewById<TextView>(R.id.tv_color).apply {
            text = "点此更改颜色"
            textColor = viewModel.color
        }
        ic_color = findViewById<TextView>(R.id.ic_2).apply { textColor = viewModel.color }
    }


    //颜色选取回调
    override fun onColorSelected(dialogId: Int, color: Int) {
        viewModel.color = color
        tv_color.textColor = color
        ic_color.textColor = color
    }
}