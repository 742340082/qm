package com.news.mvp.doubian.model;

import java.util.ArrayList;

public class DouBian
{
    private int count;
    private String date;
    private int offset;
    private ArrayList<DouBianPosts> posts;
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

    public ArrayList<DouBianPosts> getPosts()
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

    public void setPosts(ArrayList<DouBianPosts> paramArrayList)
    {
        this.posts = paramArrayList;
    }

    public void setTotal(int paramInt)
    {
        this.total = paramInt;
    }


}
