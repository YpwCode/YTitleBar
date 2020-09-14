package com.ypw.code.view.title

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import com.ypw.code.util.BarUtils
import com.ypw.code.util.ConvertUtils
import com.ypw.code.util.getDimen
import com.ypw.code.util.getDrawable
import com.ypw.code.view.side.R
import kotlin.math.max

/**
 * - xml 里通过指定 ID 直接创建 View
 * - xml 里通过属性指定 View
 * - 默认 View
 * - 需要可以设置背景颜色
 * - 需要可以设置字体大小/颜色
 */
class YTitleBar constructor(context: Context, attrs: AttributeSet? = null) : ViewGroup(context, attrs) {

    private var mWidth = 0
    private var mHeight = R.dimen.tbar_size.getDimen(context)

    private var mCenterX = 0
    private var mCenterY = 0

    private var mBgColor = Color.WHITE

    private var mLeftColor = Color.RED

    private var mCenterColor = Color.BLACK
    private var mCenterSize = R.dimen.sw_14.getDimen(context)

    private var mRightColor = Color.BLACK
    private var mRightSize = R.dimen.sw_16.getDimen(context)

    var mLeftView: View? = null
    var mCenterView: View? = null
    var mRightView: View? = null

    var mLeftShow = true
    var mCenterShow = true
    var mRightShow = false

