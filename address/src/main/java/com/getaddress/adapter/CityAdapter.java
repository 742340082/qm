package com.getaddress.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.getaddress.R;
import com.getaddress.modle.GetAddressCity;

import java.util.List;

/**
 * Created by 74234 on 2017/4/26.
 */

public class CityAdapter extends BaseQuickAdapter<GetAddressCity,BaseViewHolder> {


    public CityAdapter(int layoutResId, List<GetAddressCity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetAddressCity item) {
                helper.setText(R.id.tv_city,item.getName());

    }
}
