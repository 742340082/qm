package com.we.mvp.receiveaddress.presenter;


import com.we.mvp.receiveaddress.logic.OperateReceiveAddressLogic;
import com.we.mvp.receiveaddress.logic.OperateReceiveAddressLogicImpl;
import com.we.mvp.receiveaddress.view.OperateReveiveAddressView;

/**
 * Created by 74234 on 2017/5/4.
 */
public class OperateReveiveAddressPresenter {
    private OperateReceiveAddressLogic addReceiveAddressBiz;
    public OperateReveiveAddressPresenter(OperateReveiveAddressView view)
    {
        addReceiveAddressBiz=new OperateReceiveAddressLogicImpl(view);
    }
    public boolean   confirmation()
    {
       return addReceiveAddressBiz.confirmation();
    }
    public void addReceiveAddress()
    {
        addReceiveAddressBiz.addReceiveAddress();
    }
    public  void deleteReceiveAddress(String receiveAddressId)
    {
        addReceiveAddressBiz.deleteReceiveAddress(receiveAddressId);
    }
    public  void updateReceiveAddress(String receiveAddressId)
    {
        addReceiveAddressBiz.updateReceiveAddress(receiveAddressId);
    }
}
