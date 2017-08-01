package com.game.mvp.download.presenter;

import com.game.mvp.download.biz.DownloadBiz;
import com.game.mvp.download.biz.DownloadBizImpl;
import com.game.mvp.download.view.DownloadIngView;
import com.game.mvp.download.view.DownloadSuccessView;
import com.game.mvp.download.view.InstallAppView;

/**
 * Created by 74234 on 2017/7/31.
 */

public class DownloadPresenter {
    private DownloadBiz downloadBiz;
    public DownloadPresenter(DownloadIngView downloadIngView)
    {
        downloadBiz=new DownloadBizImpl(downloadIngView,null,null);
    }
    public DownloadPresenter(DownloadSuccessView downloadIngView)
    {
        downloadBiz=new DownloadBizImpl(null,downloadIngView,null);
    }
    public DownloadPresenter(InstallAppView installAppView)
    {
        downloadBiz=new DownloadBizImpl(null,null,installAppView);
    }

    public void initInstallAPP()
    {
        downloadBiz.initInstallAPP();
    }
    public void initDownloadIng()
    {
        downloadBiz.initDownloadIng();
    }
    public void initDownloadSuccess()
    {
        downloadBiz.initDownloadSuccess();
    }
}
