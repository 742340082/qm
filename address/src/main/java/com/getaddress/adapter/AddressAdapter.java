package com.getaddress.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.getaddress.R;

import java.util.List;

/**
 * Created by 74234 on 2017/4/26.
 */

public class AddressAdapter extends BaseQuickAdapter<PoiItem,BaseViewHolder> {
    public AddressAdapter(int layoutResId, List<PoiItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PoiItem item) {
        TextView tv_current = helper.getView(R.id.tv_address_current);
        LinearLayout ll_address = helper.getView(R.id.ll_address);
        TextView tv_address_title = helper.getView(R.id.tv_address_title);
        TextView tv_address_detail = helper.getView(R.id.tv_address_detail);
        if (!item.getSnippet().equals("")) {
            ll_address.setVisibility(View.VISIBLE);
            if (helper.getAdapterPosition() == 0) {
                tv_current.setVisibility(View.VISIBLE);
            } else {
                tv_current.setVisibility(View.GONE);
            }
            tv_address_title.setText(item.getTitle());
            if (item.getProvinceName().equals(item.getCityName())) {
                if (item.getCityName().equals(item.getAdName())) {
                    if (item.getAdName().equals(item.getSnippet())) {
                        tv_address_detail.setText(item.getProvinceName() + item.getSnippet());
                    } else {
                        tv_address_detail.setText(item.getProvinceName() + item.getAdName() + item.getSnippet());
                    }
                }else {
                    if (item.getAdName().equals(item.getSnippet())) {
                        tv_address_detail.setText(item.getProvinceName() + item.getSnippet());
                    } else {
                        tv_address_detail.setText(item.getCityName() + item.getAdName() + item.getSnippet());
                    }

                }
            } else {
                if (item.getCityName().equals(item.getAdName()))
                {
                    if (item.getAdName().equals(item.getSnippet()))
                    {
                        tv_address_detail.setText(item.getProvinceName()  + item.getSnippet());
                    }else {
                        tv_address_detail.setText(item.getProvinceName() + item.getAdName() + item.getSnippet());
                    }
                }else {
                    if (item.getAdName().equals(item.getSnippet())) {
                        tv_address_detail.setText(item.getProvinceName() +item.getCityName()+ item.getSnippet());
                    } else {
                        tv_address_detail.setText(item.getProvinceName() + item.getCityName() + item.getAdName() + item.getSnippet());
                    }

                }
            }
        }else
        {
            ll_address.setVisibility(View.GONE);
        }
    }
}
