package com.game.mvp.category.detail;

import android.support.v4.app.Fragment;

import com.game.R;
import com.game.mvp.category.detail.top.CateoryDetailTopFragment;

/**
 * Created by 74234 on 2017/5/18.
 */

public enum CategoryDetailTopTab {
    REC(0, R.string.game_tab_name_recommend,new CateoryDetailTopFragment()),
    HOT(1, R.string.game_hottest,new CateoryDetailTopFragment()),
    NEW(2, R.string.game_upnew,new CateoryDetailTopFragment());
    public int getId() {
        return id;
    }
    public int getResName() {
        return resName;
    }
    public Fragment getFragmnet() {
        return defaultFragmnet;
    }
    private int id;
    private int resName;
    private Fragment defaultFragmnet;
    //id:menu的id
    //resName:menu的名称
    //icon:menu的图标
    //class<?>:menu的改变内容容器的fragment
    private CategoryDetailTopTab(int id, int resName, Fragment defaultFragment)
    {
        this.id=id;
        this.resName=resName;
        this.defaultFragmnet=defaultFragment;
    }
}
