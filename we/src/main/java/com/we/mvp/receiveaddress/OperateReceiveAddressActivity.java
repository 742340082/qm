package com.we.mvp.receiveaddress;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.RxBus;
import com.baselibrary.utils.SaveConfigReceiveAddresUtil;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.baselibrary.view.LoadingDialog;
import com.getaddress.AddressActivity;
import com.getaddress.modle.AddressReturnAddress;
import com.we.R;
import com.we.R2;
import com.we.config.ConfigReceiveAddress;
import com.we.config.ConfigUser;
import com.we.mvp.receiveaddress.modle.ReceiveAddress;
import com.we.mvp.receiveaddress.presenter.OperateReveiveAddressPresenter;
import com.we.mvp.receiveaddress.view.OperateReveiveAddressView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by 74234 on 2017/4/23.
 */

public class OperateReceiveAddressActivity extends BaseActivity implements OperateReveiveAddressView {
    @BindView(R2.id.ll_home_number)
    LinearLayout ll_home_number;

    @BindView(R2.id.et_receiveaddress_name)
    EditText et_receiveaddress_name;
    @BindView(R2.id.et_receiveaddress_telephone)
    EditText et_receiveaddress_telephone;
    @BindView(R2.id.tv_receiveaddress_address)
    TextView tv_receiveaddress_address;
    @BindView(R2.id.et_detail_address)
    EditText et_detail_address;
    @BindView(R2.id.et_receiveaddress_host_number)
    EditText et_receiveaddress_host_number;


    @BindView(R2.id.tv_mr)
    TextView tv_mr;
    @BindView(R2.id.tv_lady)
    TextView tv_lady;
    @BindView(R2.id.tv_flyme)
    TextView tv_flyme;
    @BindView(R2.id.tv_company)
    TextView tv_company;
    @BindView(R2.id.tv_school)
    TextView tv_school;

    @BindView(R2.id.tv_address_list)
    TextView tv_address_list;

    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    @BindView(R2.id.btn_sure)
    Button btn_sure;
    private AddressReturnAddress mReturnAddress;
    private LoadingDialog loadingDialog;
    private OperateReveiveAddressPresenter presenter;
    private int mType;
    private ReceiveAddress mReceiveAddress;

    @OnClick(R2.id.tv_receiveaddress_address)
    void openGetAddressActivity() {
        UIUtils.startActivity(this, AddressActivity.class);
    }

    @OnClick(R2.id.btn_sure)
    public void addReceiveAddress() {

        switch (mType) {
            case ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_ADD:
                if (presenter.confirmation()) {
                    presenter.addReceiveAddress();
                }
                break;
            case ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_UPDATE:
                if (presenter.confirmation()) {
                    presenter.updateReceiveAddress(mReceiveAddress.getId().toString());
                }
                break;
        }

    }

