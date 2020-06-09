package grade.xyj.com.base

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import grade.xyj.com.R
import grade.xyj.com.util.extend.autoStatusBarModel
import grade.xyj.com.util.extend.getColorByCompat
import grade.xyj.com.util.extend.setWindow
import org.jetbrains.anko.*

abstract class BaseTitleActivity : AppCompatActivity(){
    lateinit var mContentView:LinearLayout
    lateinit var mFont:Typeface
    lateinit var mTitle:TextView

    abstract val title:String

    open fun onSetupSubButton(tvButton: TextView): TextView? {
        return null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setWindow()
        autoStatusBarModel()
        super.onCreate(savedInstanceState)
        verticalLayout {
            mFont = Typeface.createFromAsset(assets, "fonts/iconfont.ttf")
            linearLayout {
                id = R.id.anko_layout
                topPadding = BarUtils.getStatusBarHeight()
                backgroundColorResource = R.color.light_text_color
                val outValue = TypedValue()
                context.theme.resolveAttribute(R.attr.selectableItemBackgroundBorderless, outValue, true)

                imageButton(R.drawable.ic_arrow_back_black_24dp) {
                    backgroundResource = outValue.resourceId
                    setColorFilter(getColorByCompat(R.color.dark_text_color))
                    padding = dip(8)
                    setOnClickListener {
                        onBackPressed()
                    }
                }.lparams(wrapContent, dip(48))

                mTitle = textView(title) {
                    textColorResource = R.color.dark_text_color
                    gravity = Gravity.CENTER_VERTICAL
                    textSize = 16f
                    typeface = Typeface.DEFAULT_BOLD
                }.lparams(wrapContent, dip(48)) {
                    weight = 1f
                }
                onSetupSubButton(
                        textView {
                            gravity = Gravity.CENTER_VERTICAL
                            backgroundResource = outValue.resourceId
                            horizontalPadding = dip(24)
                        }.lparams(wrapContent, dip(48))
                )

            }.lparams(matchParent, wrapContent)
            mContentView = verticalLayout {}.lparams(matchParent, matchParent)

        }
    }
}