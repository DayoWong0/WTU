package grade.xyj.com.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class LittleCourseView : View {
    companion object {
        const val CIRCLE_RADIUS = 6f
        const val STEP = 2f

        fun getWidth() = (14 * CIRCLE_RADIUS + 6 * STEP).toInt()
        fun getHeight() = (12 * CIRCLE_RADIUS + 5 * STEP).toInt()
    }

    private var array: Array<Array<Byte>?>? = null

    constructor(context: Context, parse: LittleParse) : super(context) {
        array = parse.array
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    private val blackPaint = Paint().apply {
        color = Color.parseColor("#D1DADB")
        isFilterBitmap = true
        isDither = true
        style = Paint.Style.FILL
    }

    private val bluePaint = Paint().apply {
        color = Color.parseColor("#66DAD3")
        isFilterBitmap = true
        isDither = true
        style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(LittleCourseView.getWidth(), LittleCourseView.getHeight())
    }


    fun setParse(parse: LittleParse) {
        array = parse.array
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (array == null) {
            return
        }
        for (i in 0..6) {
            val x = (i * 2 + 1) * CIRCLE_RADIUS + i * STEP
            for (j in 0..5) {
                val y = (j * 2 + 1) * CIRCLE_RADIUS + j * STEP
                canvas.drawCircle(
                    x,
                    y,
                    CIRCLE_RADIUS,
                    if (array!![i]!![j] == 1.toByte()) bluePaint else blackPaint
                )
            }
        }

    }
}