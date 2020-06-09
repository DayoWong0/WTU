package grade.xyj.com.util

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatDelegate
import grade.xyj.com.App
import grade.xyj.com.R

object Settings {
    lateinit var sp: SharedPreferences

    private const val KEY_NIGHT_MODEL = "key_is_night_model"

    private const val COURSE_HEIGHT = "course_height"
    private const val COURSE_TEXTSIZE = "course_textsize"
    private const val COURSE_TEXTCOLOR = "course_textcolor"
    private const val COURSE_SHOWOTHERWEEK = "course_showoutherweek"
    private const val COURSE_SHOWVERTICALLINE = "course_showverticalline"
    private const val COURSE_SHOWHORIZONTALLINE = "course_showhorizontalline"
    private const val COURSE_ALPHA = "course_alpha"
    private const val COURSE_LINE_WIDTH = "course_line_width"
    private const val COURSE_LINE_COLOR = "course_line_color"

    private const val WIDGET_HEIGHT = "weight_height"
    private const val WIDGET_TEXTSIZE = "weight_textsize"
    private const val WIDGET_TEXTCOLOR = "weight_textcolor"
    private const val WIDGET_PADDING = "weight_padding"
    private const val WIDGET_ALPHA = "weight_alpha"
    private const val WIDGET_LINE_COLOR = "weight_line_color"
    private const val WIDGET_LINE_WIDTH = "weight_line_width"
    private const val WIDGET_TITLE_TEXTCOLOR = "weight_title_textcolor"


    private const val WIDGET_TODAY_ALPHA = "weight_today_alpha"
    private const val WIDGET_TODAY_TITLE_TEXTCOLOR = "weight_today_title_textcolor"
    private const val WIDGET_TODAY_TEXTCOLOR = "weight_today_textcolor"

    fun init(context: Context) {
        sp = context.getSharedPreferences("main", Context.MODE_PRIVATE)
    }

    fun clearAll() = sp.edit().clear().apply()

    //夜间模式
    var nightMode: Boolean get() = sp.getBoolean(KEY_NIGHT_MODEL,false)
    set(value) = sp.edit().putBoolean(KEY_NIGHT_MODEL,value).apply()

    //主页
    var courseFirst: Boolean
        get() = sp.getBoolean("courseFirst", true)
        set(value) = sp.edit().putBoolean("courseFirst", value).apply()


    //课表id
    var groupId: Long
        get() = Preferences.getLong(
            App.instance.getString(R.string.app_preference_current_cs_name_id),
            0L
        )
        set(value) = Preferences.putLong(
            App.instance.getString(R.string.app_preference_current_cs_name_id),
            value
        )

    //第一次使用题库
    var firstUseBank: Boolean
        get() = sp.getBoolean("first_use_qbank", true)
        set(value) = sp.edit().putBoolean("first_use_qbank", value).apply()

    /*****************************************课表设置****************************************/
    //课表高度
    var courseHeight: Int
        get() = sp.getInt(COURSE_HEIGHT, 66)
        set(value) = sp.edit().putInt(COURSE_HEIGHT, value).apply()

    //课表字体大小
    var courseTextSize: Int
        get() = sp.getInt(COURSE_TEXTSIZE, 12)
        set(value) = sp.edit().putInt(COURSE_TEXTSIZE, value).apply()

    //课表字体颜色
    var courseTextColor: Int
        get() = sp.getInt(COURSE_TEXTCOLOR, Color.WHITE)
        set(value) = sp.edit().putInt(COURSE_TEXTCOLOR, value).apply()

    //显示非本周课程
    var showOtherWeek: Boolean
        get() = sp.getBoolean(COURSE_SHOWOTHERWEEK, true)
        set(value) = sp.edit().putBoolean(COURSE_SHOWOTHERWEEK, value).apply()

