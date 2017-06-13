package com.news.mvp.doubian.model;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

/**
 * Created by 74234 on 2017/5/15.
 */

public class DouBianThumbs extends DataSupport{
    private DouBianPosts.medium medium;
    private String description;
    private  DouBianPosts.large large;
    private String tag_name;
    private DouBianPosts.small small;
    @SerializedName("id")
    private  long thumbs_id;
    private long cacheDate;
    private String large_url;
    private String medium_url;
    private String small_url;
    private DouBianPosts posts;

    public DouBianPosts getPosts() {
        return posts;
    }

    public void setPosts(DouBianPosts posts) {
        this.posts = posts;
    }

    public String getLarge_url() {
        return large_url;
    }

    public void setLarge_url(String large_url) {
        this.large_url = large_url;
    }

    public String getMedium_url() {
        return medium_url;
    }

    public void setMedium_url(String medium_url) {
        this.medium_url = medium_url;
    }

    public String getSmall_url() {
        return small_url;
    }

    public void setSmall_url(String small_url) {
        this.small_url = small_url;
    }

    public long getCacheDate() {
        return cacheDate;
    }

    public void setCacheDate(long cacheDate) {
        this.cacheDate = cacheDate;
    }


    public long getThumbs_id() {
        return thumbs_id;
    }

    public void setThumbs_id(long thumbs_id) {
        this.thumbs_id = thumbs_id;
    }

    public DouBianPosts.medium getMedium() {
        return medium;
    }

    public void setMedium(DouBianPosts.medium medium) {
        this.medium = medium;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DouBianPosts.large getLarge() {
        return large;
    }

    public void setLarge(DouBianPosts.large large) {
        this.large = large;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public DouBianPosts.small getSmall() {
        return small;
    }

    public void setSmall(DouBianPosts.small small) {
        this.small = small;
    }

}
