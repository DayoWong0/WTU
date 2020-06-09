package grade.xyj.com.util.extend

import android.graphics.Bitmap
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.EditText
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import grade.xyj.com.util.extend.dsl.LifecycleObserverDSL
import org.jetbrains.anko.contentView

fun EditText.onTextChange(action: (CharSequence?) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            action(s)
        }
    })
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.inVisible() {
    visibility = View.INVISIBLE
}

fun View.layout(width: Int, height: Int) {
    layout(0, 0, width, height)
    val measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
    val measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.UNSPECIFIED)

    measure(measuredWidth, measuredHeight)
    layout(0, 0, this.measuredWidth, this.measuredHeight)
}

fun getViewBitmap(scrollView: ScrollView): Bitmap {
    var h = 0
    // 获取scrollView实际高度,这里很重要
    repeat(scrollView.childCount) {
        h += scrollView.getChildAt(it).height
    }

    // 创建对应大小的bitmap
    val bitmap = Bitmap.createBitmap(
        scrollView.width, h,
        Bitmap.Config.ARGB_8888
    )

    scrollView.draw(Canvas(bitmap))
    return bitmap
}
