package grade.xyj.com.view

import grade.xyj.room.entity.CourseEntity

class LittleParse(courses: List<CourseEntity>, week: Int) {
    val array = arrayOfNulls<Array<Byte>>(7)

    init {
        for (i in 0..6){ array[i] = arrayOf(0,0,0,0,0,0) }
        for (course in courses){
            if (!course.shouldShow(week)) continue
            val row = course.col
            val rowNum = course.rowNum
            val col = course.row
            val start = (row-1)/2
            val end = (row+rowNum-2)/2
            for (i in start..end){
                array[col-1]!![i] = 1
            }
        }
    }
}

