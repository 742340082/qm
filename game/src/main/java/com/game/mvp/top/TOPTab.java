package com.game.mvp.top;

import android.support.v4.app.Fragment;

import com.game.R;
import com.game.mvp.top.total.TotalFragment;

/**
 * Created by 74234 on 2017/5/18.
 */

public enum TOPTab {
    NEW(0, R.string.game_new,new TotalFragment()),
    NET(1, R.string.game_net,new TotalFragment()),
    SINGLE(2, R.string.game_single,new TotalFragment()),
    HOT(3, R.string.game_hot,new TotalFragment());
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
    private TOPTab(int id, int resName, Fragment defaultFragment)
    {
        this.id=id;
        this.resName=resName;
        this.defaultFragmnet=defaultFragment;
    }
}
