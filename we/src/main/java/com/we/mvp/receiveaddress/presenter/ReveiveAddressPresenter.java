package com.we.mvp.receiveaddress.presenter;


import com.we.mvp.receiveaddress.logic.ReceiveAddressLogic;
import com.we.mvp.receiveaddress.logic.ReceiveAddressLogicImpl;
import com.we.mvp.receiveaddress.view.ReveiveAddressView;

/**
 * Created by 74234 on 2017/4/23.
 */

public class ReveiveAddressPresenter {
    private ReceiveAddressLogic receiveAddressBiz;
    public ReveiveAddressPresenter(ReveiveAddressView view)
    {
        receiveAddressBiz=new ReceiveAddressLogicImpl(view);
    }

    public  void  initReceiveAddress(String account)
    {
        receiveAddressBiz.initReceiveAddress(account);
    }
}
