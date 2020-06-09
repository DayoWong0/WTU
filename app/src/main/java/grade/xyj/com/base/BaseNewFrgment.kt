package grade.xyj.com.base

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import grade.xyj.com.R
import grade.xyj.com.component.main.MainActivity
import grade.xyj.com.util.extend.getColorByCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import kotlin.coroutines.CoroutineContext

abstract class NewBaseFragment :Fragment(), CoroutineScope {
    lateinit var mContentView: LinearLayout

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main


    abstract fun setTitle():String

    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?  = UI {
        verticalLayout {
            backgroundColorResource = R.color.light_text_color
            linearLayout {
                padding = dip(10)
                imageView(R.drawable.ic_dehaze_black_24dp) {
                    setColorFilter(context.getColorByCompat(R.color.dark_text_color))
                    padding = dip(8)
                    setOnClickListener {
                        activity?.run {
                            (this as MainActivity).openDrawer()
                        }
                    }
                }.lparams(wrapContent, dip(48)){
                    marginStart = dip(10)
                }
                textView(setTitle()) {
                    id = 123
                    textSize = 18f
                    typeface = Typeface.DEFAULT_BOLD
                }.lparams(wrapContent, wrapContent) {
                    gravity = Gravity.CENTER
                }
            }
            mContentView = verticalLayout {

            }.lparams(matchParent, matchParent)
        }

    }.view

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }
}