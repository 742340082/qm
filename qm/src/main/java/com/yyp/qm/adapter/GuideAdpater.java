package com.yyp.qm.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yyp.qm.R;

import java.util.ArrayList;

public class GuideAdpater
        extends PagerAdapter
{
    private ArrayList<View> guides;
    private LayoutInflater inflater;
    private OnFinishGuideActivityListener listener;
    private Context mContext;
    public GuideAdpater(Context context)
    {
        this.mContext = context;
        LayoutInflater localLayoutInflater = this.inflater;
        this.inflater = LayoutInflater.from(context);
    }

    public void destroyItem(ViewGroup ViewGroup, int postion, Object object)
    {
        ViewGroup.removeView((View)object);
    }

    public int getCount()
    {
        return this.guides.size();
    }

    public View instantiateItem(ViewGroup ViewGroup, int position)
    {
        View localView = (View)this.guides.get(position);
        ViewGroup.addView(localView);
        if (position == getCount() - 1) {
            ((Button)localView.findViewById(R.id.btn_guide_enter)).setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View paramAnonymousView)
                {
                    if (GuideAdpater.this.listener != null) {
                        GuideAdpater.this.listener.finishGuideActivity();
                    }
                }
            });
        }
        return localView;
    }

    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

    public void setOnFinishGuideActivityListener(OnFinishGuideActivityListener onFinishGuideActivityListener)
    {
        this.listener = onFinishGuideActivityListener;
    }

    public void setViews(ArrayList<View> guides)
    {
        this.guides = guides;
    }

    public   interface OnFinishGuideActivityListener
    {
          void finishGuideActivity();
    }
}
