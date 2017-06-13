package com.we.mvp.receiveaddress.view;


import com.baselibrary.BaseView;
import com.getaddress.modle.ReturnAddress;
import com.we.mvp.receiveaddress.modle.ReceiveAddress;

import java.util.List;

/**
 * Created by 74234 on 2017/4/23.
 */

public interface OperateReveiveAddressView extends BaseView<List<ReceiveAddress>> {
    String getName();
    Long getRoleType();
    Long getType();
    ReturnAddress getmReturnAddress();
    String getDetailAddress();
    String getVillageAddress();
    String getHouseNumber();
    String getAccount();
    Long getTelephone();
}
