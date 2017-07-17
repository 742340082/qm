package com.baselibrary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.baselibrary.R;
import com.baselibrary.utils.UIUtils;

import java.util.List;

public class QuickIndexBar extends View {

    private String[] quickNameS = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private Paint paint;
    private float width;
    private float cellHeight;
    private boolean isVisable;
    private int headerViewCount;
    private List<? extends BaseIndexBean> cityList;
    private LinearLayoutManager linearLayoutManager;
    private  TextView tv_title;


    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuickIndexBar(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getMeasuredWidth();
        cellHeight = getMeasuredHeight() * 1f / quickNameS.length; // 获取一个格子的高度
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(UIUtils.getColor(R.color.textColorPrimary));
        paint.setTextSize(25);
        paint.setTextAlign(Align.CENTER);
    }

    public QuickIndexBar setHeaderViewCount(int headerViewCount) {
        this.headerViewCount = headerViewCount;
        return this;
    }

    public QuickIndexBar setData(List<? extends BaseIndexBean> list) {
        this.cityList = list;
        return this;
    }

    public QuickIndexBar setLinearLayoutManager(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
        return this;
    }
    public  QuickIndexBar showTextView(TextView tv_title)
    {
        this.tv_title = tv_title;
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < quickNameS.length; i++) {
            float canvasTextHeight = getCanvasTextHeight(quickNameS[i]);
            if (canvasTextHeight != -1) {
                float y = cellHeight / 2 + canvasTextHeight / 2 + cellHeight * i;// 获取绘画文本的y坐标
                float x = width / 2;// 获取绘画文本的x坐标
                canvas.drawText(quickNameS[i], x, y, paint);// 画多个个文本
            }

        }

    }

    private int lastIndex = -1;

    private void showCurrentView(String text, boolean isVisable, List<? extends  BaseIndexBean> list, LinearLayoutManager linearLayoutManager) {
        if(list!=null&&list.size()>0) {
            for (int i = 0; i < list.size(); i++) {
                String zi = list.get(i).getSuspensionTag();
                if (zi.equals(text)) {
                    linearLayoutManager.scrollToPositionWithOffset(i, 0);
                    break;
                }
                if (text.equals("#")) {
                    linearLayoutManager.scrollToPositionWithOffset(0, 0);
                    break;
                }
            }
            tv_title.setText(text);
            if (isVisable) {
                // 在运行关比
                tv_title.setVisibility(View.VISIBLE);
            } else {
                tv_title.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (isVisable != true) {
                isVisable = true;
            }
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isVisable = true;
        } else {
            lastIndex = -1;
            isVisable = false;
        }
        float y = event.getY();
        if (y > getMeasuredHeight()) {
            return true;
        }
        int currentIndex = (int) (y / cellHeight);//获取当前触摸的格子
        if (currentIndex != lastIndex) {
            //触摸当前的格子

            //需要当前触摸的currentIndex大于和等于0,并且currentIndex<=文字的长度
            if (currentIndex >= 0 && currentIndex <= quickNameS.length) {

                showCurrentView(quickNameS[currentIndex],isVisable, cityList,linearLayoutManager);
                if (onTouchLetterListener != null) {

                    onTouchLetterListener.TouchLetter(quickNameS[currentIndex], currentIndex, y, isVisable);
                }
            }
        }
        lastIndex = currentIndex;//上一个触摸的格子

        return true;
    }


    private float getCanvasTextHeight(String text) {

        if (text == null)
            return -1;
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    private OnTouchLetterListener onTouchLetterListener;

    public void setOnTouchLetterListener(OnTouchLetterListener onTouchLetterListener) {
        this.onTouchLetterListener = onTouchLetterListener;
    }

    //创建一个对文字触摸的监听
    public interface OnTouchLetterListener {
        void TouchLetter(String text, int position, float y, boolean isVisable);
    }
}
