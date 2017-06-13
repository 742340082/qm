package com.game.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.mvp.category.modle.Data;
import com.game.mvp.category.modle.Tag;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 74234 on 2017/6/11.
 */

public class CategoryAdapter extends BaseQuickAdapter<Data,BaseViewHolder> {
    public CategoryAdapter(int layoutResId) {
        super(layoutResId);
    }

    public CategoryAdapter(int layoutResId, List<Data> data) {

        super(layoutResId, data);
    }
    //记录TextView数量
    private static  int k;
    //记录LinearLayout数量
    private  int t;
    @Override
    protected void convert(BaseViewHolder helper, Data item) {
        LinearLayout convertView = (LinearLayout) helper.getConvertView();

        RelativeLayout relativeLayout = (RelativeLayout) convertView.getChildAt(0);
        CircleImageView civ_category_icon = (CircleImageView) relativeLayout.getChildAt(0);
        TextView tv_category_title = (TextView) relativeLayout.getChildAt(1);
        TextView tv_category_new_count = (TextView) relativeLayout.getChildAt(2);
        tv_category_title.setText(item.getName());
        tv_category_new_count.setText("新增"+item.getNew_count()+"款");
        Glide.with(UIUtils.getContext())
                .load(item.getIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(civ_category_icon);
        List<Tag> tags = item.getTags();
        t=0;
        for (int i=0;i<convertView.getChildCount();i++)
        {

            if (i!=0)
            {

                View childView =  convertView.getChildAt(i);
                if (childView instanceof LinearLayout)
                {
                    k+=t*4;
                    LinearLayout linearLayout= (LinearLayout) childView;
                    for (int j=0;j<linearLayout.getChildCount();j++)
                    {

                        View view =  linearLayout.getChildAt(j);
                        if (view instanceof TextView)
                        {
                            TextView textView = (TextView) view;
                            textView.setText(tags.get(k).getName());
                            k++;
                        }

                    }
                    t++;
                }
                k=0;
            }
        }
    }
}
