package com.getaddress.adapter;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselibrary.utils.Logger;
import com.baselibrary.utils.UIUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.getaddress.R;
import com.getaddress.modle.AddressTipMapLocation;

import java.util.List;

/**
 * Created by 74234 on 2017/4/30.
 */

public class AddressSearchAdapter extends BaseQuickAdapter<AddressTipMapLocation,BaseViewHolder> {
    public AddressSearchAdapter(int layoutResId, List<AddressTipMapLocation> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressTipMapLocation item) {
        Logger.i("TAG",item.toString());
        TextView tv_address_title = helper.getView(R.id.tv_address_title);
        TextView tv_address_detail = helper.getView(R.id.tv_address_detail);
        RelativeLayout rl_address = helper.getView(R.id.rl_address);
        String name = item.getTip().getName();
        String searchText = item.getSearchText();

            if (name.contains(searchText)) {
                int indexOf = name.indexOf(searchText);
                SpannableStringBuilder builder = new SpannableStringBuilder(name);
                ForegroundColorSpan blueSpan = new ForegroundColorSpan(UIUtils.getColor(R.color.blue));
                builder.setSpan(blueSpan, indexOf, indexOf + searchText.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                tv_address_title.setText(builder);
            } else {
                tv_address_title.setText(name);
            }

            tv_address_detail.setText(item.getTip().getDistrict() + item.getTip().getAddress());


    }
}
