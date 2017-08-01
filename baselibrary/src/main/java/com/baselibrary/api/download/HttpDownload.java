package com.baselibrary.api.download;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.RemoteException;
import android.view.View;

import com.baselibrary.utils.StringUtil;
import com.baselibrary.utils.UIUtils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.System.out;

/**
 * Created by yangyupan on 2017/2/23.
 */

public class HttpDownload {
    public static final int STATE_NONE = 0;// 下载未开始
    public static final int STATE_WAITING = 1;// 等待下载
    public static final int STATE_DOWNLOAD = 2;// 正在下载
    public static final int STATE_PAUSE = 3;// 下载暂停
    public static final int STATE_ERROR = 4;// 下载失败
    public static final int STATE_SUCCESS = 5;// 下载成功
    public static final int STATE_CANCLE = 6;// 取消下载

    // 下载对象的集合, ConcurrentHashMap是线程安全的HashMap
    private ConcurrentHashMap<String, DownloadInfo> mDownloadInfoMap = new ConcurrentHashMap<>();
    // 所有观察者的集合
    private ConcurrentHashMap<String, ConcurrentHashMap<String,DownloadObserver>> mObservers = new ConcurrentHashMap<>();
    //每个id对应一组的观察者集合
    private ConcurrentHashMap<String,DownloadObserver> mDownloadObserverS;
    // 下载任务集合, ConcurrentHashMap是线程安全的HashMap
    private ConcurrentHashMap<String, DownloadTask> mDownloadTaskMap = new ConcurrentHashMap<>();
    private final ThreadManager.ThreadPool mThreadPool;

    private HttpDownload() {
        mThreadPool = ThreadManager.getThreadPool();
    }

    // 2. 注册观察者

    /**
     * 注册观察者
     *
     * @param id       唯一标识每个
     * @param observer
     */
    public synchronized void registerObserver(String id,String tagId, DownloadObserver observer) {
        Set<String> keySet = mObservers.keySet();
        if (!keySet.contains(tagId)) {
            mDownloadObserverS=new ConcurrentHashMap();

            mDownloadObserverS.put(id,observer);
            mObservers.put(tagId, mDownloadObserverS);
        } else {
            ConcurrentHashMap<String, DownloadObserver> observerConcurrentHashMap = mObservers.get(tagId);
            observerConcurrentHashMap.put(id,observer);
        }

    }

    public ConcurrentHashMap<String, DownloadInfo> getmDownloadInfoMap()
    {
        return mDownloadInfoMap;
    }

    // 3. 注销一组观察者
    public synchronized void unregisterObserverSet(String tagId) {
        mObservers.remove(tagId);
    }
    // 3. 注销观察者
    public synchronized void unregisterObserver(String id,String tagId) {
        ConcurrentHashMap<String, DownloadObserver> concurrentHashMap = mObservers.get(tagId);
        concurrentHashMap.remove(id);
    }

