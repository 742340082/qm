package com.we.mvp.receiveaddress.logic;

/**
 * Created by 74234 on 2017/5/4.
 */

public interface OperateReceiveAddressLogic {
     boolean   confirmation();
    void addReceiveAddress();
    void deleteReceiveAddress(String id);
    void updateReceiveAddress(String id);
}
