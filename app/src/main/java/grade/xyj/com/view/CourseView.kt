package grade.xyj.com.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import grade.xyj.com.R
import grade.xyj.com.bean.CourseAncestor
import grade.xyj.com.classchedule.util.Utils
import grade.xyj.com.util.Settings
import grade.xyj.com.util.ShapeUtils
import grade.xyj.com.util.TimeUtils
import org.jetbrains.anko.dip

class CourseView(context: Context, attrs: AttributeSet):FrameLayout(context,attrs){
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private var mRowCount = 7
    private var mColCount = 12

    private var mRowItemWidth = dip(50f)
    private var mColItemHeight = dip(Settings.courseHeight.toFloat())

    var mCurrentIndex = TimeUtils.getCurrentWeek()

    /** 行item的宽度根据view的总宽度自动平均分配  */
    private var mRowItemWidthAuto = true

    internal var mCourseList = mutableListOf<CourseAncestor>()

    private var mAddTagCourse: CourseAncestor? = null
    private var mAddTagCourseView: View? = null

    /** 圆角弧度 */
    private var mCourseItemRadius = 3f

    private val mLinePath = Path()


    /** 第一次绘制  */
    private var mFirstDraw: Boolean = false

    /** text  */
    private var mTextLRPadding = dip(2f)
    private var mTextTBPadding = dip(4f)
    private var textTBMargin = dip(3f)
    private var textLRMargin = dip(3f)
    //字体颜色
    private var mTextColor = Settings.courseTextColor
    //字体大小
    private var mTextSize = Settings.courseTextSize
    //显示不活跃课程
    private var showOtherWeek = Settings.showOtherWeek
    /** 显示垂直分割线  */
    private var mShowVerticalLine = Settings.showVerticalLine
    /** 显示水平分割线  */
    private var mShowHorizontalLine = Settings.showHorizontalLine
    //边框宽度
    private var mLineWidth = Settings.courseLineWidth
    //边框颜色
    private var mLineColor = Settings.courseLineColor
    //格子透明度
    private var mAlpha = Math.round(255 * (Settings.courseAlpha.toFloat() / 100))



    /** 不活跃  */
    private var mInactiveBackgroundColor = -0x1c110b
    private val mInactiveTextColor = -0x452537

    private var mItemClickListener: OnItemClickListener? = null


    fun updateSetting(){
        mTextSize = Settings.courseTextSize
        mColItemHeight = dip(Settings.courseHeight.toFloat())
        mTextColor = Settings.courseTextColor
        mShowVerticalLine = Settings.showVerticalLine
        mShowHorizontalLine = Settings.showHorizontalLine
        showOtherWeek = Settings.showOtherWeek

        mAlpha = Math.round(255 * (Settings.courseAlpha.toFloat() / 100))
        mLineColor = Settings.courseLineColor
        mLineWidth = Settings.courseLineWidth
        initCourseItemView()
    }


