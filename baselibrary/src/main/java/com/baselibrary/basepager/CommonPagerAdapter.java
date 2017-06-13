package com.baselibrary.basepager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baselibrary.basepager.ViewHolder.ViewHolder;

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
    public void addTabPage(List<T> dataS, List<String> TitleS)
    {
        this.dataS =dataS;
        this.titleS =TitleS;
    }
    public void addTabPage(List<T> dataS)
    {
        this.dataS =dataS;
    }
    public abstract void convert(ViewHolder viewholder, T data, int position);

    public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
    {
        paramViewGroup.removeView((View)paramObject);
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//
//        return titleS.get(position);
//    }

    public int getCount()
    {
        return dataS.size();
    }

    public abstract int getLayouId();

    public Object instantiateItem(ViewGroup viewGroup, int position)
    {
        ViewHolder localViewHolder = ViewHolder.get(this.context, null,viewGroup, getLayouId(), position);
        convert(localViewHolder, dataS.get(position), position);
        viewGroup.addView(localViewHolder.getConvertView());
        return localViewHolder.getConvertView();
    }

    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }
}
