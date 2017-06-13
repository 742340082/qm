package com.we.mvp.receiveaddress.logic;

import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.UIUtils;
import com.we.config.ConfigReceiveAddress;
import com.we.mvp.receiveaddress.view.ReveiveAddressView;

/**
 * Created by 74234 on 2017/4/23.
 */

public class ReceiveAddressLogicImpl implements ReceiveAddressLogic {

    private ReveiveAddressView receiveAddressView;
    private LogicReceiveAddress logicReceiveAddress;

    public ReceiveAddressLogicImpl(ReveiveAddressView view) {
        receiveAddressView = view;
        logicReceiveAddress=new LogicReceiveAddress(view);
    }

    @Override
    public void initReceiveAddress(String account) {
        if (NetworkState.networkConnected(UIUtils.getContext())) {
            logicReceiveAddress.operateReceiveAddress(account,null,null, ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_SELECT);
        } else {
            receiveAddressView.error(ConfigStateCode.STATE_NO_NETWORK,ConfigStateCode.STATE_NO_NETWORK_VALUE);
        }
    }
}
