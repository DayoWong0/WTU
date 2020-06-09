package grade.xyj.com.component.course.setting

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import grade.xyj.com.base.BaseTitleActivity
import grade.xyj.com.classchedule.event.CourseSettingChangeEvent
import grade.xyj.com.component.groupmg.ScheduleManageActivity
import grade.xyj.com.util.Settings
import grade.xyj.com.util.TimeUtils
import grade.xyj.com.util.extend.defaultBus
import grade.xyj.com.util.extend.getViewModel
import grade.xyj.com.util.extend.toastInfo
import grade.xyj.com.view.ImportCourseDialog
import grade.xyj.com.view.colorpicker.ColorPickerFragment
import grade.xyj.com.view.setting.bean.*
import grade.xyj.com.view.setting.binder.*
import grade.xyj.com.widget.AppWidgetUtil
import me.drakeet.multitype.MultiTypeAdapter
import org.jetbrains.anko.UI
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.startActivity


class ScheduleSettingsActivity : BaseTitleActivity(){

    private val COURSE_TEXT_COLOR = 2
    private val STROKE_COLOR = 3
    private val WIDGET_TITLE_COLOR = 4
    private val WIDGET_COURSE_TEXT_COLOR = 5
    private val WIDGET_STROKE_COLOR = 6
    private val WIDGET_TODAY_TITLE_COLOR = 7
    private val WIDGET_TODAY_COURSE_TEXT_COLOR = 8


    override val title: String
        get() =  "课表设置"

    private lateinit var mRecyclerView: RecyclerView

    private var isChange = false

    private val mAdapter: MultiTypeAdapter = MultiTypeAdapter()
    private val items = mutableListOf<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //监听颜色
        getViewModel<ColorPickViewModel>().result.observe(this, Observer {
            val color = it.second
            isChange = true
            when (it.first) {
                COURSE_TEXT_COLOR -> Settings.courseTextColor = color
                WIDGET_COURSE_TEXT_COLOR -> Settings.widgetTextColor = color
                WIDGET_TITLE_COLOR -> Settings.widgetTitleTextColor = color
                WIDGET_STROKE_COLOR -> Settings.widgetLineColor = color
                STROKE_COLOR -> Settings.courseLineColor = color
                WIDGET_TODAY_TITLE_COLOR->Settings.widgetTodayTitleTextColor = color
                WIDGET_TODAY_COURSE_TEXT_COLOR -> Settings.widgetTodayTextColor = color
            }
        })
        createAdapter()
        createItem()

