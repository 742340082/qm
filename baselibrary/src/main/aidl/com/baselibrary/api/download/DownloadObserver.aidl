// DownloadObserver.aidl
package com.baselibrary.api.download;

// Declare any non-default types here with import statements
import com.baselibrary.api.download.DownloadInfo;
interface DownloadObserver {

     void onDownloadStateChanged(in DownloadInfo info);


     void onDownloadProgressChanged(in DownloadInfo info);
}
