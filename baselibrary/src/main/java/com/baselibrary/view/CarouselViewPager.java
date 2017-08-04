package com.baselibrary.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.baselibrary.config.ConfigValues;

/**
 * Created by 74234 on 2017/8/4.
 */

public class CarouselViewPager extends ViewPager {
    private boolean isStartCarouse = false;
    private ViewPagerHandler mHandler = new ViewPagerHandler();
    private float downX;
    private float downY;

    public class ViewPagerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int item = getCurrentItem();
            item++;
            if (!isStartCarouse) {
                setCurrentItem(item);
                mHandler.removeCallbacksAndMessages(null);
                mHandler.sendMessageDelayed(Message.obtain(),
                        ConfigValues.VALUE_NEWS_CHANGE_TIME);
            }
        }
    }



    public CarouselViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(Message.obtain(), ConfigValues.VALUE_NEWS_CHANGE_TIME);// 延时4s发送消息
    }




    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        downX = 0f;
        downY = 0f;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                isStartCarouse = true;
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                isStartCarouse = true;
                float moveX = ev.getX();
                float moveY = ev.getY();
                float dx = moveX - downX;
                float dy = moveY - downY;
                // 判断是会否左右滑动
                if (Math.abs(dx) < Math.abs(dy)) {
                    //上下滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }else
                {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                isStartCarouse = false;
                mHandler.removeCallbacksAndMessages(null);
                mHandler.sendMessageDelayed(Message.obtain(),
                        ConfigValues.VALUE_NEWS_CHANGE_TIME);
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                isStartCarouse = false;
                mHandler.removeCallbacksAndMessages(null);
                mHandler.sendMessageDelayed(Message.obtain(),
                        ConfigValues.VALUE_NEWS_CHANGE_TIME);
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }

        return super.dispatchTouchEvent(ev);
    }


}
