package grade.xyj.com.util

import android.text.format.DateUtils
import java.util.*

object TimeUtils {
    fun getCurrentWeek(): Int {
        val cal = getFirstWeekDay()
        val longs = System.currentTimeMillis() - cal.timeInMillis
        val day = (longs / (24 * 60 * 60 * 1000)).toInt() / 7 + 1
        return if (day in 1..20) day else 1
    }

    fun getFirstWeekDay(): Calendar {
        val cal = Calendar.getInstance()
        val year = Preferences.getInt("year", 0)
        if (year == 0) {
            cal.add(Calendar.DATE, 2 - cal.get(Calendar.DAY_OF_WEEK))
        } else {
            val month = Preferences.getInt("month", 0)
            val day = Preferences.getInt("day", 0)
            cal.set(year, month, day)
        }
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal
    }

    private fun setFirstWeekDay(cal: Calendar) {
        Preferences.putInt("year", cal.get(Calendar.YEAR))
        Preferences.putInt("month", cal.get(Calendar.MONTH))
        Preferences.putInt("day", cal.get(Calendar.DAY_OF_MONTH))
    }

    //根据当前周数获取第一周第一天
    private fun getFirstWeekDay(week: Int): Calendar = Calendar.getInstance().apply {
        add(Calendar.DATE, 1 - dayOfWeek - (week - 1) * 7)
    }

    //根据第一周第一天和当前周数获取日期列表
    fun getDays(cal: Calendar, week: Int): IntArray {
        val arrays = IntArray(8)
        cal.add(Calendar.DATE, (week - 1) * 7)
        //获取月份
        arrays[0] = cal.get(Calendar.MONTH) + 1
        //获取日期
        (1..7).forEach {
            arrays[it] = cal.get(Calendar.DAY_OF_MONTH)
            cal.add(Calendar.DATE, 1)
        }
        return arrays
    }

    fun getCurrentSchoolYearAndSemester(): IntArray {
        val xq: (Int) -> Int = { if (it in 2..7) 2 else 1 }
        return Date().run {
            val x = xq(month + 1)
            intArrayOf(year + 1901 - x, x)
        }
    }

    val month get() = Date().month + 1

    val day get() = Date().date

    val dayOfWeek get() = Date().run { if (day == 0) 7 else day }

    val todayString get() = "${month}月${day}日  ${Constant.WEEK[dayOfWeek]}"

    fun setCurrentWeek(week: Int) = setFirstWeekDay(getFirstWeekDay(week))

    fun isToday(time: Long): Boolean = DateUtils.isToday(time)

}