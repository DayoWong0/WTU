package grade.xyj.com.component.course

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ConvertUtils.dp2px
import grade.xyj.com.R
import grade.xyj.com.bean.CourseAncestor
import grade.xyj.com.classchedule.event.CourseDataChangeEvent
import grade.xyj.com.classchedule.event.CourseSettingChangeEvent
import grade.xyj.com.component.addcourse.AddCourseActivity
import grade.xyj.com.util.Constant
import grade.xyj.com.util.Settings
import grade.xyj.com.util.ShapeUtils
import grade.xyj.com.util.TimeUtils
import grade.xyj.com.util.extend.*
import grade.xyj.com.view.CourseView
import grade.xyj.com.view.LittleCourseView
import grade.xyj.com.view.LittleParse
import grade.xyj.room.entity.CourseEntity
import kotlinx.android.synthetic.main.fragment_course.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.textColorResource

@SuppressLint("SetTextI18n")
class CourseFragment : Fragment() {


    companion object {

        private lateinit var instance: CourseFragment

        fun getInstance(): CourseFragment {
            if (!this::instance.isInitialized) {
                instance = CourseFragment()
            }
            return instance
        }
    }

    lateinit var mContentView: LinearLayout
    lateinit var mTitleLinear: LinearLayout
    lateinit var mTitle: TextView
    lateinit var mGroupName: TextView
    lateinit var courseView: CourseView

    private var offset = 2.5f

    private var mHeightSelectWeek: Int = 0
    private var mSelectWeekIsShow = true


    private lateinit var viewModel: CourseViewModel

    private val WEEK_TEXT_SIZE = 11f
    private val NODE_TEXT_SIZE = 11f
    private val NODE_WIDTH = 28f
    private val mTextViews = arrayOfNulls<TextView>(8)

    private val NIGHT_MODE = Settings.nightMode

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        CourseFragmentUI().createView(AnkoContext.create(context!!, this)).also {
            inflater.inflate(R.layout.fragment_course, mContentView)
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //设置viewmodel
        viewModel = getViewModel()
        initObserver()
        //设置view
        initView()

        viewModel.updateCoursePreference()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defaultBus.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        defaultBus.unregister(this)
    }

