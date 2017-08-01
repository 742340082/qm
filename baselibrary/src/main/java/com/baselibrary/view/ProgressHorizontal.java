package com.baselibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews.RemoteView;

import com.baselibrary.R;
import com.baselibrary.utils.StringUtil;
import com.baselibrary.utils.UIUtils;

@RemoteView
public class ProgressHorizontal extends View {
    //最大的进度
    private int DOWNLOAD_PROGRESS_MAX = 100;
    private long mThreadId;
    //进度条背景
    private Drawable mDrbBackground;
    //进度背景
    private Drawable mDrbProgress;
    //进度条文字大小
    private float mProgressTextSize;
    //进度文字的颜色
    private int mProgressTextColor;
    //字体的风格
    private Typeface mTypeface = Typeface.DEFAULT;
    private Paint mTextPaint;
    //是否显示文字
    private boolean mProgressTextVisible = true;
    //进度
    private float mProgress;

    //进度可以显示的大小
    private Rect mRawProgressBounds;
    private StringBuilder mSb = new StringBuilder(4);
    //进度条显示的文字
    private String ProgressText;


    public ProgressHorizontal(Context context) {
        this(context, null);

    }

    public ProgressHorizontal(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressHorizontal(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        mRawProgressBounds = new Rect();
        mTextPaint = new Paint();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        float defaultTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressHorizontal);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int resourceId = typedArray.getIndex(i);
            if (resourceId == R.styleable.ProgressHorizontal_max) {
                DOWNLOAD_PROGRESS_MAX = typedArray.getInt(resourceId, 100);
            } else if (resourceId == R.styleable.ProgressHorizontal_progress) {
                mProgress = typedArray.getInt(resourceId, 0);
            } else if (resourceId == R.styleable.ProgressHorizontal_ProgressBackground) {
                mDrbProgress = typedArray.getDrawable(resourceId);
            } else if (resourceId == R.styleable.ProgressHorizontal_ProgressTextColor) {
                mProgressTextColor = typedArray.getColor(resourceId, Color.BLUE);
            } else if (resourceId == R.styleable.ProgressHorizontal_ProgressHorizontalBackground) {
                mDrbBackground = typedArray.getDrawable(resourceId);
            } else if (resourceId == R.styleable.ProgressHorizontal_ProgressTextSize) {
                mProgressTextSize = typedArray.getDimension(resourceId, defaultTextSize);
            }
        }
        typedArray.recycle();
    }


    public void setProgressBackgroundResource(int resId) {
        try {
            mDrbBackground = UIUtils.getDrawable(resId);
            if (null != mDrbBackground) {
                mDrbBackground.setBounds(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
            }
        } catch (Exception e) {
            mDrbBackground = null;
        }
        invalidate();
    }

    public void setProgressDrawble(Drawable drawable) {
        if (mDrbProgress == drawable) {
            return;
        }
        mDrbProgress = drawable;
        invalidate();
    }


    public void setMax(int max) {
        if (max <= 0) {
            return;
        }
        DOWNLOAD_PROGRESS_MAX = max;
    }

    public void setProgress(int progress) {
        setProgress(progress, false);
    }

    public synchronized void setProgress(float progress, boolean smooth) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > 100) {
            progress = 100;
        }
        mProgress = progress;
        invalidateSafe();
    }

    public void setProgressTextSize(int px) {
        mProgressTextSize = px;
    }

    public void setProgressTextColor(int color) {
        mProgressTextColor = color;
    }

    public void setProgressTextVisible(boolean visible) {
        mProgressTextVisible = visible;
    }

    public void setCenterText(String text) {
        ProgressText = text;
        invalidate();
    }


    private void invalidateSafe() {
        if (mThreadId == Process.myTid()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = mDrbBackground == null ? 0 : mDrbBackground.getIntrinsicWidth();
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = mDrbBackground == null ? 0 : mDrbBackground.getIntrinsicHeight();
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }
        mRawProgressBounds.set(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(), height
                - getPaddingBottom());

        mDrbBackground.setBounds(mRawProgressBounds);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        // Draw background
        if (null != mDrbBackground) {
            mDrbBackground.draw(canvas);
        }

        // Draw progress
        if (null != mDrbProgress) {

            if (mProgress == 0) {
                mDrbProgress.setBounds(0, 0, 0, 0);
            } else {
                int pencentSize = mRawProgressBounds.width() / DOWNLOAD_PROGRESS_MAX;

                mDrbProgress.setBounds(mRawProgressBounds.left,
                        mRawProgressBounds.top,
                        (int) (mRawProgressBounds.left + pencentSize
                                * mProgress)
                        , mRawProgressBounds.bottom);
            }
            mDrbProgress.draw(canvas);
        }

        // Draw progress text
        if (mProgressTextVisible) {
            mSb.delete(0, mSb.length());
            if (StringUtil.isEmpty(ProgressText)) {
                mSb.append(mProgress);
                mSb.append('%');
            } else {
                mSb.append(ProgressText);
            }
            String text = mSb.toString();

            mTextPaint.setAntiAlias(true);
            mTextPaint.setColor(mProgressTextColor);
            mTextPaint.setTextSize(mProgressTextSize);
            mTextPaint.setTypeface(mTypeface);
            mTextPaint.setTextAlign(Align.CENTER);
            FontMetrics fm = mTextPaint.getFontMetrics();
            int fontH = (int) (Math.abs(fm.descent - fm.ascent));
            canvas.drawText(text, getWidth() >> 1, ((getHeight() - getPaddingTop() - getPaddingBottom()) >> 1) + (fontH >> 1), mTextPaint);

        }

    }

    @Override
    protected void drawableStateChanged() {
        int[] drawableState = getDrawableState();
        if (mDrbBackground != null && mDrbBackground.isStateful()) {
            mDrbBackground.setState(drawableState);
        }
        if (mDrbProgress != null && mDrbProgress.isStateful()) {
            mDrbProgress.setState(drawableState);
        }
        invalidate();
    }


}
