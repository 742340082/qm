package com.baselibrary.base.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baselibrary.base.adapter.ViewHolder.ViewHolder;
import com.baselibrary.utils.StringUtil;

import java.util.List;

public abstract class CommonPagerAdapter<T>
        extends PagerAdapter
{
    protected Context context;
    private List<T> dataS;
    private List<String> titleS;
    protected LayoutInflater mInflater;

    public CommonPagerAdapter(Context context)
    {
        this.context = context;
        this.mInflater = LayoutInflater.from(this.context);
    }
    @Override
    public void destroyItem(ViewGroup viewGroup, int position, Object object)
    {
        viewGroup.removeView((View)object);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        int newPosition = position % dataS.size();
        if (!StringUtil.isEmpty(titleS.get(newPosition)))
        {
            return titleS.get(newPosition);
        }else
        {
            return null;
        }

    }
    @Override
    public int getCount()
    {
        return Integer.MAX_VALUE;
    }


    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position)
    {
        int newPosition = position % dataS.size();

        ViewHolder localViewHolder = ViewHolder.get(this.context, null,viewGroup, getLayouId(), newPosition);

        convert(localViewHolder, dataS.get(newPosition), newPosition);
        viewGroup.addView(localViewHolder.getConvertView());
        return localViewHolder.getConvertView();
    }

    public void addTabPage(List<T> dataS, List<String> TitleS)
    {
        this.dataS =dataS;
        this.titleS =TitleS;
    }
    public void addTabPage(List<T> dataS)
    {
        this.dataS =dataS;
    }

    public abstract int getLayouId();
    public abstract void convert(ViewHolder viewholder, T data, int position);
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }
}
