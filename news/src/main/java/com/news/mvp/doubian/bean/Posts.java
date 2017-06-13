package com.news.mvp.doubian.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.news.adapter.NewDouBianContentAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

public class Posts
        extends DataSupport
        implements MultiItemEntity {

    @SerializedName("abstract")
    private String abs;
    private int app_css;
    private String column;
    private int comments_count;
    private String created_time;
    private String date;
    private long cacheDate;
    private String description;
    private String large_url;
    private String medium_url;
    private String small_url;
    private ArrayList<Thumbs> thumbs;
    private DouBian douBian;

    public DouBian getDouBian() {
        return douBian;
    }

    public void setDouBian(DouBian douBian) {
        this.douBian = douBian;
    }

    private author author;
    private int display_style;
    @SerializedName("id")
    private long posts_id;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getPosts_id() {
        return posts_id;
    }

    public void setPosts_id(long posts_id) {
        this.posts_id = posts_id;
    }

    private boolean is_editor_choice;
    private boolean is_liked;
    private int like_count;
    private String published_time;
    private String share_pic_url;
    private String short_url;
    private String title;
    private String type;
    private String url;

    public long getCacheDate() {
        return cacheDate;
    }

    public void setCacheDate(long cacheDate) {
        this.cacheDate = cacheDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public ArrayList<Thumbs> getThumbs() {
        return thumbs;
    }

    public void setThumbs(ArrayList<Thumbs> thumbs) {
        this.thumbs = thumbs;
    }

    public Posts.author getAuthor() {
        return author;
    }

    public void setAuthor(Posts.author author) {
        this.author = author;
    }

    public Posts() {
    }

    public String getAbs() {
        return this.abs;
    }

    public int getApp_css() {
        return this.app_css;
    }


    public String getColumn() {
        return this.column;
    }

    public int getComments_count() {
        return this.comments_count;
    }

    public String getCreated_time() {
        return this.created_time;
    }

    public String getDate() {
        return this.date;
    }

    public int getDisplay_style() {
        return this.display_style;
    }


    public int getLike_count() {
        return this.like_count;
    }

    public String getPublished_time() {
        return this.published_time;
    }

    public String getShare_pic_url() {
        return this.share_pic_url;
    }

    public String getShort_url() {
        return this.short_url;
    }


    public String getTitle() {
        return this.title;
    }

    public String getType() {
        return this.type;
    }

    public String getUrl() {
        return this.url;
    }

    public boolean is_editor_choice() {
        return this.is_editor_choice;
    }

    public boolean is_liked() {
        return this.is_liked;
    }

    public void setAbs(String paramString) {
        this.abs = paramString;
    }

    public void setApp_css(int paramInt) {
        this.app_css = paramInt;
    }


    public void setColumn(String paramString) {
        this.column = paramString;
    }

    public void setComments_count(int paramInt) {
        this.comments_count = paramInt;
    }

    public void setCreated_time(String paramString) {
        this.created_time = paramString;
    }

    public void setDate(String paramString) {
        this.date = paramString;
    }

    public void setDisplay_style(int paramInt) {
        this.display_style = paramInt;
    }


    public void setIs_editor_choice(boolean paramBoolean) {
        this.is_editor_choice = paramBoolean;
    }

    public void setIs_liked(boolean paramBoolean) {
        this.is_liked = paramBoolean;
    }

    public void setLike_count(int paramInt) {
        this.like_count = paramInt;
    }

    public void setPublished_time(String paramString) {
        this.published_time = paramString;
    }

    public void setShare_pic_url(String paramString) {
        this.share_pic_url = paramString;
    }

    public void setShort_url(String paramString) {
        this.short_url = paramString;
    }


    public void setTitle(String paramString) {
        this.title = paramString;
    }

    public void setType(String paramString) {
        this.type = paramString;
    }

    public void setUrl(String paramString) {
        this.url = paramString;
    }

    public int getItemType() {
        if (getCount() <= 0) {
            return NewDouBianContentAdapter.NEWS_DOUBIAN_NOIMG;
        }
        return NewDouBianContentAdapter.NEWS_DOUBIAN_NORMAL;
    }



    public class small {
        String url;
        int width;
        int height;

        public String getUrl() {
            return url;
        }
    }

    public class medium {
        String url;
        int width;
        int height;

        public String getUrl() {
            return url;
        }
    }

    public class large {
        String url;
        int width;
        int height;

        public String getUrl() {
            return url;
        }
    }


    private class author {
        boolean is_followed;
        String uid;
        String url;
        String avatar;
        String name;
        boolean is_special_user;
        int n_posts;
        String alt;
        String large_avatar;
        String id;
        boolean is_auth_author;
    }
}
