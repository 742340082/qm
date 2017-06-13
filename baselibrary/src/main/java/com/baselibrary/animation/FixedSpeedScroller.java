package com.baselibrary.animation;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class FixedSpeedScroller
        extends Scroller
{
    private int mDuration = 1500;

    public FixedSpeedScroller(Context paramContext)
    {
        super(paramContext);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator)
    {
        super(context, interpolator);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator, boolean b)
    {
        super(context, interpolator, b);
    }

    public void startScroll(int startX, int startY, int dx, int dy)
    {
        super.startScroll(startX, startY, dx, dy, this.mDuration);
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration)
    {
        if (duration == 200)
        {
            super.startScroll(startX, startY, dx, dy, this.mDuration);
        }else {
            super.startScroll(startX, startY, dx, dy, duration);
        }
    }
}
