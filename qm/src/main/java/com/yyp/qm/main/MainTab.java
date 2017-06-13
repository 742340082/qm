package com.yyp.qm.main;

import android.support.v4.app.Fragment;

import com.game.GameFragment;
import com.news.NewsFragment;
import com.we.WeFragment;
import com.yyp.qm.R;

public enum MainTab {

//	TAKEOUT(0, R.string.main_tab_name_takeout,R.drawable.main_tab_takeout_selector,new TakeOutFragment()),
	APP(1, R.string.main_tab_name_game,R.drawable.main_tab_game_selector,new GameFragment()),
	NEWS(2,R.string.main_tab_name_news,R.drawable.main_tab_news_selector,new NewsFragment()),
//	FROM(3,R.string.main_tab_name_from,R.drawable.main_tab_from_selector,new DefaultFragment()),
	WE(4,R.string.main_tab_name_we, R.drawable.main_tab_we_selector,new WeFragment());



	public int getId() {
		return id;
	}
	public int getResName() {
		return resName;
	}
	public int getIcon() {
		return icon;
	}
	public Fragment getFragmnet() {
		return defaultFragmnet;
	}
	private int id;
	private int resName;
	private int icon;
	private Fragment defaultFragmnet;
	//id:menu的id
	//resName:menu的名称
	//icon:menu的图标
	//class<?>:menu的改变内容容器的fragment
	private MainTab(int id,int resName,int icon,Fragment defaultFragment)
	{
		this.id=id;
		this.resName=resName;
		this.icon=icon;
		this.defaultFragmnet=defaultFragment;
	}
}