    @OnClick(R2.id.tv_address_list)
    void openPhoneList() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("vnd.android.cursor.dir/phone_v2");
        startActivityForResult(intent, ConfigValues.VALUE_OPEN_CONTACTS_LIST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ConfigValues.VALUE_OPEN_CONTACTS_LIST:
                if (data != null) {
                    Uri uri = data.getData();
                    String num = null;
                    // 创建内容解析者
                    ContentResolver contentResolver = getContentResolver();
                    Cursor cursor = contentResolver.query(uri,
                            null, null, null, null);
                    while (cursor.moveToNext()) {
                        num = cursor.getString(cursor.getColumnIndex("data1"));
                    }
                    cursor.close();
                    //  把电话号码中的  -  符号 替换成空格
                    num = num.replaceAll("-", "");
                    //  给 EditText空间设置你选择的联系号码
                    et_receiveaddress_telephone.setText(num);
                }
                break;
        }
    }


    @Override
    public void initView() {
        setSupportActionBar(toolbar);
        loadingDialog = new LoadingDialog(this);
        mType = SaveConfigReceiveAddresUtil.getInt(UIUtils.getContext(), ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_TYOE, -1);
        if (mType != -1) {
            switch (mType) {
                case ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_ADD:
                    loadingDialog.setTitle(UIUtils.getString(R.string.add_loading));
                    toolbar.setTitle(UIUtils.getString(R.string.add_receice_address));
                    break;
                case ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_UPDATE:
                    loadingDialog.setTitle(UIUtils.getString(R.string.update_loading));
                    toolbar.setTitle(UIUtils.getString(R.string.update_receice_address));
                    btn_sure.setText(UIUtils.getString(R.string.ok));
                    ll_home_number.setVisibility(View.GONE);
                    Intent intent = getIntent();
                    mReceiveAddress = (ReceiveAddress) intent.getSerializableExtra(ConfigReceiveAddress.RECEIVEADDRESS_SEND_RECEIVE_ADDRESS);
                    if (mReceiveAddress != null) {
                        et_detail_address.setText(mReceiveAddress.getAddress());
                        et_receiveaddress_name.setText(mReceiveAddress.getName());
                        et_receiveaddress_telephone.setText(mReceiveAddress.getTelephone().toString());
                        tv_receiveaddress_address.setText(mReceiveAddress.getTitle());
                        if (mReceiveAddress.getRoleId() != null) {
                            switch (mReceiveAddress.getRoleId().intValue()) {
                                case 4:
                                    tv_lady.setSelected(true);
                                    break;
                                case 3:
                                    tv_mr.setSelected(true);
                                    break;

                            }
                        }
                        if (mReceiveAddress.getTypeId() != null) {
                            switch (mReceiveAddress.getTypeId().intValue()) {
                                case 1:
                                    tv_flyme.setSelected(true);
                                    break;
                                case 2:
                                    tv_school.setSelected(true);
                                    break;
                                case 3:
                                    tv_company.setSelected(true);
                                    break;

                            }
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void initData() {
        presenter = new OperateReveiveAddressPresenter(this);
        RxBus.getDefault().toObservable(ConfigValues.VALUE_ADDRESS_RX_SEND_RETURNADDRESS, AddressReturnAddress.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AddressReturnAddress>() {
                    @Override
                    public void accept(AddressReturnAddress returnAddress) throws Exception {
                        mReturnAddress = returnAddress;
                        tv_receiveaddress_address.setText(returnAddress.getTitle());
                        et_detail_address.setText(returnAddress.getAddress());
                    }
                });
    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_mr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_mr.setSelected(true);
                tv_lady.setSelected(false);
            }
        });

        tv_lady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_lady.setSelected(true);
                tv_mr.setSelected(false);
            }
        });

        tv_flyme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_flyme.setSelected(true);
                tv_company.setSelected(false);
                tv_school.setSelected(false);
            }
        });

        tv_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_company.setSelected(true);
                tv_flyme.setSelected(false);
                tv_school.setSelected(false);
            }
        });

        tv_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_school.setSelected(true);
                tv_flyme.setSelected(false);
                tv_company.setSelected(false);
            }
        });
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_receiveaddress_add;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mType==ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_UPDATE)
        {
            getMenuInflater().inflate(R.menu.delete_menu,menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.delete_menu) {
            loadingDialog.setTitle(UIUtils.getString(R.string.delete_loading));
            presenter.deleteReceiveAddress(mReceiveAddress.getId().toString());

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getName() {
        String addressName = et_receiveaddress_name.getText().toString().trim();
        String name = addressName.replace(" ", "");
        if (TextUtils.isEmpty(name)) {
            ToastUtils.makeShowToast(this, "联系人不能为空");
            return null;
        }
        return name;
    }

    @Override
    public Long getRoleType() {

        if (tv_mr.isSelected()) {
            return 3l;
        }
        if (tv_lady.isSelected()) {
            return 4l;
        }
        return null;
    }

    @Override
    public Long getType() {
        if (tv_flyme.isSelected()) {
            return 1l;
        }
        if (tv_school.isSelected()) {
            return 2l;
        }
        if (tv_company.isSelected()) {
            return 3l;
        }
        return null;
    }

    public AddressReturnAddress getmReturnAddress() {
        if (mReturnAddress == null) {
            mReturnAddress = new AddressReturnAddress();
            if (mReceiveAddress!=null) {
                mReturnAddress.setCity(mReceiveAddress.getCity());
                mReturnAddress.setProvince(mReceiveAddress.getProvince());
                mReturnAddress.setAddress(mReceiveAddress.getAddress());
                mReturnAddress.setTitle(mReceiveAddress.getTitle());
                mReturnAddress.setLatLng(new LatLonPoint(mReceiveAddress.getLatitude(), mReceiveAddress.getLongitude()));
            }
        }
        return mReturnAddress;
    }

    @Override
    public String getDetailAddress() {
        String detailAddress = et_detail_address.getText().toString().trim();
        if (TextUtils.isEmpty(detailAddress)) {
            ToastUtils.makeShowToast(this, "地址不能为空");
            return null;
        }
        return detailAddress;
    }

    @Override
    public String getVillageAddress() {
        String detailAddress = tv_receiveaddress_address.getText().toString().trim();
        String address = detailAddress.replace(" ", "");
        if (TextUtils.isEmpty(address)) {
            ToastUtils.makeShowToast(this, "地址不能为空");
            return null;
        }
        return address;
    }

    @Override
    public String getHouseNumber() {
        String detailAddress = et_receiveaddress_host_number.getText().toString().trim();
        String houseNumber = detailAddress.replace(" ", "");
        return houseNumber;
    }

    @Override
    public String getAccount() {
        String account = getIntent().getStringExtra(ConfigUser.USER_ACCOUNT);
        if (TextUtils.isEmpty(account)) {
            ToastUtils.makeShowToast(this, "异常");
            return null;
        }
        return account;
    }

    @Override
    public Long getTelephone() {
        String telephone = et_receiveaddress_telephone.getText().toString().trim();
        String phone = telephone.replace(" ", "");
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.makeShowToast(this, "电话号码不能为空");
            return null;
        }
        if (!phone.startsWith("1")) {
            ToastUtils.makeShowToast(this, "电话号码规则错误");
            return null;
        }
        if (phone.length() != 11) {
            ToastUtils.makeShowToast(this, "电话号码规则错误");
            return null;
        }
        return Long.parseLong(phone);
    }

    @Override
    public void loading() {
        loadingDialog.show();
    }

    @Override
    public void success(List<ReceiveAddress> data) {
        loadingDialog.dismiss();
        onBackPressed();
    }


    @Override
    public void error(int errrcode, String message) {
        loadingDialog.dismiss();
        if (errrcode!= ConfigStateCode.STAT_OTHER) {
            ToastUtils.makeShowToast(UIUtils.getContext(), message);
        }
    }

}
