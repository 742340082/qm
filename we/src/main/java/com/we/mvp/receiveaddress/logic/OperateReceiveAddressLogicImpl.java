package com.we.mvp.receiveaddress.logic;


import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.UIUtils;
import com.getaddress.modle.AddressReturnAddress;
import com.we.config.ConfigReceiveAddress;
import com.we.mvp.receiveaddress.modle.ReceiveAddress;
import com.we.mvp.receiveaddress.view.OperateReveiveAddressView;

/**
 * Created by 74234 on 2017/5/4.
 */

public class OperateReceiveAddressLogicImpl  implements OperateReceiveAddressLogic {
    private OperateReveiveAddressView addressView;
    private ReceiveAddress reveiveAddress;
    private  LogicReceiveAddress logicReceiveAddress;

    public OperateReceiveAddressLogicImpl(OperateReveiveAddressView operateReveiveAddressView) {
        addressView=operateReveiveAddressView;
        logicReceiveAddress=new LogicReceiveAddress(operateReveiveAddressView);
    }

    @Override
    public boolean confirmation( ) {
        if (!NetworkState.networkConnected(UIUtils.getContext())) {
            addressView.error(ConfigStateCode.STATE_NO_NETWORK,ConfigStateCode.STATE_NO_NETWORK_VALUE);
            return false;
        }

        String detailAddress = addressView.getDetailAddress();
        String houseNumber = addressView.getHouseNumber();
        Long telephone = addressView.getTelephone();
        String villageAddress = addressView.getVillageAddress();
        String account = addressView.getAccount();
        String name = addressView.getName();
        AddressReturnAddress returnAddress = addressView.getmReturnAddress();
        Long roleType = addressView.getRoleType();
        Long type = addressView.getType();
        if (detailAddress == null || houseNumber == null || telephone == null || villageAddress == null || account == null || name == null) {
            return false;
        }
        reveiveAddress = new ReceiveAddress();
        if(houseNumber!=null)
        {
            detailAddress+="  "+houseNumber;
        }
        reveiveAddress.setAddress(detailAddress);
        reveiveAddress.setCity(returnAddress.getCity());
        reveiveAddress.setProvince(returnAddress.getProvince());
        reveiveAddress.setName(name);
        reveiveAddress.setAccount(account);
        reveiveAddress.setTelephone(telephone);
        reveiveAddress.setTypeId(type);
        reveiveAddress.setRoleId(roleType);
        reveiveAddress.setTitle(returnAddress.getTitle());
        reveiveAddress.setLatitude(returnAddress.getLatLng().getLatitude());
        reveiveAddress.setLongitude(returnAddress.getLatLng().getLongitude());
        return true;
    }

    @Override
    public void addReceiveAddress() {
            logicReceiveAddress.operateReceiveAddress(reveiveAddress.getAccount(),null,reveiveAddress, ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_ADD);

    }

    @Override
    public void deleteReceiveAddress(String receiveAddressId) {
        logicReceiveAddress.operateReceiveAddress(null,receiveAddressId,null, ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_DELETE);
    }

    @Override
    public void updateReceiveAddress(String receiveAddressId) {
        logicReceiveAddress.operateReceiveAddress(null,receiveAddressId,reveiveAddress, ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_UPDATE);

    }


}
