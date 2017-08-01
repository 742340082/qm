package com.game.mvp.download;

import android.support.v4.app.Fragment;

import com.baselibrary.utils.UIUtils;
import com.game.R;

/**
 * Created by 74234 on 2017/5/18.
 */

public enum GameDetailTab {
    DOWNLOADING(0, R.string.game_tab_name_downloading,new DownloadIngFragment()),
    DOWNLOADSUCCESS(2, R.string.game_tab_name_downloadsuccess,new DownloadSuccessFragment()),
    INSTALLAPP(1, R.string.game_tab_name_installapp,new InstallAppFragment());
    public int getId() {
        return id;
    }
    public String getResName() {
        return resName;
    }
    public Fragment getFragmnet() {
        return defaultFragmnet;
    }
    private int id;
    private String resName;
    private Fragment defaultFragmnet;
    //id:menu的id
    //resName:menu的名称
    //icon:menu的图标
    //class<?>:menu的改变内容容器的fragment
    private GameDetailTab(int id, int resName, Fragment defaultFragment)
    {
        this.id=id;
        this.resName= UIUtils.getString(resName);
        this.defaultFragmnet=defaultFragment;
    }
}
