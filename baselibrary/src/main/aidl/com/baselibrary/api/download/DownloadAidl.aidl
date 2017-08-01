// DownloadAidl.aidl
package com.baselibrary.api.download;

// Declare any non-default types here with import statements
import com.baselibrary.api.download.DownloadInfo;
import com.baselibrary.api.download.DownloadObserver;
interface DownloadAidl {
    void download(in DownloadInfo info);
    void install(in DownloadInfo info);
    void pause(in DownloadInfo info);
    void cancle(in DownloadInfo info);
    void registDownloadObserver(in String id,in String tagId,in DownloadObserver downloadObserver);
    void unregistDownloadObserver(in String id,in String tagId);
    void addCacheDownloadInfoS();
    List<DownloadInfo> getDownloadInfos();
    DownloadInfo getDownloadInfo(String gameId);

}