        mAdapter.items = items
        mRecyclerView = UI {
            recyclerView {
                layoutManager = LinearLayoutManager(this@ScheduleSettingsActivity)
                adapter = mAdapter
            }
        }.view as RecyclerView
        mContentView.addView(mRecyclerView)
    }

    private fun createAdapter() {
        mAdapter.register(CategoryItem::class, CategoryItemViewBinder())
        mAdapter.register(SeekBarItem::class, SeekBarItemViewBinder { item, value -> onSeekBarValueChange(item, value) })
        mAdapter.register(HorizontalItem::class, HorizontalItemViewBinder { onHorizontalItemClick(it) })
        mAdapter.register(VerticalItem::class, VerticalItemViewBinder(
                { onVerticalItemClick(it) },
                { true }
        ))
        mAdapter.register(SwitchItem::class, SwitchItemViewBinder { item, isCheck -> onSwitchItemCheckChange(item, isCheck) })
    }

    private fun createItem() {
        items.add(CategoryItem("课程数据", false))
        items.add(HorizontalItem("当前周", "第${TimeUtils.getCurrentWeek()}周"))
        items.add(HorizontalItem("课表管理", "多课表管理"))
        items.add(HorizontalItem("课表导入", "一键导入你的课表~~"))

        items.add(CategoryItem("课表外观", false))
        items.add(VerticalItem("课程文字颜色", "指课程格子内文字的颜色"))
        items.add(VerticalItem("课程边框颜色", "指课程格子边框的颜色"))
        items.add(SeekBarItem("课程边框宽度", Settings.courseLineWidth, 0, 5, "dp"))
        items.add(SeekBarItem("课程格子透明度", Settings.courseAlpha, 0, 100, "%"))
        items.add(SeekBarItem("课程格子高度", Settings.courseHeight, 32, 96, "dp"))
        items.add(SeekBarItem("课程显示文字大小", Settings.courseTextSize, 8, 16, "sp"))
        items.add(SwitchItem("显示非本周课程", Settings.showOtherWeek))
        items.add(SwitchItem("显示垂直分割线", Settings.showVerticalLine))
        items.add(SwitchItem("显示水平分割线", Settings.showHorizontalLine))

        items.add(CategoryItem("全周小部件外观", false))
        items.add(SeekBarItem("小部件格子高度", Settings.widgetHeight, 20, 70, "dp"))
        items.add(SeekBarItem("小部件显示文字大小", Settings.widgetTextSize, 8, 16, "sp"))
        items.add(SeekBarItem("小部件不透明度", Settings.widgetAlpha, 0, 100, "%"))
        items.add(SeekBarItem("小部件内边距", Settings.widgetPadding, 0, 5, "dp"))
        items.add(SeekBarItem("小部件边框宽度", Settings.widgetLineWidth, 0, 5, "dp"))
        items.add(VerticalItem("小部件边框颜色", "指课程格子边框的颜色"))
        items.add(VerticalItem("小部件标题颜色", "指标题等字体的颜色"))
        items.add(VerticalItem("小部件课程文字颜色", "指课程格子内的文字颜色"))

        items.add(CategoryItem("当日小部件外观", false))
        items.add(SeekBarItem("小部件不透明度 ", Settings.widgetTodayAlpha, 0, 100, "%"))
        items.add(VerticalItem("小部件标题颜色 ", "指标题等字体的颜色"))
        items.add(VerticalItem("小部件课程文字颜色 ", "指课程格子内的文字颜色"))
    }

    //选择框回调
    private fun onSwitchItemCheckChange(item: SwitchItem, isChecked: Boolean) {
        isChange = true
        when (item.title) {
            "显示非本周课程" -> Settings.showOtherWeek = isChecked
            "显示垂直分割线" -> Settings.showVerticalLine = isChecked
            "显示水平分割线" -> Settings.showHorizontalLine = isChecked
        }
        item.checked = isChecked
    }

    //滑动回调
    private fun onSeekBarValueChange(item: SeekBarItem, value: Int) {
        isChange = true
        val i = value + item.min
        when (item.title) {
            "课程格子高度" -> Settings.courseHeight = i
            "课程显示文字大小" -> Settings.courseTextSize = i
            "课程边框宽度" -> Settings.courseLineWidth = i
            "课程格子透明度" -> Settings.courseAlpha = i
            "小部件格子高度" -> Settings.widgetHeight = i
            "小部件显示文字大小" -> Settings.widgetTextSize = i
            "小部件不透明度" -> Settings.widgetAlpha = i
            "小部件不透明度 " -> Settings.widgetTodayAlpha = i
            "小部件内边距" -> Settings.widgetPadding = i
            "小部件边框宽度" -> Settings.widgetLineWidth = i
        }
        item.valueInt = i
    }

    private fun onHorizontalItemClick(item: HorizontalItem) {
        when (item.title) {
            "当前周" -> toastInfo("请在课表页面设置")
            "课表管理" -> startActivity<ScheduleManageActivity>()
            "课表导入" -> ImportCourseDialog(this)
        }
    }

    private fun onVerticalItemClick(item: VerticalItem) {
        when (item.title) {
            "课程文字颜色" -> buildColorPickerDialogBuilder(Settings.courseTextColor, COURSE_TEXT_COLOR)
            "课程边框颜色" -> buildColorPickerDialogBuilder(Settings.courseLineColor, STROKE_COLOR)
            "小部件课程文字颜色" -> buildColorPickerDialogBuilder(Settings.widgetTextColor, WIDGET_COURSE_TEXT_COLOR)
            "小部件标题颜色" -> buildColorPickerDialogBuilder(Settings.widgetTitleTextColor, WIDGET_TITLE_COLOR)
            "小部件边框颜色" -> buildColorPickerDialogBuilder(Settings.widgetLineColor, WIDGET_STROKE_COLOR)
            "小部件课程文字颜色 " -> buildColorPickerDialogBuilder(Settings.widgetTodayTextColor, WIDGET_TODAY_COURSE_TEXT_COLOR)
            "小部件标题颜色 " -> buildColorPickerDialogBuilder(Settings.widgetTodayTitleTextColor, WIDGET_TODAY_TITLE_COLOR)
        }
    }

    override fun onPause() {
        if(isChange){
            AppWidgetUtil.updateWidget(this)
            defaultBus.post(CourseSettingChangeEvent)
            isChange = false
        }
        super.onPause()
    }

    private fun buildColorPickerDialogBuilder(color: Int, id: Int) {
        ColorPickerFragment.newBuilder()
                .setShowAlphaSlider(true)
                .setColor(color)
                .setDialogId(id)
                .show(this)
    }
}