package com.game;

import android.support.v4.app.Fragment;

import com.game.mvp.category.CategoryFragment;
import com.game.mvp.index.IndexFragment;
import com.game.mvp.top.TopFragment;

/**
 * Created by 74234 on 2017/5/18.
 */

public enum GameTab {
    RECOMMEND(0, R.string.game_tab_name_recommend,new IndexFragment()),
    CATEGORY(1, R.string.game_tab_name_category,new CategoryFragment()),
    TOP(2,R.string.game_tab_name_top,new TopFragment());
//    CONSULTATION(3,R.string.game_tab_name_consultation,new DefaultFragment());
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
    private GameTab(int id,int resName,Fragment defaultFragment)
    {
        this.id=id;
        this.resName=resName;
        this.defaultFragmnet=defaultFragment;
    }
}