    var mCenterStr = ""
    var mRightStr = ""

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateLayoutParams(lp: LayoutParams?): LayoutParams {
        return MarginLayoutParams(lp)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    init {
        attrs?.let {
            val types = context.obtainStyledAttributes(it, R.styleable.YTitleBar)

            val leftResId = types.getResourceId(R.styleable.YTitleBar_tbar_left, -1)
            val centerResId = types.getResourceId(R.styleable.YTitleBar_tbar_center, -1)
            val rightResId = types.getResourceId(R.styleable.YTitleBar_tbar_right, -1)

            mLeftShow = types.getBoolean(R.styleable.YTitleBar_tbar_leftShow, mLeftShow)
            mCenterShow = types.getBoolean(R.styleable.YTitleBar_tbar_centerShow, mCenterShow)
            mRightShow = types.getBoolean(R.styleable.YTitleBar_tbar_rightShow, mRightShow)

            mBgColor = types.getColor(R.styleable.YTitleBar_tbar_bg, mBgColor)
            mLeftColor = types.getColor(R.styleable.YTitleBar_tbar_leftColor, mLeftColor)
            mCenterColor = types.getColor(R.styleable.YTitleBar_tbar_centerColor, mCenterColor)
            mCenterSize = types.getDimensionPixelOffset(R.styleable.YTitleBar_tbar_centerSize, mCenterSize)
            mRightColor = types.getColor(R.styleable.YTitleBar_tbar_rightColor, mRightColor)
            mRightSize = types.getDimensionPixelOffset(R.styleable.YTitleBar_tbar_rightSize, mRightSize)

            mCenterStr = types.getString(R.styleable.YTitleBar_tbar_centerStr) ?: ""
            mRightStr = types.getString(R.styleable.YTitleBar_tbar_rightStr) ?: ""

            types.recycle()

            if (leftResId > 0) {
                mLeftView = LayoutInflater.from(context).inflate(leftResId, this, false)
            }
            if (centerResId > 0) {
                mCenterView = LayoutInflater.from(context).inflate(centerResId, this, false)
            }
            if (rightResId > 0) {
                mRightView = LayoutInflater.from(context).inflate(rightResId, this, false)
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        fetchViewFromIdOrDefault()
        removeAllViews()
        addView(mLeftView)
        addView(mCenterView)
        addView(mRightView)
        if (mBgColor < 1) {
            setBackgroundColor(mBgColor)
        }
    }

    private fun fetchViewFromIdOrDefault() {
        if (mLeftView == null) {
            var m = findViewById<View>(R.id.tbar_left_id)
            mLeftView = findViewById(R.id.tbar_left_id) ?: getDefaultLeftView()
            mLeftView!!.apply {
                setOnClickListener {
                    BarUtils.getActivityByView(this).finish()
                }
                visibility = if (mLeftShow) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
        if (mCenterView == null) {
            mCenterView = findViewById(R.id.tbar_center_id) ?: getDefaultCenterView()
            mCenterView!!.apply {
                visibility = if (mCenterShow) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
        if (mRightView == null) {
            mRightView = findViewById(R.id.tbar_right_id) ?: getDefaultRightView()
            mRightView!!.apply {
                visibility = if (mRightShow) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }
    private fun getDefaultLeftView(): View {
        return ImageView(context).apply {
            id = R.id.tbar_left_id
            layoutParams = LayoutParams(R.dimen.tbar_size.getDimen(context), R.dimen.tbar_size.getDimen(context))
            val drawable = R.drawable.ic_back.getDrawable(context)
            DrawableCompat.setTint(drawable, mLeftColor)
            setImageDrawable(drawable)
            val pad = ConvertUtils.dp2px(14f)
            setPadding(pad, 0, pad, 0)
        }
    }
    private fun getDefaultCenterView(): View {
        return TextView(context).apply {
            id = R.id.tbar_center_id
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            text = mCenterStr
            setTextColor(mCenterColor)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightSize.toFloat())
        }
    }
    private fun getDefaultRightView(): View {
        return TextView(context).apply {
            id = R.id.tbar_right_id
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            text = mRightStr
            setTextColor(mRightColor)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightSize.toFloat())
            val pad = ConvertUtils.dp2px(15f)
            setPadding(pad, 0, pad, 0)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mLeftView?.let {
            val lp = it.layoutParams
            val h = if (lp is MarginLayoutParams) {
                it.measuredHeight + lp.topMargin + lp.bottomMargin
            } else {
                it.measuredHeight
            }
            mHeight = max(h, mHeight)
        }
        mCenterView?.let {
            val lp = it.layoutParams
            val h = if (lp is MarginLayoutParams) {
                it.measuredHeight + lp.topMargin + lp.bottomMargin
            } else {
                it.measuredHeight
            }
            mHeight = max(h, mHeight)
        }
        mRightView?.let {
            val lp = it.layoutParams
            val h = if (lp is MarginLayoutParams) {
                it.measuredHeight + lp.topMargin + lp.bottomMargin
            } else {
                it.measuredHeight
            }
            mHeight = max(h, mHeight)
        }

        mCenterX = mWidth / 2
        mCenterY = mHeight / 2

        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        layoutLeft()
        layoutCenter()
        layoutRight()
    }

    private fun layoutLeft() {
        val leftView = mLeftView ?: return

        val lp = leftView.layoutParams
        val offset = if (lp is MarginLayoutParams) {
            lp.marginStart
        } else {
            0
        }

        val width = leftView.measuredWidth
        val height = leftView.measuredHeight

        val space = mHeight - height

        val padding = paddingTop + paddingBottom

        val left = paddingStart + offset
        val right = left + width
        val top: Int
        val bottom: Int

        if (padding >= space) {
            top = paddingTop
            bottom = mHeight - paddingBottom
        } else {
            val offset = when {
                paddingTop > space / 2 -> {
                    paddingTop - space / 2
                }
                paddingBottom > space / 2 -> {
                    space / 2 - paddingBottom
                }
                else -> {
                    0
                }
            }
            top = mCenterY - height / 2 + offset
            bottom = top + height
        }

        leftView.layout(left, top, right, bottom)
    }

    private fun layoutCenter() {
        val centerView = mCenterView ?: return

        val height = centerView.measuredHeight

        val space = mHeight - height

        val padding = paddingTop + paddingBottom

        val left = mCenterX - centerView.measuredWidth / 2
        val right = mCenterX + centerView.measuredWidth / 2

        val top: Int
        val bottom: Int

        if (padding >= space) {
            top = paddingTop
            bottom = mHeight - paddingBottom
        } else {
            val offset = when {
                paddingTop > space / 2 -> {
                    paddingTop - space / 2
                }
                paddingBottom > space / 2 -> {
                    space / 2 - paddingBottom
                }
                else -> {
                    0
                }
            }
            top = mCenterY - height / 2 + offset
            bottom = top + height
        }

        centerView.layout(left, top, right, bottom)
    }

    private fun layoutRight() {
        val rightView = mRightView ?: return

        val lp = rightView.layoutParams
        val offset = if (lp is MarginLayoutParams) {
            lp.marginEnd
        } else {
            0
        }

        val width = rightView.measuredWidth
        val height = rightView.measuredHeight

        val space = mHeight - height

        val padding = paddingTop + paddingBottom

        val right = mWidth - paddingEnd - offset
        val left = right - width
        val top: Int
        val bottom: Int

        if (padding >= space) {
            top = paddingTop
            bottom = mHeight - paddingBottom
        } else {
            val offset = when {
                paddingTop > space / 2 -> {
                    paddingTop - space / 2
                }
                paddingBottom > space / 2 -> {
                    space / 2 - paddingBottom
                }
                else -> {
                    0
                }
            }
            top = mCenterY - height / 2 + offset
            bottom = top + height
        }

        rightView.layout(left, top, right, bottom)

    }

    fun setCenterStr(str: String) {
        mCenterView?.let {
            if (it is TextView) {
                it.text = str
            }
        }
    }

    fun setRightStr(str: String) {
        mRightView?.let {
            if (it is TextView) {
                it.text = str
            }
        }
    }

    fun setLeftListener(onClickListener: OnClickListener) {
        mLeftView?.setOnClickListener(onClickListener)
    }

    fun setCenterListener(onClickListener: OnClickListener) {
        mCenterView?.setOnClickListener(onClickListener)
    }

    fun setRightListener(onClickListener: OnClickListener) {
        mRightView?.setOnClickListener(onClickListener)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (mBgColor < 1 && !isInEditMode) {
            BarUtils.setStatusBarColor(BarUtils.getActivityByView(this), mBgColor)
            BarUtils.setStatusBarLightMode(BarUtils.getActivityByView(this), BarUtils.isLightColor(mBgColor))
            BarUtils.addMarginTopEqualStatusBarHeight(this)
        }

        if (Build.VERSION.SDK_INT >= 21) {
            elevation = R.dimen.sw_10.getDimen(context).toFloat()
        }
    }
}