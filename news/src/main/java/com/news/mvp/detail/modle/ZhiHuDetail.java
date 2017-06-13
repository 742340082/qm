package com.news.mvp.detail.modle;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

public class ZhiHuDetail extends DataSupport
{
    private String body;
    private ArrayList<String> css;
    private String ga_prefix;
    private long id;
    private String image;
    private String image_source;
    private ArrayList<String> images;
    private ArrayList<String> js;
    private String share_url;
    private String title;
    private int type;
    private long zhihudetail_id;
    private long cacheTime;

    public long getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
    }

    public long getZhihudetail_id() {
        return zhihudetail_id;
    }

    public void setZhihudetail_id(long zhihudetail_id) {
        this.zhihudetail_id = zhihudetail_id;
    }

    public String getBody()
    {
        return this.body;
    }

    public ArrayList<String> getCss()
    {
        return this.css;
    }

    public String getGa_prefix()
    {
        return this.ga_prefix;
    }

    public long getId()
    {
        return this.id;
    }

    public String getImage()
    {
        return this.image;
    }

    public String getImage_source()
    {
        return this.image_source;
    }

    public ArrayList<String> getImages()
    {
        return this.images;
    }

    public ArrayList<String> getJs()
    {
        return this.js;
    }

    public String getShare_url()
    {
        return this.share_url;
    }

    public String getTitle()
    {
        return this.title;
    }

    public int getType()
    {
        return this.type;
    }

    public void setBody(String paramString)
    {
        this.body = paramString;
    }

    public void setCss(ArrayList<String> paramArrayList)
    {
        this.css = paramArrayList;
    }

    public void setGa_prefix(String paramString)
    {
        this.ga_prefix = paramString;
    }

    public void setId(long paramInt)
    {
        this.id = paramInt;
    }

    public void setImage(String paramString)
    {
        this.image = paramString;
    }

    public void setImage_source(String paramString)
    {
        this.image_source = paramString;
    }

    public void setImages(ArrayList<String> paramArrayList)
    {
        this.images = paramArrayList;
    }

    public void setJs(ArrayList<String> paramArrayList)
    {
        this.js = paramArrayList;
    }

    public void setShare_url(String paramString)
    {
        this.share_url = paramString;
    }

    public void setTitle(String paramString)
    {
        this.title = paramString;
    }

    public void setType(int paramInt)
    {
        this.type = paramInt;
    }
}
