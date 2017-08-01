package com.baselibrary.api.download;

import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

import com.baselibrary.model.game.Game;

import org.litepal.crud.DataSupport;

import java.io.File;

/**
 * Created by 74234 on 2017/7/20.
 */

public class DownloadInfo extends DataSupport implements Parcelable {
    private String gameId;
    private long currentPosition;
    private String donwloadUrl;
    private long contentLength;
    private int precentNumber;
    private String fileName;
    private String downloadPath;
    private File downloadFile;
    private Game downloadInfo;
    private int downloadState = HttpDownload.STATE_NONE;

    protected DownloadInfo(Parcel in) {
        gameId = in.readString();
        currentPosition = in.readLong();
        donwloadUrl = in.readString();
        contentLength = in.readLong();
        precentNumber = in.readInt();
        fileName = in.readString();
        downloadState = in.readInt();

    }
    public void updateDownloadInfo(DownloadInfo info)
    {
        this.gameId=info.getGameID();
        this.currentPosition=info.getCurrentPosition();
        this.donwloadUrl =info.getDownloadUrl();
        this.contentLength=info.getContentLength();
        this.precentNumber=info.getPrecentNumber();
        this.fileName=info.getFileName();
        this.downloadFile=info.getDownloadFile();
        this.downloadInfo=info.getDownloadInfo();
        this.downloadPath=info.getDownloadPath();
        downloadState=info.getDownloadState();

    }
    public static final Creator<DownloadInfo> CREATOR = new Creator<DownloadInfo>() {
        @Override
        public DownloadInfo createFromParcel(Parcel in) {
            return new DownloadInfo(in);
        }

        @Override
        public DownloadInfo[] newArray(int size) {
            return new DownloadInfo[size];
        }
    };

    public Game getDownloadInfo() {
        return downloadInfo;
    }

    public void setDownloadInfo(Game downloadInfo) {
        this.downloadInfo = downloadInfo;
    }

    public DownloadInfo(String gameId, String donwloadUrl,String appName) {
        this.gameId = gameId;
        this.donwloadUrl = donwloadUrl;
        this.fileName = appName+".apk";
        this.downloadFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        downloadPath=downloadFile.getAbsolutePath();
}

    public DownloadInfo() {
    }

    public void setDonwloadUrl(String donwloadUrl) {
        this.donwloadUrl = donwloadUrl;
    }
    public String getDownloadUrl()
    {
        return donwloadUrl;
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

    public String getDownloadPath()
    {
        return downloadPath;
    }

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
        return donwloadUrl;
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
        downloadPath=downloadFile.getAbsolutePath();
    }

    public void setSaveFilePaht(String filePath, String fileName) {
        this.downloadFile = new File(filePath, fileName);
        downloadPath=downloadFile.getAbsolutePath();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gameId);
        dest.writeLong(currentPosition);
        dest.writeString(donwloadUrl);
        dest.writeLong(contentLength);
        dest.writeInt(precentNumber);
        dest.writeString(fileName);
        dest.writeInt(downloadState);
    }
}
