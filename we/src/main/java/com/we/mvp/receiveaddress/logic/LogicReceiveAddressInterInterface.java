package com.we.mvp.receiveaddress.logic;


import com.we.mvp.receiveaddress.modle.ReceiveAddress;

/**
 * Created by 74234 on 2017/5/6.
 */

public interface LogicReceiveAddressInterInterface {
    void operateReceiveAddress(String account, String receiveaddressId, ReceiveAddress receiveAddress, int operateReceiveaAddressType);
}