    //显示水平分割线
    var showHorizontalLine: Boolean
        get() = sp.getBoolean(COURSE_SHOWHORIZONTALLINE, true)
        set(value) = sp.edit().putBoolean(COURSE_SHOWHORIZONTALLINE, value).apply()

    //显示垂直分割线
    var showVerticalLine: Boolean
        get() = sp.getBoolean(COURSE_SHOWVERTICALLINE, true)
        set(value) = sp.edit().putBoolean(COURSE_SHOWVERTICALLINE, value).apply()

    //课表透明度
    var courseAlpha: Int
        get() = sp.getInt(COURSE_ALPHA, 75)
        set(value) = sp.edit().putInt(COURSE_ALPHA, value).apply()

    //课表边框宽度
    var courseLineWidth: Int
        get() = sp.getInt(COURSE_LINE_WIDTH, 2)
        set(value) = sp.edit().putInt(COURSE_LINE_WIDTH, value).apply()

    //课表边框颜色
    var courseLineColor: Int
        get() = sp.getInt(COURSE_LINE_COLOR, Color.BLACK)
        set(value) = sp.edit().putInt(COURSE_LINE_COLOR, value).apply()


    /*****************************************小部件设置****************************************/

    //小部件高度
    var widgetHeight: Int
        get() = sp.getInt(WIDGET_HEIGHT, 50)
        set(value) = sp.edit().putInt(WIDGET_HEIGHT, value).apply()

    //课表字体大小
    var widgetTextSize: Int
        get() = sp.getInt(WIDGET_TEXTSIZE, 12)
        set(value) = sp.edit().putInt(WIDGET_TEXTSIZE, value).apply()

    //课表字体颜色
    var widgetTextColor: Int
        get() = sp.getInt(WIDGET_TEXTCOLOR, Color.WHITE)
        set(value) = sp.edit().putInt(WIDGET_TEXTCOLOR, value).apply()

    //小部件标题字体颜色
    var widgetTitleTextColor: Int
        get() = sp.getInt(WIDGET_TITLE_TEXTCOLOR, Color.WHITE)
        set(value) = sp.edit().putInt(WIDGET_TITLE_TEXTCOLOR, value).apply()

    //小部件内边距
    var widgetPadding: Int
        get() = sp.getInt(WIDGET_PADDING, 1)
        set(value) = sp.edit().putInt(WIDGET_PADDING, value).apply()

    //小部件透明度
    var widgetAlpha: Int
        get() = sp.getInt(WIDGET_ALPHA, 40)
        set(value) = sp.edit().putInt(WIDGET_ALPHA, value).apply()

    //小部件边框宽度
    var widgetLineWidth: Int
        get() = sp.getInt(WIDGET_LINE_WIDTH, 2)
        set(value) = sp.edit().putInt(WIDGET_LINE_WIDTH, value).apply()

    //小部件边框颜色
    var widgetLineColor: Int
        get() = sp.getInt(WIDGET_LINE_COLOR, Color.WHITE)
        set(value) = sp.edit().putInt(WIDGET_LINE_COLOR, value).apply()


    /*****************************************当日小部件设置****************************************/

    //当日小部件透明度
    var widgetTodayAlpha: Int
        get() = sp.getInt(WIDGET_TODAY_ALPHA, 70)
        set(value) = sp.edit().putInt(WIDGET_TODAY_ALPHA, value).apply()

    //当日课表字体颜色
    var widgetTodayTextColor: Int
        get() = sp.getInt(WIDGET_TODAY_TEXTCOLOR, Color.WHITE)
        set(value) = sp.edit().putInt(WIDGET_TODAY_TEXTCOLOR, value).apply()

    //当日小部件标题字体颜色
    var widgetTodayTitleTextColor: Int
        get() = sp.getInt(WIDGET_TODAY_TITLE_TEXTCOLOR, Color.WHITE)
        set(value) = sp.edit().putInt(WIDGET_TODAY_TITLE_TEXTCOLOR, value).apply()

}
