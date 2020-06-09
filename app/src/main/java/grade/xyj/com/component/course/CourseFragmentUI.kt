package grade.xyj.com.component.course

import android.graphics.Color
import android.view.Gravity
import android.view.View
import grade.xyj.com.R
import grade.xyj.com.component.main.MainActivity
import grade.xyj.com.component.course.setting.ScheduleSettingsActivity
import grade.xyj.com.util.extend.getColorByCompat
import grade.xyj.com.view.ImportCourseDialog
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class CourseFragmentUI : AnkoComponent<CourseFragment> {

    override fun createView(ui: AnkoContext<CourseFragment>): View = ui.apply {
        verticalLayout {
            frameLayout {
                backgroundColorResource = R.color.light_text_color
                padding = dip(5)
                linearLayout {
                    imageView(R.drawable.ic_dehaze_black_24dp) {
                        onClick {
                            (owner.activity as MainActivity).openDrawer()
                        }
                        setColorFilter(context.getColorByCompat(R.color.dark_text_color))
                    }.lparams(wrapContent, dip(48))
                }.lparams(wrapContent, wrapContent) {
                    gravity = Gravity.CENTER_VERTICAL
                    marginStart = dip(20)
                }

                owner.mTitleLinear = verticalLayout {
                    leftPadding = dip(20)
                    rightPadding = dip(20)
                    onClick {
                        owner.mGroupName.callOnClick()
                    }
                    owner.mTitle = textView("第十三周") {
                        textColorResource = R.color.dark_text_color
                        textSize = 20f
                    }.lparams(wrapContent, wrapContent)
                    owner.mGroupName = textView("默认") {
                        textColorResource = R.color.dark_text_color
                        textSize = 12f
                        gravity = Gravity.CENTER
                    }.lparams(matchParent, wrapContent) {
                        topMargin = dip(3)
                    }
                }.lparams(wrapContent, wrapContent) {
                    gravity = Gravity.CENTER
                }
                linearLayout {
                    imageView(R.drawable.ic_file_download_black_24dp) {
                        padding = dip(10)
                        setColorFilter(context.getColorByCompat(R.color.dark_text_color))
                        onClick {
                            ImportCourseDialog(owner.activity!!)
                        }
                    }.lparams(wrapContent, dip(48)) {
                        gravity = Gravity.CENTER_VERTICAL
                    }
                    imageView(R.drawable.ic_settings_black_24dp) {
                        padding = dip(10)
                        setColorFilter(context.getColorByCompat(R.color.dark_text_color))
                        onClick {
                            startActivity<ScheduleSettingsActivity>()
                        }
                    }.lparams(wrapContent, dip(48))
                }.lparams(wrapContent, wrapContent) {
                    gravity = Gravity.CENTER_VERTICAL or Gravity.END
                    marginEnd = dip(10)
                }


            }
            owner.mContentView = verticalLayout { }.lparams(matchParent, matchParent)
        }
    }.view

}