package com.baselibrary.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class NoScrollViewPager
        extends ViewPager
{
    public NoScrollViewPager(Context context)
    {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
    }
@Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent)
    {
        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        return true;
    }
    @Override
    public void removeView(View view)
    {
        super.removeView(view);
    }
}
