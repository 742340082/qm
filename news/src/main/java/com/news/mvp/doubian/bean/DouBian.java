package com.news.mvp.doubian.bean;

import java.util.ArrayList;

public class DouBian
{
    private int count;
    private String date;
    private int offset;
    private ArrayList<Posts> posts;
    private int total;

    public int getCount()
    {
        return this.count;
    }

    public String getDate()
    {
        return this.date;
    }

    public int getOffset()
    {
        return this.offset;
    }

    public ArrayList<Posts> getPosts()
    {
        return this.posts;
    }

    public int getTotal()
    {
        return this.total;
    }

    public void setCount(int paramInt)
    {
        this.count = paramInt;
    }

    public void setDate(String paramString)
    {
        this.date = paramString;
    }

    public void setOffset(int paramInt)
    {
        this.offset = paramInt;
    }

    public void setPosts(ArrayList<Posts> paramArrayList)
    {
        this.posts = paramArrayList;
    }

    public void setTotal(int paramInt)
    {
        this.total = paramInt;
    }


}
