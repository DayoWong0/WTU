package grade.xyj.com.component

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import grade.xyj.com.BuildConfig
import grade.xyj.com.R
import grade.xyj.com.base.BaseTitleActivity
import grade.xyj.com.util.URL
import grade.xyj.com.util.extend.copyToClipboard
import grade.xyj.com.util.extend.toClipboard
import grade.xyj.com.util.extend.toastError
import grade.xyj.com.util.extend.toastSuccess
import org.jetbrains.anko.*

class AboutActivity : BaseTitleActivity() {

    override val title: String
        get() = "关于"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val list =
            listOf(
                OpenBean(
                    "YZune/WakeupSchedule_Kotlin",
                    "https://github.com/YZune/WakeupSchedule_Kotlin/"
                ),
                OpenBean("Kotlin/anko", "https://github.com/Kotlin/anko"),
                OpenBean("square/okhttp", "https://github.com/square/okhttp"),
                OpenBean("google/gson", "https://github.com/google/gson"),
                OpenBean(
                    "Kotlin/kotlinx.coroutines",
                    "https://github.com/Kotlin/kotlinx.coroutines"
                ),
                OpenBean("ikew0ng/SwipeBackLayout", "https://github.com/ikew0ng/SwipeBackLayout"),
                OpenBean(
                    "scwang90/SmartRefreshLayout",
                    "https://github.com/scwang90/SmartRefreshLayout"
                ),
                OpenBean("MagicIndicator", "https://github.com/hackware1993/MagicIndicator"),
                OpenBean("laobie/StatusBarUtil", "https://github.com/laobie/StatusBarUtil"),
                OpenBean(
                    "CymChad/BaseRecyclerViewAdapterHelper",
                    "https://github.com/CymChad/BaseRecyclerViewAdapterHelper"
                ),
                OpenBean("mikepenz/MaterialDrawer", "https://github.com/mikepenz/MaterialDrawer"),
                OpenBean("AigeStudio/WheelPicker", "https://github.com/AigeStudio/WheelPicker"),
                OpenBean("Blankj/AndroidUtilCode", "https://github.com/Blankj/AndroidUtilCode")
            )
        val view = UI {
            scrollView {

                verticalLayout {
                    imageView(R.drawable.ic_388).lparams(dip(70), dip(70)) {
                        gravity = Gravity.CENTER
                        topMargin = dip(20)
                    }
                    textView("武纺") {}.lparams{
                        gravity = Gravity.CENTER
                        topMargin = dip(20)
                    }
                    textView(BuildConfig.VERSION_NAME) {}.lparams{
                        gravity = Gravity.CENTER
                        topMargin = dip(10)
                    }
                    textView("""
                        由于作者已经毕业，所以将停止该软件的开发与维护。
                        项目已经在Github开源，有兴趣的大佬可以接手。
                        对于之前一键评教的Bug对大家造成的影响非常抱歉，在这里给大家说一声对不起。已经出问题的同学需要去教务处102室找相关老师解决。
                    """.trimIndent()) {
                        textColorResource = R.color.dark_text_color
                        padding = dip(20)
                        gravity = Gravity.CENTER
                    }.lparams {
                        gravity = Gravity.CENTER
                    }
                    textView("开源地址:https://github.com/HandsomeXi/WTU"){
                        textColorResource = R.color.blue
                        leftPadding = dip(20)
                        rightPadding = dip(20)
                        setOnClickListener {
                            browse("https://github.com/HandsomeXi/WTU")
                        }
                    }.lparams{
                        gravity = Gravity.CENTER
                    }

                    textView("感谢以下开源项目") {
                        textSize = 16f
                        textColorResource = R.color.dark_text_color
                    }.lparams{
                        gravity = Gravity.CENTER
                        topMargin = dip(50)
                    }
                    list.forEach {
                        textView(it.name) {
                            setOnClickListener { _ -> browse(it.url) }
                        }.lparams {
                            gravity = Gravity.CENTER
                            topMargin = dip(5)
                        }
                    }
                }
            }
        }.view
        mContentView.addView(view)
    }

    class OpenBean(val name: String, val url: String)
}