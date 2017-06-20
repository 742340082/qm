package com.baselibrary.base.adapter;

import android.view.View;

import com.baselibrary.utils.UIUtils;
import com.baselibrary.view.shake.StellarMap;

import java.util.List;

/**
 * Created by 74234 on 2017/6/17.
 */

public abstract class CommonShakeAdapter<T> implements StellarMap.Adapter {
    private List<T> data;
    private int layoutResId;
    private int postion;

    public CommonShakeAdapter(int layoutResId, List<T> data) {
    this.layoutResId=layoutResId;
        this.data=data;
    }

    public CommonShakeAdapter(List<T> data) {
        this.data=data;
    }

    public CommonShakeAdapter(int layoutResId) {
        this.layoutResId=layoutResId;
    }

    @Override
    public int getGroupCount() {
        return 7;
    }

    @Override
    public int getCount(int group) {
        int count = data.size() / getGroupCount();// 用总数除以组个数就是每组应该展示的孩子的个数
        if (group == getGroupCount() - 1) {// 由于上一行代码不一定整除, 最后一组,将余数补上
            count += data.size() % getGroupCount();
        }
        return count;
    }

    public void addData(List<T> data) {
        this.data = data;
    }

    @Override
    public View getView(int group,final int position, View convertView) {
        if (group > 0) {// 如果发现不是第一组,需要更新position, 要加上之前几页的总数,才是当前组的准确位置
            this.postion = position + getCount(group - 1) * group;
        }else
        {
            this.postion =position;
        }
        T t = data.get(postion);
        View view = View.inflate(UIUtils.getContext(), layoutResId, null);
        convert(t,view);
        setCurrentItemClick(view,this.postion);
        return view;
    }

    private void setCurrentItemClick(View view,final int postion) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null)
                {
                    listener.itemClick(CommonShakeAdapter.this,v,postion);
                }
            }
        });
    }

    public T getItem(int position)
    {
        return data.get(position);
    }

    protected abstract void convert(T data,View view);
    @Override
    public int getNextGroupOnZoom(int group, boolean isZoomIn) {
        if (!isZoomIn) {
            // 下一组
            if (group < getGroupCount() - 1) {
                return ++group;
            } else {
                return 0;// 如果没有下一页了,就跳到第一组
            }
        } else {
            // 上一组
            if (group > 0) {
                return --group;
            } else {
                return getGroupCount() - 1;// 如果没有上一页了,就跳到最后一组
            }
        }
    }



    public interface  OnItemClickListener
    {
        void itemClick(CommonShakeAdapter adapter,View view ,int postion);
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener=listener;
    }
}
