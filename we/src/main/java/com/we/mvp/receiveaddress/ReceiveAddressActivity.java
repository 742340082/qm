package com.we.mvp.receiveaddress;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.RxBus;
import com.baselibrary.utils.SaveConfigReceiveAddresUtil;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.we.R;
import com.we.R2;
import com.we.adapter.ReceiveAddressAdapter;
import com.we.config.ConfigReceiveAddress;
import com.we.config.ConfigUser;
import com.we.mvp.receiveaddress.modle.ReceiveAddress;
import com.we.mvp.receiveaddress.presenter.ReveiveAddressPresenter;
import com.we.mvp.receiveaddress.view.ReveiveAddressView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by 74234 on 2017/4/22.
 */

public class ReceiveAddressActivity extends BaseActivity implements ReveiveAddressView, OnRetryListener {

    @BindView(R2.id.rl_add_user_receive_address)
    RelativeLayout rl_add_user_receive_address;
    @BindView(R2.id.rv_user_receive_address)
    RecyclerView rv_user_receive_address;
    @BindView(R2.id.rl_user_receive_address)
    RelativeLayout rl_user_receive_address;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    private StatusLayoutManager mStatusLayoutManager;
    private String account;
    private ReveiveAddressPresenter presenter;
    private ReceiveAddressAdapter adapter;
    private int mPosition;

    @OnClick(R2.id.rl_add_user_receive_address)
    void addReceiveAddress() {
        Intent intent = new Intent();
        SaveConfigReceiveAddresUtil.setInt(UIUtils.getContext(), ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_TYOE,
                ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_ADD);
        intent.putExtra(ConfigUser.USER_ACCOUNT, account);
        intent.setClass(this, OperateReceiveAddressActivity.class);
        startActivity(intent);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_user_receiveaddress;
    }


    @Override
    public void initData() {
        presenter = new ReveiveAddressPresenter(this);
        RxBus.getDefault().toObservable(ConfigReceiveAddress.RECEIVEADDRESS_RX_SEND_RECEIVE_ADDRESS, ReceiveAddress.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ReceiveAddress>() {
                    @Override
                    public void accept(ReceiveAddress receiveAddress) throws Exception {
                        int type = SaveConfigReceiveAddresUtil.getInt(UIUtils.getContext(), ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_TYOE, -1);
                        switch (type) {
                            case ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_ADD:
                                if (adapter == null) {
                                    mStatusLayoutManager.showContent();
                                    adapter = new ReceiveAddressAdapter(R.layout.item_receiveaddress, null);
                                    rv_user_receive_address.setLayoutManager(new LinearLayoutManager(ReceiveAddressActivity.this, 1, false));
                                    rv_user_receive_address.setAdapter(adapter);
                                    adapter.openLoadAnimation(ReceiveAddressAdapter.SLIDEIN_LEFT);
                                    adapter.setOnCLickItemListener(new ReceiveAddressAdapter.onCLickItemListener() {
                                        @Override
                                        public void click(int position, ReceiveAddress receiveAddress, View view) {
                                            mPosition = position;
                                            SaveConfigReceiveAddresUtil.setInt(UIUtils.getContext(), ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_TYOE,
                                                    ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_UPDATE);
                                            Intent intent = new Intent();
                                            intent.putExtra(ConfigUser.USER_ACCOUNT, account);
                                            intent.putExtra(ConfigReceiveAddress.RECEIVEADDRESS_SEND_RECEIVE_ADDRESS, (ReceiveAddress) adapter.getItem(position));
                                            intent.setClass(ReceiveAddressActivity.this, OperateReceiveAddressActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                }


                                adapter.getData().add(0, receiveAddress);
                                adapter.notifyItemInserted(0);
                                break;
                            case ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_SELECT:

                                break;
                            case ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_DELETE:
                                adapter.getData().remove(mPosition);
                                adapter.notifyItemRemoved(mPosition);
                                break;
                            case ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_UPDATE:
                                List<ReceiveAddress> data = adapter.getData();
                                data.set(mPosition, receiveAddress);
                                adapter.notifyItemChanged(mPosition);
                                break;
                        }

                    }
                });

        account = getIntent().getStringExtra(ConfigUser.USER_ACCOUNT);
        presenter.initReceiveAddress(account);
    }

    @Override
    public void initView() {

        setSupportActionBar(toolbar);
        mStatusLayoutManager = StatusLayoutManager.newBuilder(this)
                .emptyDataView(R.layout.state_receive_address_empty)
                .errorView(R.layout.state_error)
                .loadingView(R.layout.state_loading)
                .netWorkErrorView(R.layout.state_network_error)
                .retryViewId(R.id.iv_content_error)
                .onRetryListener(this)
                .build();
        rl_user_receive_address.addView(mStatusLayoutManager.getRootLayout(), rl_user_receive_address.getChildCount() - 1);

    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void loading() {
        mStatusLayoutManager.showLoading();
    }

    @Override
    public void error(int errorCode, String message) {
        switch (errorCode) {
            case ConfigStateCode.STATE_ERROE:
                mStatusLayoutManager.showError();
                break;
            case ConfigStateCode.RESULT_RECEIVE_ADDRESS_EMPTY:
                ToastUtils.makeShowToast(this, message);
                mStatusLayoutManager.showEmptyData();
                break;
            case ConfigStateCode.RESULT_RECEIVE_ADDRESS_FAILER:
                ToastUtils.makeShowToast(this, message);
                mStatusLayoutManager.showError();
                break;
            case ConfigStateCode.STATE_NO_NETWORK:
                ToastUtils.makeShowToast(this, message);
                mStatusLayoutManager.showNetWorkError();
                break;
            default:
            if (errorCode!= ConfigStateCode.STAT_OTHER) {
                ToastUtils.makeShowToast(UIUtils.getContext(), message);
            }
        }
    }

    @Override
    public void success(List<ReceiveAddress> addressList) {
        mStatusLayoutManager.showContent();
        adapter = new ReceiveAddressAdapter(R.layout.item_receiveaddress, addressList);
        adapter.openLoadAnimation(ReceiveAddressAdapter.SLIDEIN_LEFT);
        rv_user_receive_address.setLayoutManager(new LinearLayoutManager(ReceiveAddressActivity.this, 1, false));
        rv_user_receive_address.setAdapter(adapter);
        adapter.setOnCLickItemListener(new ReceiveAddressAdapter.onCLickItemListener() {
            @Override
            public void click(int position, ReceiveAddress receiveAddress, View view) {
                mPosition = position;
                SaveConfigReceiveAddresUtil.setInt(UIUtils.getContext(), ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_TYOE,
                        ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_UPDATE);
                Intent intent = new Intent();
                intent.putExtra(ConfigUser.USER_ACCOUNT, account);
                intent.putExtra(ConfigReceiveAddress.RECEIVEADDRESS_SEND_RECEIVE_ADDRESS, receiveAddress);
                intent.setClass(ReceiveAddressActivity.this, OperateReceiveAddressActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void retry() {
        presenter.initReceiveAddress(account);
    }
}