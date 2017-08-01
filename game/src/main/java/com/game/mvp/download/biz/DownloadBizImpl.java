package com.game.mvp.download.biz;

import android.os.RemoteException;

import com.baselibrary.api.download.DownloadAidl;
import com.baselibrary.api.download.DownloadInfo;
import com.baselibrary.api.download.HttpDownload;
import com.baselibrary.base.activity.MyApplication;
import com.game.mvp.download.view.DownloadIngView;
import com.game.mvp.download.view.DownloadSuccessView;
import com.game.mvp.download.view.InstallAppView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 74234 on 2017/7/31.
 */

public class DownloadBizImpl implements DownloadBiz {
    private DownloadIngView mDownloadView;
    private DownloadSuccessView mDownloadSuccessView;
    private InstallAppView mInstallAppView;
    private DownloadAidl mDownloadAidl;

    public DownloadBizImpl(DownloadIngView downloadView,DownloadSuccessView downloadSuccessView,InstallAppView installAppView) {
        this.mDownloadView = downloadView;
        this.mDownloadSuccessView=downloadSuccessView;
        this.mInstallAppView=installAppView;
        mDownloadAidl = MyApplication.getDownloadAidl();
    }

    @Override
    public void initInstallAPP() {

    }

    @Override
    public void initDownloadIng() {

        List<DownloadInfo> downloadIngInfos=new ArrayList<>();
        try {
            List<DownloadInfo> downloadInfos = mDownloadAidl.getDownloadInfos();
            for (DownloadInfo downloadInfo :downloadInfos)
            {
                if (downloadInfo.getDownloadState()!= HttpDownload.STATE_SUCCESS)
                {
                    downloadIngInfos.add(downloadInfo);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mDownloadView.success(downloadIngInfos);
    }

    @Override
    public void initDownloadSuccess() {
        List<DownloadInfo> downloadSuccessInfos=new ArrayList<>();
        try {
            List<DownloadInfo> downloadInfos = mDownloadAidl.getDownloadInfos();
            for (DownloadInfo downloadInfo :downloadInfos)
            {
                if (downloadInfo.getDownloadState()== HttpDownload.STATE_SUCCESS)
                {
                    downloadSuccessInfos.add(downloadInfo);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mDownloadSuccessView.success(downloadSuccessInfos);
    }
}
