package com.game.mvp.top.total.presenter;

import com.game.mvp.top.total.biz.TotalBiz;
import com.game.mvp.top.total.biz.TotalBizImpl;
import com.game.mvp.top.total.view.TotalView;

/**
 * Created by 74234 on 2017/6/12.
 */

public class TotalPresenter {

    private final TotalBiz totalBiz;

    public TotalPresenter(TotalView totalView)
    {
        totalBiz = new TotalBizImpl(totalView);
    }
    public  void initGameTopTotal(int page,String topType)
    {
        totalBiz.initGameTopTotal(page,topType);
    }
}
