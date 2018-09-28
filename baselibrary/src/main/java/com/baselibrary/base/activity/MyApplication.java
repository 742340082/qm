package com.baselibrary.base.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;

import com.baselibrary.api.download.DownloadAidl;
import com.baselibrary.service.DownloadService;

import org.litepal.LitePalApplication;

public class MyApplication
        extends LitePalApplication {
    private static int UiThreadId;
    private static Context mContext;
    private static Handler mHandler;
    private static DownloadAidl downloadAidl;

    public static DownloadAidl getDownloadAidl() {
        return downloadAidl;
    }

    public static Context getContext() {
        return mContext;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static int getUiThreadId() {
        return UiThreadId;
    }

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (downloadAidl == null) {
                return;
            }
            downloadAidl.asBinder().unlinkToDeath(deathRecipient, 0);
            downloadAidl = null;
        }

    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            downloadAidl = DownloadAidl.Stub.asInterface(service);
            try {
                downloadAidl.addCacheDownloadInfoS();
                service.linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        mContext = getApplicationContext();
        UiThreadId = Process.myUid();


        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }
}