    private val mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {

        color = Color.LTGRAY
        strokeWidth = 1f
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(5f, 5f), 0f)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mHeight = mColItemHeight * mColCount
        val heightResult = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY)

        setMeasuredDimension(widthMeasureSpec, heightResult)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (mRowItemWidthAuto) {
            mWidth = w
            mRowItemWidth = mWidth / mRowCount
        } else {
            mWidth = mRowItemWidth * mRowCount
        }
    }

    /** 把数组中的数据全部添加到界面  */
    private fun initCourseItemView() {
        removeAllViews()
        mCourseList.forEach {
            realAddCourseItemView(it)
        }
    }

    fun addCourse(course: CourseAncestor) {
        if (!mCourseList.contains(course)) {
            mCourseList.add(course)
            realAddCourseItemView(course)
        }
    }

    private fun realAddCourseItemView(course: CourseAncestor) {

        if (!course.isDisplayable) {
            return
        }

        course.activeStatus = course.shouldShow(mCurrentIndex)

        if(!course.activeStatus && !showOtherWeek) return

        val itemView = createItemView(course).apply {
            layoutParams = LayoutParams(mRowItemWidth,
                    mColItemHeight * course.rowNum).apply {
                //动态计算课程位置
                leftMargin = (course.row - 1) * mRowItemWidth
                topMargin = (course.col - 1) * mColItemHeight
            }
        }
        //添加课程
        if (course.activeStatus) {
            addView(itemView)
        } else {
            //展示其它周的课程但是优先级低
            addView(itemView, 0)
        }
    }

    private fun setItemViewBackground(course: CourseAncestor, tv: TextView) {
        if(course.activeStatus){
            tv.background = ShapeUtils.courseItemBg(course.color,mLineColor,mLineWidth,mAlpha)
        }else{
            tv.setTextColor(mInactiveTextColor)
            tv.background = getShowBgDrawable(mInactiveBackgroundColor, mInactiveBackgroundColor and -0x7f000001)
        }
    }

    private fun getShowBgDrawable(color: Int, color2: Int): StateListDrawable = Utils.getPressedSelector(context, color, color2, mCourseItemRadius)

    private fun getCourseTextView(h: Int, w: Int) = TextView(context).apply {
        layoutParams = FrameLayout.LayoutParams(w, h)
        setTextColor(mTextColor)
        setLineSpacing(-2f, 1f)
        setPadding(mTextLRPadding, mTextTBPadding, mTextLRPadding, mTextTBPadding)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize.toFloat())
        isFocusable = true
        isClickable = true

        paint.isFakeBoldText = true
    }

    override fun dispatchDraw(canvas: Canvas) {
        drawLine(canvas)
        super.dispatchDraw(canvas)

        if (!mFirstDraw) {
            initCourseItemView()
            mFirstDraw = true
        }
    }

    private fun drawLine(canvas: Canvas) {
        //横线
        if (mShowHorizontalLine) {
            for (i in 1 until mColCount) {
                mLinePath.run {
                    reset()
                    moveTo(0f, (i * mColItemHeight).toFloat())
                    lineTo(mWidth.toFloat(), (i * mColItemHeight).toFloat())
                }
                canvas.drawPath(mLinePath, mLinePaint)
            }
        }

        //竖线
        if (mShowVerticalLine) {
            for (i in 1 until mRowCount) {
                mLinePath.run {
                    reset()
                    moveTo((i * mRowItemWidth).toFloat(), 0f)
                    lineTo((i * mRowItemWidth).toFloat(), mHeight.toFloat())
                }
                canvas.drawPath(mLinePath, mLinePaint)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true

            MotionEvent.ACTION_MOVE -> removeAddTagView()

            MotionEvent.ACTION_UP -> addTagCourseView(event.x.toInt(), event.y.toInt())
        }

        return super.onTouchEvent(event)
    }

    private fun addTagCourseView(x: Int, y: Int) {

        /*找到点击的方框坐标*/
        var x1 = x / mRowItemWidth + 1
        var y1 = y / mColItemHeight + 1

        if (x1 > mRowCount) x1 = mRowCount

        if (y1 > mColCount) y1 = mColCount

        if (mAddTagCourse == null)
            mAddTagCourse = CourseAncestor()

        if (mAddTagCourseView == null)
            mAddTagCourseView = createAddTagView()
        else
            removeView(mAddTagCourseView)

        mAddTagCourse?.row = x1
        mAddTagCourse?.col = y1

        realAddTagCourseView()
    }

    /**
     * 移除添加按钮
     */
    private fun removeAddTagView() {
        if (mAddTagCourseView != null) {
            removeView(mAddTagCourseView)
        }
    }

    fun resetView() = initCourseItemView()


    /**
     * 建立添加按钮
     */
    private fun createAddTagView(): View {
        val iv = ImageView(context).apply {

            layoutParams = LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT).apply {
                setMargins(textLRMargin, textTBMargin, textLRMargin, textTBMargin)
            }
            setImageResource(R.drawable.ic_svg_add)
            scaleType = ImageView.ScaleType.CENTER
            setBackgroundColor(Color.LTGRAY)
            background = Utils.getPressedSelector(context, Color.LTGRAY, Color.LTGRAY, mCourseItemRadius)
            isClickable = true
            isFocusable = true
            setOnClickListener {
                mItemClickListener?.run {
                    onAdd(mAddTagCourse!!, mAddTagCourseView!!)
                    removeAddTagView()
                }
            }
        }
        return FrameLayout(context).apply { addView(iv) }
    }

    /**
     * 建立itemview
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun createItemView(course: CourseAncestor): View {

        val showText = if (course.activeStatus)
            course.text
        else
            "[非本周]${course.text}"


        //TextView
        val tv = getCourseTextView(mColItemHeight * course.rowNum, mRowItemWidth).apply {
            layoutParams = LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT).apply {
                setMargins(textLRMargin, textTBMargin, textLRMargin, textTBMargin)
            }
            text = showText
        }
        val bgLayout = FrameLayout(context).apply { addView(tv) }

        setItemViewBackground(course, tv)
        itemEvent(course, bgLayout, tv)

        return bgLayout
    }

    private fun itemEvent(course: CourseAncestor, viewGroup: ViewGroup, textView: TextView) {
        val courses = mutableListOf<CourseAncestor>()
        courses.add(course)

        /*查找在点击的item范围内重叠的item*/
        for (findCourse in mCourseList) {
            if (findCourse.row == course.row && course !== findCourse) {
                if (findCourse.col < course.col + course.rowNum && findCourse.col >= course.col) {
                    courses.add(findCourse)
                }
            }
        }

        textView.setOnClickListener {
            removeAddTagView()
            mItemClickListener?.onClick(courses, viewGroup)
        }

        textView.setOnLongClickListener { v ->
            removeAddTagView()
            mItemClickListener?.let {
                it.onLongClick(courses, viewGroup)
                return@setOnLongClickListener true
            }
            false
        }
    }

    private fun realAddTagCourseView() {
        mAddTagCourseView!!.layoutParams = FrameLayout.LayoutParams(mRowItemWidth,
                mColItemHeight * mAddTagCourse!!.rowNum).apply {
            leftMargin = (mAddTagCourse!!.getRow() - 1) * mRowItemWidth
            topMargin = (mAddTagCourse!!.getCol() - 1) * mColItemHeight
        }
        addView(mAddTagCourseView)
    }

    fun clear() {
        removeAllViews()
        mCourseList.clear()
    }

    abstract class OnItemClickListener {
        abstract fun onClick(courses: List<CourseAncestor>, itemView: View)

        abstract fun onLongClick(courses: List<CourseAncestor>, itemView: View)

        abstract fun onAdd(course: CourseAncestor, addView: View)
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        mItemClickListener = itemClickListener
    }

}