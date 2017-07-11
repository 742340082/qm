package com.baselibrary.api.download;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import com.baselibrary.utils.UIUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
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
    private ConcurrentHashMap<String, DownLoadInfo> mDownloadInfoMap = new ConcurrentHashMap<String, DownLoadInfo>();
    // 所有观察者的集合
    private ArrayList<DownloadObserver> mObservers = new ArrayList<DownloadObserver>();
    // 下载任务集合, ConcurrentHashMap是线程安全的HashMap
    private ConcurrentHashMap<String, DownloadTask> mDownloadTaskMap = new ConcurrentHashMap<String, DownloadTask>();


    private HttpDownload() {
    }

    // 2. 注册观察者
    public synchronized void registerObserver(DownloadObserver observer) {
        if (observer != null && !mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    // 3. 注销观察者
    public synchronized void unregisterObserver(DownloadObserver observer) {
        if (observer != null && mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    // 4. 通知下载状态变化
    private synchronized void notifyDownloadStateChanged(final DownLoadInfo info) {
        UIUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                for (DownloadObserver observer : mObservers) {
                    observer.onDownloadStateChanged(info);
                }
            }
        });

    }

    // 5. 通知下载进度变化
    private synchronized void notifyDownloadProgressChanged(final DownLoadInfo info) {
        UIUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                for (DownloadObserver observer : mObservers) {
                    observer.onDownloadProgressChanged(info);
                }
            }
        });
    }

    /**
     * 开始下载
     */
    public synchronized void download(DownLoadInfo info) {
        if (info != null) {
            DownLoadInfo downLoadInfo = mDownloadInfoMap.get(info.getId());
            // 如果downloadInfo不为空,表示之前下载过, 就无需创建新的对象, 要接着原来的下载位置继续下载,也就是断点续传
            if (downLoadInfo != null) {
                info = downLoadInfo;
            }


            // 下载状态更为正在等待
            info.setDownloadState(STATE_WAITING);
            // 通知状态发生变化,各观察者根据此通知更新主界面
            notifyDownloadStateChanged(info);
            // 将下载对象保存在集合中
            mDownloadInfoMap.put(info.getId(), info);

            // 初始化下载任务
            DownloadTask downloadTask = new DownloadTask(info);
            // 启动下载任务
            ThreadManager.ThreadPool threadPool = ThreadManager.getThreadPool();
            threadPool.execute(downloadTask);
            // 将下载任务对象维护在集合当中
            mDownloadTaskMap.put(info.getId(), downloadTask);

        }
    }

    private OkHttpClient httpClient;

    public static HttpDownload getInstance() {
        return new HttpDownload();
    }

    class DownloadTask implements Runnable {
        private DownLoadInfo downloadInfo;

        public DownloadTask(DownLoadInfo downloadInfo) {
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
            if (downloadFile.exists()) {
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
            }
            InputStream in = null;
            RandomAccessFile randomAccessFile = null;
            Request request = new Request.Builder().addHeader("RANGE", "bytes=" + downloadInfo.getCurrentPosition() + "-").url(downloadInfo.getDonwloadUrl()).build();
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
                        downloadInfo.setCurrentPosition(STATE_ERROR);
                        downloadInfo.setCurrentPosition(0);
                        notifyDownloadStateChanged(downloadInfo);
                        // 删除无效文件

                    }
                } else {
                    downloadInfo.setCurrentPosition(STATE_ERROR);
                    downloadInfo.setCurrentPosition(0);
                    notifyDownloadStateChanged(downloadInfo);
                    // 删除无效文件
                }

            } catch (Exception e) {
                e.printStackTrace();
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
            // 不管下载成功,失败还是暂停, 下载任务已经结束,都需要从当前任务集合中移除
            mDownloadTaskMap.remove(downloadInfo.getId());
        }
    }

    public synchronized void pause(DownLoadInfo appInfo) {
        if (appInfo != null) {
            DownLoadInfo downloadInfo = mDownloadInfoMap.get(appInfo.getId());
            if (downloadInfo != null) {
                int state = downloadInfo.getDownloadState();
                // 如果当前状态是等待下载或者正在下载, 需要暂停当前任务
                if (state == STATE_WAITING || state == STATE_DOWNLOAD) {
                    // 停止当前的下载任务
                    DownloadTask downloadTask = mDownloadTaskMap
                            .get(appInfo.getId());
                    if (downloadTask != null) {
                        ThreadManager.getThreadPool().cancel(downloadTask);
                    }

                    // 更新下载状态为暂停
                    downloadInfo.setDownloadState(STATE_PAUSE);
                    notifyDownloadStateChanged(downloadInfo);
                }
            }
        }
    }

    public synchronized void cancle(DownLoadInfo appInfo) {
        if (appInfo != null) {
            DownLoadInfo downloadInfo = mDownloadInfoMap.get(appInfo.getId());
            if (downloadInfo != null) {
                int state = downloadInfo.getDownloadState();
                // 如果当前状态是等待下载或者正在下载, 需要暂停当前任务
                if (state == STATE_WAITING || state == STATE_DOWNLOAD || state == STATE_PAUSE || state == STATE_SUCCESS) {
                    // 停止当前的下载任务
                    DownloadTask downloadTask = mDownloadTaskMap
                            .get(appInfo.getId());
                    if (downloadTask != null) {
                        ThreadManager.getThreadPool().cancel(downloadTask);
                    }
                    File downloadFile = appInfo.getDownloadFile();
                    if (downloadFile.exists()) {
                        downloadFile.delete();
                    }
                    appInfo.setCurrentPosition(0);
                    // 更新下载状态为暂停
                    downloadInfo.setDownloadState(STATE_CANCLE);
                    notifyDownloadStateChanged(downloadInfo);
                }
            }
        }
    }

    public synchronized void install(Context context, DownLoadInfo appInfo) {
        DownLoadInfo downloadInfo = mDownloadInfoMap.get(appInfo.getId());
        if (downloadInfo != null) {
            // 跳到系统的安装页面进行安装
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(appInfo.getDownloadFile());
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }
    public void setOnClickView(View view)
    {
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

    public interface DownloadObserver {
        // 下载状态发生变化
        public void onDownloadStateChanged(DownLoadInfo info);

        // 下载进度发生变化
        public void onDownloadProgressChanged(DownLoadInfo info);
    }



    public static class DownLoadInfo {
        private String id;
        private long currentPosition;
        private String DonwloadUrl;
        private long downLoadSpeed;
        private long lastDownloadPosition;

        public DownLoadInfo() {
        }

        public DownLoadInfo(String id, String donwloadUrl) {
            this.id = id;
            DonwloadUrl = donwloadUrl;
        }

        public void setDonwloadUrl(String donwloadUrl) {
            DonwloadUrl = donwloadUrl;
        }

        public long getCurrentPosition() {
            return currentPosition;
        }

        private void setCurrentPosition(long currentPosition) {
            long progresss = 0;
            if (getContentLength() > 0) {
                progresss = currentPosition * 100 / getContentLength();
            }
            progress = (int) progresss;
            this.currentPosition = currentPosition;
            ThreadManager.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        downLoadSpeed = DownLoadInfo.this.currentPosition - lastDownloadPosition;
                        Thread.sleep(1000);
                        lastDownloadPosition = DownLoadInfo.this.currentPosition;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        public long getDownLoadSpeed() {
            return downLoadSpeed;
        }

        private int downloadState = HttpDownload.STATE_NONE;

        public int getDownloadState() {
            return downloadState;
        }

        private void setDownloadState(int downloadState) {
            this.downloadState = downloadState;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


        public String getDonwloadUrl() {
            return DonwloadUrl;
        }

        private long contentLength;
        private int progress;
        private String fileName;
        private File downloadFile;

        public long getContentLength() {
            return contentLength;
        }

        private void setContentLength(long contentLength) {
            this.contentLength = contentLength;
        }

        public File getDownloadFile() {
            File downFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            return new File(downFile, getFileName());
        }


        public String getFileName() {
            String fileName = DonwloadUrl.substring(DonwloadUrl.lastIndexOf("/"));
            return fileName;
        }

        public int getProgress() {
            return progress;
        }
    }
}
