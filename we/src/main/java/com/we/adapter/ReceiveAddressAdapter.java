package com.we.adapter;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baselibrary.utils.ViewUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.we.R;
import com.we.mvp.receiveaddress.modle.ReceiveAddress;

import java.util.List;

/**
 * Created by 74234 on 2017/4/23.
 */

public class ReceiveAddressAdapter extends BaseQuickAdapter<ReceiveAddress,BaseViewHolder> {
    public ReceiveAddressAdapter(int layoutResId, List<ReceiveAddress> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder,final ReceiveAddress item) {
        ViewUtil.initCutOff(holder,holder.getLayoutPosition());
        TextView tv_receiveaddress_name = holder.getView(R.id.tv_receiveaddress_name);
        TextView tv_receiveaddress_role_type = holder.getView(R.id.tv_receiveaddress_role_type);
        TextView tv_receiveaddress_telephone = holder.getView(R.id.tv_receiveaddress_telephone);

        FrameLayout fl_receiveaddress_type = holder.getView(R.id.fl_receiveaddress_type);
        TextView tv_receiveaddress_type_flyme = holder.getView(R.id.tv_receiveaddress_type_flyme);
        TextView tv_receiveaddress_type_school = holder.getView(R.id.tv_receiveaddress_type_school);
        TextView tv_receiveaddress_type_company = holder.getView(R.id.tv_receiveaddress_type_company);


        TextView tv_receiveaddress_address = holder.getView(R.id.tv_receiveaddress_address);

        ImageView iv_receiveaddress_update_info = holder.getView(R.id.iv_receiveaddress_update_info);

        tv_receiveaddress_name.setText(item.getName());
        tv_receiveaddress_telephone.setText(item.getTelephone()+"");
        tv_receiveaddress_address.setText(item.getAddress());
        if(item.getRoleId()!=null)
        {
            tv_receiveaddress_role_type.setVisibility(View.VISIBLE);
            String role_type_content=null;
            switch (item.getRoleId().intValue())
            {
                case 3:
                    role_type_content="先生";
                    break;
                case 4:
                    role_type_content="女士";
                    break;
            }
            tv_receiveaddress_role_type.setText(role_type_content);
        }

        if(item.getTypeId()!=null)
        {
            fl_receiveaddress_type.setVisibility(View.VISIBLE);
            switch (item.getTypeId().intValue())
            {
                case 1:
                    tv_receiveaddress_type_school.setVisibility(View.GONE);
                    tv_receiveaddress_type_company.setVisibility(View.GONE);
                    tv_receiveaddress_type_flyme.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    tv_receiveaddress_type_flyme.setVisibility(View.GONE);
                    tv_receiveaddress_type_company.setVisibility(View.GONE);
                    tv_receiveaddress_type_school.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    tv_receiveaddress_type_flyme.setVisibility(View.GONE);
                    tv_receiveaddress_type_school.setVisibility(View.GONE);
                    tv_receiveaddress_type_company.setVisibility(View.VISIBLE);
                    break;
            }
        }


        iv_receiveaddress_update_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null)
                {
                    listener.click(holder.getLayoutPosition(),item,v);
                }
            }
        });

    }
    private onCLickItemListener listener;

    public void  setOnCLickItemListener(onCLickItemListener listener)
    {
        this.listener=listener;
    }

    public interface  onCLickItemListener
    {
        void click(int position, ReceiveAddress receiveAddress, View view);
    }

}
