package grade.xyj.com.util

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.*
import grade.xyj.com.R
import grade.xyj.com.util.extend.getColorByCompat
import grade.xyj.room.impl.CourseDaoImpl
import org.jetbrains.anko.*
import kotlin.math.roundToInt


fun createAllWeekView(context: Context): View = context.UI {

    val courses = CourseDaoImpl.getCoursesByGroupId(Settings.groupId).apply {
        forEach { it.init() }
    }
    Logger.e("小部件获取到 ${courses.size} 课程")
    val week = TimeUtils.getCurrentWeek()
    val dpSize = Settings.widgetHeight
    val tSize = Settings.widgetTextSize.toFloat()
    val tColor = Settings.widgetTextColor
    val titleColor = Settings.widgetTitleTextColor
    val pad = Settings.widgetPadding
    val lineWidth = Settings.widgetLineWidth
    val lineColor = Settings.widgetLineColor
    val widgetAlpha = (255 * (Settings.widgetAlpha.toFloat() / 100)).roundToInt()

    scrollView {
        id = 0
        linearLayout {
            orientation = LinearLayout.HORIZONTAL
            verticalLayout {
                for (i in 1..12) {
                    textView(i.toString()) {
                        textSize = 12f
                        textColor = titleColor
                        gravity = Gravity.CENTER
                    }.lparams(width = matchParent, height = dip(dpSize))
                }
            }.lparams(width = 0, height = wrapContent) {
                weight = 0.5f
            }
            for (i in 1..7) {
                frameLayout {
                    courses.forEach {
                        if (it.shouldShow(week) && it.week == i) {
                            verticalLayout {
                                textView {
                                    var showText = it.name
                                    if (it.location.isNotBlank()) {
                                        showText = "$showText@${it.location}"
                                    }
                                    text = showText
                                    background = ShapeUtils.courseItemBg(
                                        it.courseColor,
                                        lineColor,
                                        lineWidth,
                                        widgetAlpha
                                    )
                                    gravity = Gravity.CENTER
                                    textColor = tColor
                                    textSize = tSize
                                }.lparams(matchParent, matchParent)
                                padding = dip(pad)
                            }.lparams {
                                width = matchParent
                                height = it.nodeCount * dip(dpSize)
                                topMargin = (it.startNode - 1) * dip(dpSize)
                            }

                        }
                    }
                }.lparams(width = 0, height = matchParent) {
                    weight = 1f
                }
            }

        }.lparams(width = matchParent, height = wrapContent)
    }
}.view


fun setDrawerItems(builder: DrawerBuilder, context: Context) {
    val darkColor = context.getColorByCompat(R.color.dark_text_color)
    builder.addDrawerItems(
        PrimaryDrawerItem()
            .withIconColor(darkColor)
            .withIcon(R.mipmap.pic38)
            .withName("我的课表")
            .withTextColor(darkColor)
            .withIdentifier(10),
        ExpandableBadgeDrawerItem()
            .withName("教务")
            .withTextColor(darkColor)
            .withSubItems(
                SecondaryDrawerItem()
                    .withIconColor(darkColor)
                    .withTextColor(darkColor)
                    .withIcon(R.mipmap.pic20)
                    .withName("成绩查询")
                    .withIdentifier(20),

                SecondaryDrawerItem()
                    .withIconColor(darkColor)
                    .withTextColor(darkColor)
                    .withIcon(R.mipmap.pic17)
                    .withName("教务系统")
                    .withIdentifier(21)

            ),
        ExpandableBadgeDrawerItem()
            .withTextColor(darkColor)
            .withName("服务")
            .withSubItems(
                SecondaryDrawerItem()
                    .withIconColor(darkColor)
                    .withTextColor(darkColor)
                    .withIcon(R.mipmap.pic45)
                    .withName("校园卡")
                    .withIdentifier(30),

                SecondaryDrawerItem()
                    .withIconColor(darkColor)
                    .withTextColor(darkColor)
                    .withIcon(R.mipmap.pic19)
                    .withName("校内新闻")
                    .withIdentifier(32),

              /*  SecondaryDrawerItem()
                    .withIconColor(darkColor)
                    .withTextColor(darkColor)
                    .withIcon(R.mipmap.pic12)
                    .withName("校园网")
                    .withIdentifier(36),*/
                SecondaryDrawerItem()
                    .withIconColor(darkColor)
                    .withTextColor(darkColor)
                    .withIcon(R.mipmap.pic43)
                    .withName("黄页")
                    .withIdentifier(31)
            ),
        ExpandableBadgeDrawerItem().withName("工具")
            .withTextColor(darkColor)
            .withSubItems(
                /*SecondaryDrawerItem()
                    .withIconColorRes(R.color.dark_text_color)
                    .withTextColorRes(R.color.dark_text_color)
                    .withIcon(R.mipmap.pic14)
                    .withName("题库")
                    .withSelectable(false)
                    .withIdentifier(42),*/
                SecondaryDrawerItem()
                    .withIconColor(darkColor)
                    .withTextColor(darkColor)
                    .withIcon(R.mipmap.kefu)
                    .withName("百事通")
                    .withSelectable(false)
                    .withIdentifier(44),
                SecondaryDrawerItem()
                    .withIconColor(darkColor)
                    .withTextColor(darkColor)
                    .withIcon(R.mipmap.english)
                    .withName("46级查询")
                    .withSelectable(false)
                    .withIdentifier(45)
            ),

        SectionDrawerItem().withName("其他")
            .withTextColor(darkColor),

        PrimaryDrawerItem()
            .withIcon(R.mipmap.setting)
            .withIconColor(darkColor)
            .withTextColor(darkColor)
            .withName("设置")
            .withSelectable(false)
            .withIdentifier(43),

        PrimaryDrawerItem()
            .withIcon(R.mipmap.about)
            .withIconColor(darkColor)
            .withTextColor(darkColor)
            .withName("关于")
            .withSelectable(false)
            .withIdentifier(40),
        PrimaryDrawerItem()
            .withIcon(R.mipmap.logout)
            .withIconColor(darkColor)
            .withTextColor(darkColor)
            .withName("退出登录")
            .withSelectable(false)
            .withIdentifier(41)
    )
}