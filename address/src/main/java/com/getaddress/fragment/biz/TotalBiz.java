package com.getaddress.fragment.biz;

/**
 * Created by 74234 on 2017/4/27.
 */

public interface TotalBiz {
    void  initdata(String query, String city, double longitude, double latitude, int pageNum);
    void loadMore(int pageNum);
}
