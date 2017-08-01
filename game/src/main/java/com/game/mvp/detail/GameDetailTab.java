package com.game.mvp.detail;

import android.support.v4.app.Fragment;

import com.game.R;

/**
 * Created by 74234 on 2017/5/18.
 */

public enum GameDetailTab {
    SUMMARY(0, R.string.game_tab_name_summary,new GameSummaryFragment()),
    COMMENT(1, R.string.game_tab_name_strategy,new GameStrategyFragment());
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
    private GameDetailTab(int id, int resName, Fragment defaultFragment)
    {
        this.id=id;
        this.resName=resName;
        this.defaultFragmnet=defaultFragment;
    }
}
