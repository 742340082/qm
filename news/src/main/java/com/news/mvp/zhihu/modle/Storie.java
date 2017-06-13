package com.news.mvp.zhihu.modle;

import com.baselibrary.view.ISuspensionInterface;
import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 74234 on 2017/5/12.
 */

public class Storie extends DataSupport implements ISuspensionInterface {
    private String ga_prefix;
    @SerializedName("images")
    private List<String> imageList=new ArrayList<>();
    private String title;
    private int type;
    private ZhiHu zhihu;

    @SerializedName("id")
    private long storie_id;





    public Storie() {
    }

    public ZhiHu getZhihu() {
        return zhihu;
    }

    public void setZhihu(ZhiHu zhihu) {
        this.zhihu = zhihu;
    }

    public long getStorie_id() {
        return storie_id;
    }

    public void setStorie_id(long storie_id) {
        this.storie_id = storie_id;
    }

    public String getGa_prefix() {
        return this.ga_prefix;
    }


    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getTitle() {
        return this.title;
    }

    public int getType() {
        return this.type;
    }

    public void setGa_prefix(String paramString) {
        this.ga_prefix = paramString;
    }


    public void setTitle(String paramString) {
        this.title = paramString;
    }

    public void setType(int paramInt) {
        this.type = paramInt;
    }

    @Override
    public boolean isShowSuspension() {
        return false;
    }

    @Override
    public String getSuspensionTag() {
        return null;
    }
}
