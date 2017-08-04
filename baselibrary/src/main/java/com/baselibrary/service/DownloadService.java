package com.baselibrary.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.baselibrary.api.download.DownloadAidl;
import com.baselibrary.api.download.DownloadInfo;
import com.baselibrary.api.download.DownloadObserver;
import com.baselibrary.api.download.HttpDownload;
import com.baselibrary.model.game.Game;
import com.baselibrary.utils.UIUtils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.List;

import static com.baselibrary.api.download.HttpDownload.getDownloadFile;

/**
 * Created by 74234 on 2017/7/22.
 */

public class DownloadService extends Service {

    private HttpDownload httpDownload;
    private DownloadBinder binder;

    public class DownloadBinder extends DownloadAidl.Stub {


        @Override
        public void download(DownloadInfo info) throws RemoteException {
            httpDownload.download(info);
        }

        @Override
        public void install(DownloadInfo info) throws RemoteException {
            httpDownload.install(UIUtils.getContext(), info);
        }

        @Override
        public void pause(DownloadInfo info) throws RemoteException {
            httpDownload.pause(info);
        }

        @Override
        public void cancle(DownloadInfo info) throws RemoteException {
            httpDownload.cancle(info);
        }

        @Override
        public void registDownloadObserver(String id, String tagId, DownloadObserver downloadObserver) throws RemoteException {
            httpDownload.registerObserver(id, tagId, downloadObserver);
        }

        @Override
        public void unregistDownloadObserver(String id, String tagId) throws RemoteException {
            httpDownload.unregisterObserver(id, tagId);
        }

        @Override
        public void addCacheDownloadInfoS() throws RemoteException {

            httpDownload.addCacheDownloadInfo(initDownloadInfos());
        }

        @Override
        public List<DownloadInfo> getDownloadInfos() throws RemoteException {
            return initDownloadInfos();
        }

        @Override
        public DownloadInfo getDownloadInfo(String gameId) throws RemoteException {
            DownloadInfo downloadInfo = DataSupport.where(new String[]{"gameId=?", gameId}).findFirst(DownloadInfo.class);
            if (downloadInfo!=null)
            {
                Game game = DataSupport.where(new String[]{"game_id=? and downurl=?", downloadInfo.getGameID(), downloadInfo.getDonwloadUrl()}).findFirst(Game.class);
                downloadInfo.setContentLength(Long.parseLong(game.getSize_byte()));
                if (game != null) {
                    File downloadFile = getDownloadFile(UIUtils.getContext(), Integer.parseInt(game.getGame_id()));
                    if (downloadFile != null && downloadFile.exists()) {
                        downloadInfo.setCurrentPosition(downloadFile.length());
                        downloadInfo.setSaveFilePath(downloadFile);
                    } else {
                        downloadInfo.setCurrentPosition(0);
                    }
                }
                downloadInfo.setDownloadInfo(game);
            }

            return downloadInfo;
        }

    }

    private List<DownloadInfo> initDownloadInfos() {
        List<DownloadInfo> downloadInfos = DataSupport.where("1=1").find(DownloadInfo.class);
        for (int i = 0; i < downloadInfos.size(); i++) {
            DownloadInfo downloadInfo = downloadInfos.get(i);
            Game game = DataSupport.where(new String[]{"game_id=? and downurl=?", downloadInfo.getGameID(), downloadInfo.getDonwloadUrl()}).findFirst(Game.class);
                downloadInfo.setContentLength(Long.parseLong(game.getSize_byte()));
                if (game != null) {
                    File downloadFile = getDownloadFile(UIUtils.getContext(), Integer.parseInt(game.getGame_id()));
                    if (downloadFile != null && downloadFile.exists()) {
                        downloadInfo.setCurrentPosition(downloadFile.length());
                        downloadInfo.setSaveFilePath(downloadFile);
                    } else {
                        downloadInfo.setCurrentPosition(0);
                    }
            }

            downloadInfo.setDownloadInfo(game);
            downloadInfos.set(i, downloadInfo);
        }
        return downloadInfos;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        httpDownload = HttpDownload.getInstance();
        binder = new DownloadBinder();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

}
