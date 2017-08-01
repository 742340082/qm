/*
 * File Name: MarketProgressArc.java 
 * History:
 * Created by wangyl on 2013-11-1
 */
package com.baselibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.baselibrary.R;
import com.baselibrary.utils.UIUtils;

import static com.baselibrary.utils.UIUtils.getDrawable;

/**
 * 圆形进度条
 *
 * @author Kevin
 */
public class ProgressArc extends View {
    // ==========================================================================
    // Constants
    // ==========================================================================
    //开始进度的角度
    private final static int START_PROGRESS_ANGLE = -90;
    //圆的最大角度
    private static final float CIRCLE_MAX_ANGLE = 360f;
    //背景的资源ID
    private int mDrawableForegroudResId;
    //背景的图片
    private Drawable mDrawableForegroud;// 图片
    // 进度条的颜色
    private int mProgressColor;
    // 用于画圆形的区域
    private RectF mArcRect;
    // 用户画进度条的画笔
    private Paint mARCPaint;
    //是否动态改变圆的原点
    private boolean mUserCenter = false;
    private int mCurrentProgress;// 当前进度
    private int mMAX;// 目标进度
    //进度条的宽度
    private int mProgressSweep;

    public ProgressArc(Context context) {
        this(context, null);

    }

    public ProgressArc(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressArc(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        mARCPaint = new Paint();
        mARCPaint.setAntiAlias(true);
        mARCPaint.setStrokeWidth(mProgressSweep);
        mARCPaint.setStyle(Paint.Style.STROKE);


        mUserCenter = false;
        mArcRect = new RectF();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        float defaultProgressSweep = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics());
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressArc);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int refreshId = typedArray.getIndex(i);
            if (refreshId == R.styleable.ProgressArc_BackgroundDrawable) {
                mDrawableForegroud = typedArray.getDrawable(refreshId);
            } else if (refreshId == R.styleable.ProgressArc_ProgressColor) {
                mProgressColor = typedArray.getColor(refreshId, Color.BLUE);
            } else if (refreshId == R.styleable.ProgressArc_ProgressSweep) {
                mProgressSweep = (int) typedArray.getDimension(refreshId, defaultProgressSweep);
            } else if (refreshId == R.styleable.ProgressArc_max) {
                mMAX = typedArray.getInt(refreshId, 100);
            } else if (refreshId == R.styleable.ProgressArc_progress) {
                mCurrentProgress = typedArray.getInt(refreshId, 0);
            }
        }
        typedArray.recycle();
    }

    public void seForegroundResource(int resId) {
        if (mDrawableForegroudResId == resId) {
            return;
        }
        mDrawableForegroudResId = resId;
        mDrawableForegroud = getDrawable(mDrawableForegroudResId);
        invalidateSafe();
    }


    /**
     * 设置进度条的颜色
     */
    public void setProgressColor(int progressColor) {
        mProgressColor = progressColor;
    }

    /**
     * 设置进度，第二个参数是否采用平滑进度
     */
    public void setProgress(int progress, boolean smooth) {
        mCurrentProgress = progress;
        invalidateSafe();
    }

    private void invalidateSafe() {
        if (UIUtils.isRunOnUiThread()) {
            postInvalidate();
        } else {
            invalidate();
        }
    }

    /**
     * 测量
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {// 如果是精确的
            width = widthSize;
        } else {// 采用图片的大小
            width = mDrawableForegroud == null ? 0 : mDrawableForegroud
                    .getIntrinsicWidth();
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {// 如果是精确的
            height = heightSize;
        } else {// 采用图片的大小
            height = mDrawableForegroud == null ? 0 : mDrawableForegroud
                    .getIntrinsicHeight();
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }

        // 计算出进度条的区域
        mArcRect.left = getPaddingLeft()+mProgressSweep/2;
        mArcRect.top = getPaddingTop()+mProgressSweep/2;
        mArcRect.right = width-getPaddingRight()-mProgressSweep/2;
        mArcRect.bottom = height-getPaddingBottom()-mProgressSweep/2;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDrawableForegroud != null) {// 先把图片画出来
            mDrawableForegroud.setBounds(mProgressSweep, mProgressSweep, getMeasuredWidth()-mProgressSweep,
                    getMeasuredHeight()-mProgressSweep);
            mDrawableForegroud.draw(canvas);
        }
        mARCPaint.setColor(mProgressColor);
        // 再画进度
        drawArc(canvas);
    }

    protected void drawArc(Canvas canvas) {

        float pencent = CIRCLE_MAX_ANGLE / mMAX;
        float currentAngle = mCurrentProgress * pencent;
        canvas.drawArc(mArcRect, START_PROGRESS_ANGLE, currentAngle, mUserCenter,
                mARCPaint);

    }
}

