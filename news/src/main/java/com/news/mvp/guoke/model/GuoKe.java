package com.news.mvp.guoke.model;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

public class GuoKe extends DataSupport {
    private long id;
    private String now;
    private boolean ok;
    private ArrayList<GuoKeResult> result;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public ArrayList<GuoKeResult> getResult() {
        return result;
    }

    public void setResult(ArrayList<GuoKeResult> result) {
        this.result = result;
    }


    public class source_data {
        private String image;
        private String summary;
        private int id;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