    // 4. 通知下载状态变化
    private synchronized void notifyDownloadStateChanged(final DownloadInfo info) {
        info.saveOrUpdate(new String[]{"gameId=?", info.getGameID()});
        //找到下载这个游戏的一组观察者
        ConcurrentHashMap<String, DownloadObserver> concurrentHashMap = mObservers.get(info.getGameID());

        if (concurrentHashMap!=null&&concurrentHashMap.size()>0) {
            for (Map.Entry<String, DownloadObserver> observer : concurrentHashMap.entrySet()) {
                //注册观察者id和一组观察中相同就通知
                final DownloadObserver value = observer.getValue();

                UIUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            value.onDownloadStateChanged(info);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }


    // 5. 通知下载进度变化

    private synchronized void notifyDownloadProgressChanged(final DownloadInfo info) {

        //找到下载这个游戏的一组观察者
        ConcurrentHashMap<String, DownloadObserver> concurrentHashMap = mObservers.get(info.getGameID());
        if (concurrentHashMap!=null&&concurrentHashMap.size()>0) {
            for (Map.Entry<String, DownloadObserver> observer : concurrentHashMap.entrySet()) {
                //注册观察者id和一组观察中相同就通知
                final DownloadObserver value = observer.getValue();

                UIUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            value.onDownloadProgressChanged(info);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    public void  addCacheDownloadInfo(List<DownloadInfo> downloadInfos)
    {
        for (DownloadInfo downloadInfo:downloadInfos)
        {
            mDownloadInfoMap.put(downloadInfo.getGameID(),downloadInfo);
        }

    }
    /**
     * 开始下载
     */

    public synchronized void download(DownloadInfo info) {
        if (info != null) {
            DownloadInfo downLoadInfo = mDownloadInfoMap.get(info.getGameID());
            // 如果downloadInfo不为空,表示之前下载过, 就无需创建新的对象, 要接着原来的下载位置继续下载,也就是断点续传
            if (downLoadInfo != null) {
                info = downLoadInfo;

            }

            if (DataSupport.where(new String[]{"gameId=?", info.getGameID()}).findFirst(DownloadInfo.class) == null) {
                info.save();
            }
            // 下载状态更为正在等待
            info.setDownloadState(STATE_WAITING);
            // 通知状态发生变化,各观察者根据此通知更新主界面
            notifyDownloadStateChanged(info);
            // 将下载对象保存在集合中
            mDownloadInfoMap.put(info.getGameID(), info);

            // 初始化下载任务
            DownloadTask downloadTask = new DownloadTask(info);
            // 启动下载任务
            mThreadPool.execute(downloadTask);
            // 将下载任务对象维护在集合当中
            mDownloadTaskMap.put(info.getGameID(), downloadTask);

        }
    }

    private OkHttpClient httpClient;
    private volatile static HttpDownload httpDownload;

    public static HttpDownload getInstance() {
        synchronized (HttpDownload.class) {
            if (httpDownload == null) {
                httpDownload = new HttpDownload();
            }
        }
        return httpDownload;
    }

    class DownloadTask implements Runnable {
        private DownloadInfo downloadInfo;

        public DownloadTask(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
            httpClient = new OkHttpClient();
        }

        @Override
        public void run() {
            // 更新下载状态
            long total = 0;
            downloadInfo.setDownloadState(STATE_DOWNLOAD);
            notifyDownloadStateChanged(downloadInfo);

            File downloadFile = downloadInfo.getDownloadFile();
            if (downloadFile != null && downloadFile.exists()) {
                downloadInfo.setCurrentPosition(downloadFile.length());
                total = downloadFile.length();
            }
            long contentLength = getContentLength(downloadInfo.getDonwloadUrl());
            downloadInfo.setContentLength(contentLength);
            if (contentLength == 0) {
                downloadInfo.setDownloadState(STATE_ERROR);
                notifyDownloadStateChanged(downloadInfo);
            }
            if (downloadInfo.getCurrentPosition() == contentLength) {
                downloadInfo.setDownloadState(STATE_SUCCESS);
                notifyDownloadStateChanged(downloadInfo);
            }else
            {
                InputStream in = null;
                RandomAccessFile randomAccessFile = null;
                Request request = new Request.Builder().addHeader("RANGE", "bytes=" + downloadInfo.getCurrentPosition() + "-")
                        .url(downloadInfo.getDonwloadUrl()).build();
                try {
                    Response response = httpClient.newCall(request).execute();
                    randomAccessFile = new RandomAccessFile(downloadFile, "rw");
                    randomAccessFile.seek(downloadInfo.getCurrentPosition());
                    if (response != null && response.isSuccessful()) {

                        int len = 0;
                        byte[] buffer = new byte[1024];
                        in = response.body().byteStream();
                        while ((len = in.read(buffer)) != -1
                                && downloadInfo.getDownloadState() == STATE_DOWNLOAD) {// 只有在下载的状态才读取文件,如果状态变化,就立即停止读写文件
                            randomAccessFile.write(buffer, 0, len);
                            out.flush();
                            total += len;
                            downloadInfo.setCurrentPosition(total);
                            notifyDownloadProgressChanged(downloadInfo);


                        }
                        // 下载结束, 判断文件是否完整
                        if (randomAccessFile.length() == downloadInfo.getContentLength()) {
                            // 下载完毕
                            downloadInfo.setDownloadState(STATE_SUCCESS);
                            notifyDownloadStateChanged(downloadInfo);
                        } else if (downloadInfo.getDownloadState() == STATE_CANCLE) {
                            //取消下载
                            notifyDownloadStateChanged(downloadInfo);
                        } else if (downloadInfo.getDownloadState() == STATE_PAUSE) {
                            // 中途暂停
                            notifyDownloadStateChanged(downloadInfo);
                        } else {
                            // 下载失败
                            downloadInfo.setDownloadState(STATE_ERROR);
                            downloadInfo.setCurrentPosition(0);
                            notifyDownloadStateChanged(downloadInfo);
                            // 删除无效文件
                            File file = downloadInfo.getDownloadFile();
                            if (file!=null&&file.exists()) {
                                file.delete();
                            }
                        }
                    } else {
                        downloadInfo.setDownloadState(STATE_ERROR);
                        downloadInfo.setCurrentPosition(0);
                        notifyDownloadStateChanged(downloadInfo);
                        // 删除无效文件
                        File file = downloadInfo.getDownloadFile();
                        if (file!=null&&file.exists()) {
                            file.delete();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    cancle(downloadInfo);
                } finally {
                    if (randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            // 不管下载成功,失败还是暂停, 下载任务已经结束,都需要从当前任务集合中移除
            mDownloadTaskMap.remove(downloadInfo.getGameID());
        }

    }

    public synchronized void pause(DownloadInfo appInfo) {
        if (appInfo != null) {
            DownloadInfo downloadInfo = mDownloadInfoMap.get(appInfo.getGameID());
            if (downloadInfo != null) {
                int state = downloadInfo.getDownloadState();
                // 如果当前状态是等待下载或者正在下载, 需要暂停当前任务
                if (state == STATE_WAITING || state == STATE_DOWNLOAD) {
                    // 停止当前的下载任务
                    DownloadTask downloadTask = mDownloadTaskMap
                            .get(appInfo.getGameID());
                    if (downloadTask != null) {
                        mThreadPool.cancel(downloadTask);
                    }

                    // 更新下载状态为暂停
                    downloadInfo.setDownloadState(STATE_PAUSE);
                    notifyDownloadStateChanged(downloadInfo);
                }
            }
        }
    }

    public synchronized void cancle(DownloadInfo info) {
        if (info != null) {
            DownloadInfo downloadInfo = mDownloadInfoMap.get(info.getGameID());
            if (downloadInfo != null) {
                int state = downloadInfo.getDownloadState();
                // 如果当前状态是等待下载或者正在下载, 需要暂停当前任务
                if (state == STATE_WAITING || state == STATE_DOWNLOAD ||
                        state == STATE_PAUSE || state == STATE_SUCCESS || state == STATE_ERROR) {
                    // 停止当前的下载任务
                    DownloadTask downloadTask = mDownloadTaskMap
                            .get(downloadInfo.getGameID());
                    if (downloadTask != null) {
                        mThreadPool.cancel(downloadTask);
                    }
                    File downloadFile = downloadInfo.getDownloadFile();
                    if (downloadFile!=null&&downloadFile.exists()) {
                        downloadFile.delete();
                    }
                    downloadInfo.setCurrentPosition(0);

                    // 更新下载状态为取消
                    downloadInfo.setDownloadState(STATE_CANCLE);
                    notifyDownloadStateChanged(downloadInfo);

                    downloadInfo.setDownloadInfo(null);
                    downloadInfo.delete();
                }
            }
        }
    }

    public ConcurrentHashMap<String, DownloadInfo> getDownloadInfoMap() {
        return mDownloadInfoMap;
    }


    public synchronized void install(Context context, DownloadInfo appInfo) {
        // 跳到系统的安装页面进行安装
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(appInfo.getDownloadFile());
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public void setOnClickView(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    public long getContentLength(String downloadUrl) {
        Request request = new Request.Builder().url(downloadUrl).get().build();
        try {
            Response response = httpClient.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                response.close();
                return contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static File getDownloadFile(Context context, int id) {

        DownloadInfo downloadInfo = DataSupport.where("gameId=?", id + "").findFirst(DownloadInfo.class);
        if (!StringUtil.isEmpty(downloadInfo.getDownloadPath())) {
            File file = new File(downloadInfo.getDownloadPath());
            if (file.exists()) {
                return file;
            }
        }

        return null;

    }


}
