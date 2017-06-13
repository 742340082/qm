package com.baselibrary.basepager.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BaseRecyclerViewHolder
        extends RecyclerView.ViewHolder
{
    private Context context;
    private View rootView;
    private SparseArray<View> viewS = new SparseArray();

    public BaseRecyclerViewHolder(Context context, View view, int position)
    {
        super(view);
        this.context = context;
        this.rootView = view;
    }

    public BaseRecyclerViewHolder(View view)
    {
        super(view);
    }

    public static BaseRecyclerViewHolder getViewHolder(Context paramContext, ViewGroup paramViewGroup, int paramInt)
    {
        return new BaseRecyclerViewHolder(paramContext, LayoutInflater.from(paramContext).inflate(paramInt, paramViewGroup), paramInt);
    }

    public View getRootView()
    {
        return this.rootView;
    }

    public <T> T getView(int resId)
    {
        View view = (View)this.viewS.get(resId);
        if (view == null)
        {
            view = this.rootView.findViewById(resId);
            this.viewS.put(resId, view);
        }
        return (T)view;
    }

    public void setText(int resId, String content)
    {
        TextView localTextView = (TextView)getView(resId);
        if (content == null) {
            return;
        }
        localTextView.setText(content);
    }
}