    private fun initView() {
        courseView = course_view_v2

        course_changeweeklinear.setOnClickListener {
            ChooseWeekFragment.newInstance().show(childFragmentManager, "chooseWeek")
        }
        initSelectWeekLinear()

        initCourseView()
        initWeekNodeGroup()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onCourseChange(event: CourseDataChangeEvent) {

        viewModel.updateCoursePreference()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onCourseSettingChange(event: CourseSettingChangeEvent) {
        courseView.updateSetting()
        setTimeNodes()
    }

    private fun initObserver() {
        //周变化监听
        viewModel.currentWeek.observe(viewLifecycleOwner, Observer {
            mTitle.text = "第$it 周"
            viewModel.setDays()
            courseView.mCurrentIndex = it
            courseView.resetView()
        })
        //课程变化监听
        viewModel.courses.observe(viewLifecycleOwner, Observer {
            courseView.clear()
            it.forEach { itt -> courseView.addCourse(itt) }
            initSelectWeeks(it)
        })
        //课表名称监听
        viewModel.cgName.observe(viewLifecycleOwner, Observer {
            mGroupName.text = it
        })
        viewModel.days.observe(viewLifecycleOwner, Observer {
            val month = TimeUtils.month
            val day = TimeUtils.day
            for (i in 0..7) {
                if (i == 0) {
                    mTextViews[i]?.text = "${it[i]}\n月"
                } else {
                    mTextViews[i]?.run {
                        text = "${Constant.WEEK[i]}\n${it[i]}日"
                        setBackgroundColor(
                            getColor(
                                if (month == it[0] && day == it[i])
                                    R.color.select_bg
                                else
                                    R.color.un_select_bg
                            )
                        )
                    }
                }
            }
        })
    }

    //标题点击监听
    private fun initSelectWeekLinear() {
        mGroupName.onClick {
            val realWeek = TimeUtils.getCurrentWeek()
            if (mSelectWeekIsShow && viewModel.currentWeek.value != realWeek) {
                linear_select_week.getChildAt(realWeek - 1).callOnClick()
            }
            animSelectWeek(!mSelectWeekIsShow, 300)
        }
        course_week_parent_linear.runNoResult {
            addOnLayoutChangeListener { _, _, top, _, bottom, _, _, _, _ ->
                mHeightSelectWeek = bottom - top
            }
        }
    }

    //顶部选择器
    private fun initSelectWeeks(courses: List<CourseEntity>) {
        if (linear_select_week.childCount == 0) {
            for (i in 1..20) {
                linear_select_week.addView(
                    CourseUtil.getWeekSelectView(activity!!, courses, i).apply {
                        if (viewModel.currentWeek.value == i) {
                            background = ShapeUtils.radius(R.color.week_select_bg, 5,context)
                        }
                        setOnClickListener {
                            context.vibrate(10)
                            //去掉上一个指示器的背景色
                            linear_select_week.getChildAt(viewModel.currentWeek.value!! - 1).background =
                                null
                            //添加背景色
                            background = ShapeUtils.radius(R.color.week_select_bg, 5,context)
                            //设置当前周
                            viewModel.currentWeek.value = tag as Int
                        }
                    })
            }
        } else {
            //指示器已经添加了
            for (i in 0..19) {
                //设置背景
                val linear = linear_select_week.getChildAt(i) as LinearLayout
                linear.background = null
                //设置指示器
                (linear.getChildAt(1) as LittleCourseView).setParse(LittleParse(courses, i + 1))
            }
            //设置背景
            linear_select_week.getChildAt(viewModel.currentWeek.value!! - 1).background =
                ShapeUtils.radius(R.color.week_select_bg, 5,requireContext())
        }
        scroll()
    }

    private fun initCourseView() {

        courseView.run {
            setOnTouchListener { _, _ -> false }
            setOnItemClickListener(object : CourseView.OnItemClickListener() {
                override fun onLongClick(courses: List<CourseAncestor>, itemView: View) = Unit

                override fun onClick(courses: List<CourseAncestor>, itemView: View) {
                    ShowDetailDialog()
                        .show(this@CourseFragment, courses[0] as CourseEntity)
                }

                override fun onAdd(course: CourseAncestor, addView: View) =
                    startActivity<AddCourseActivity>("key" to "")
            })
        }
    }

    private fun initWeekNodeGroup() {

        for (i in -1..6) {
            val textView = TextView(context).apply {
                gravity = Gravity.CENTER
                width = 0
                textColorResource = R.color.dark_text_color
            }

            val params: LinearLayout.LayoutParams

            if (i == -1) {
                params = LinearLayout.LayoutParams(
                    dp2px(NODE_WIDTH),
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                textView.textSize = NODE_TEXT_SIZE
            } else {
                params = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
                    .apply { weight = 1f }
                textView.textSize = WEEK_TEXT_SIZE
            }
            mTextViews[i + 1] = textView
            layout_week_group.addView(textView, params)
        }
        setTimeNodes()
    }

    //侧边栏
    private fun setTimeNodes() {
        layout_node_group.removeAllViews()
        val nodeItemHeight = dp2px(Settings.courseHeight.toFloat())
        for (i in 1..12) {
            layout_node_group.addView(
                TextView(context).apply {
                    textSize = NODE_TEXT_SIZE
                    gravity = Gravity.CENTER
                    textColorResource = R.color.dark_text_color
                    text = i.toString()
                },
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, nodeItemHeight
                )
            )
        }

    }


    private fun animSelectWeek(show: Boolean, dur: Long) {
        mSelectWeekIsShow = show

        var start = 0
        var end = 0
        if (show) {
            start = -mHeightSelectWeek
        } else {
            end = -mHeightSelectWeek
        }
        ValueAnimator.ofInt(start, end).run {
            duration = dur
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                course_week_parent_linear.run {
                    layoutParams = (layoutParams as LinearLayout.LayoutParams).apply {
                        topMargin = animation.animatedValue as Int
                    }
                }
            }
            start()
        }

        if (show) scroll()
    }

    private fun scroll() = delayed(300) {
        val px =
            ((viewModel.currentWeek.value!! - offset) * linear_select_week.getChildAt(0).width).toInt()
        course_scrollview.smoothScrollTo(px, 0)
    }
}