package com.baselibrary.utils;

import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by 74234 on 2017/8/1.
 */

public class ViewUtil {
    public static void initCutOff(BaseViewHolder helper, int layoutPosition) {
        if(layoutPosition==0)
        {
            View view =  helper.getConvertView();
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams)
            {
                ViewGroup.MarginLayoutParams params= (ViewGroup.MarginLayoutParams) layoutParams;
                params.setMargins(0, UIUtils.dip2px(5),0,UIUtils.dip2px(1));
                view.setLayoutParams(params);
            }
        }else
        {
            View view =  helper.getConvertView();
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams)
            {
                ViewGroup.MarginLayoutParams params= (ViewGroup.MarginLayoutParams) layoutParams;
                params.setMargins(0,0,0,UIUtils.dip2px(1));
                view.setLayoutParams(params);
            }
        }
    }
}
