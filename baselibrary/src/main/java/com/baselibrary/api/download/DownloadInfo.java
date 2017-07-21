package com.baselibrary.api.download;

import android.os.Environment;

import com.baselibrary.utils.SaveConfigGameUtil;
import com.baselibrary.utils.UIUtils;

import org.litepal.crud.DataSupport;

import java.io.File;

/**
 * Created by 74234 on 2017/7/20.
 */

public class DownloadInfo<T> extends DataSupport {
    private String gameId;
    private long currentPosition;
    private String DonwloadUrl;
    private long contentLength;
    private int precentNumber;
    private String fileName;
    private File downloadFile;
    private T downloadInfo;

    public T getDownloadInfo() {
        return downloadInfo;
    }

    public void setDownloadInfo(T downloadInfo) {
        this.downloadInfo = downloadInfo;
    }

    public DownloadInfo(String gameId, String donwloadUrl,String appName) {
        this.gameId = gameId;
        DonwloadUrl = donwloadUrl;
        this.fileName = appName+".apk";
        this.downloadFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        SaveConfigGameUtil.setString(UIUtils.getContext(), gameId, downloadFile.getAbsolutePath());
    }

    public void setDonwloadUrl(String donwloadUrl) {
        DonwloadUrl = donwloadUrl;
    }

    public long getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(long currentPosition) {
        long progresss = 0;
        if (getContentLength() > 0) {
            progresss = currentPosition * 100 / getContentLength();
        }
        this.precentNumber = (int) progresss;
        this.currentPosition = currentPosition;
    }


    private int downloadState = HttpDownload.STATE_NONE;

    public int getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }

    public String getGameID() {
        return gameId;
    }

    public void setGameID(String gameId) {
        this.gameId = gameId;
    }


    public String getDonwloadUrl() {
        return DonwloadUrl;
    }


    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }


    public String getFileName() {
        return fileName;
    }


    public File getDownloadFile() {
        return downloadFile;
    }


    public int getPrecentNumber() {
        return precentNumber;
    }

    public void setSaveFilePath(File saveFile) {
        this.downloadFile = saveFile;
        SaveConfigGameUtil.setString(UIUtils.getContext(), gameId, downloadFile.getAbsolutePath());
    }

    public void setSaveFilePaht(String filePath, String fileName) {
        this.downloadFile = new File(filePath, fileName);
        SaveConfigGameUtil.setString(UIUtils.getContext(), gameId, downloadFile.getAbsolutePath());
    }
}
