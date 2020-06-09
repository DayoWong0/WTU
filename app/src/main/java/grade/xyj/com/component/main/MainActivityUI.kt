package grade.xyj.com.component.main

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import com.blankj.utilcode.util.BarUtils
import grade.xyj.com.R
import grade.xyj.com.util.extend.setWindow
import org.jetbrains.anko.*

class MainActivityUI :AnkoComponent<MainActivity>{
    @SuppressLint("ResourceType")
    override fun createView(ui: AnkoContext<MainActivity>): View = ui.apply {
        frameLayout {
            fitsSystemWindows = true
            id = 123456
            //backgroundColorResource = R.color.light_text_color
        }

    }.view

}