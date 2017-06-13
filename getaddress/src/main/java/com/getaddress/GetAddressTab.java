package com.getaddress;

import android.support.v4.app.Fragment;

import com.baselibrary.utils.UIUtils;
import com.getaddress.fragment.TotalFragment;


public enum GetAddressTab {

	TOTAL(0, UIUtils.getStringS(R.array.getaddress_search_valeu)[0],new TotalFragment()),
	OFFICE(1,UIUtils.getStringS(R.array.getaddress_search_valeu)[1],new TotalFragment()),
	VILLAGE(2,UIUtils.getStringS(R.array.getaddress_search_valeu)[2],new TotalFragment()),
	SCHOOl(3,UIUtils.getStringS(R.array.getaddress_search_valeu)[3],new TotalFragment());



	public int getId() {
		return id;
	}
	public String getResName() {
		return resName;
	}
	public Fragment getDefaultFragmnet() {
		return defaultFragmnet;
	}
	private int id;
	private String resName;
	private Fragment defaultFragmnet;
	//id:menu的id
	//resName:menu的名称
	//icon:menu的图标
	//class<?>:menu的改变内容容器的fragment
	private GetAddressTab(int id, String resName, Fragment defaultFragment)
	{
		this.id=id;
		this.resName=resName;
		this.defaultFragmnet=defaultFragment;
	}
}
