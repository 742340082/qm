package com.getaddress.fragment.presenter;


import com.getaddress.fragment.biz.TotalBiz;
import com.getaddress.fragment.biz.TotalBizImpl;
import com.getaddress.fragment.view.TotalView;

/**
 * Created by 74234 on 2017/4/27.
 */

public class TotalPresenter {
    private TotalBiz totalBiz;

    public TotalPresenter(TotalView totalView) {
        totalBiz = new TotalBizImpl(totalView);
    }

    public void initdata(String query, String city, double longitude, double latitude,int pageNum)
    {
        totalBiz.initdata(query,city,longitude,latitude,pageNum);
    }
    public void loadmore(int pageNum)
    {
        totalBiz.loadMore(pageNum);
    }
}
