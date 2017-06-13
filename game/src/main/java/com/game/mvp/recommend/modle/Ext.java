package com.game.mvp.recommend.modle;

import com.game.mvp.model.Game;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 74234 on 2017/6/11.
 */

public class Ext extends DataSupport{
    private long id;
    private String pic_url_3_2;
    private String url;
    private List<Game> list=new ArrayList<>();

    public SubjectList getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(SubjectList subjectList) {
        this.subjectList = subjectList;
    }

    private SubjectList subjectList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPic_url_3_2() {
        return pic_url_3_2;
    }

    public void setPic_url_3_2(String pic_url_3_2) {
        this.pic_url_3_2 = pic_url_3_2;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Game> getList() {
        return list;
    }

    public void setList(List<Game> list) {
        this.list = list;
    }
}
