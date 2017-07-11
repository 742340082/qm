package com.getaddress.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.getaddress.R;
import com.getaddress.modle.AddressCity;

import java.util.List;

/**
 * Created by 74234 on 2017/4/26.
 */

public class CityAdapter extends BaseQuickAdapter<AddressCity,BaseViewHolder> {


    public CityAdapter(int layoutResId, List<AddressCity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressCity item) {
                helper.setText(R.id.tv_address_city,item.getName());

    }
}
