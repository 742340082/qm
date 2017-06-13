package com.getaddress.modle;


import com.baselibrary.view.BaseIndexBean;

public class GetAddressCity extends BaseIndexBean implements  Comparable<GetAddressCity>{
    private Integer id;

    private String code;

    private String name;

    private Integer parentid;

    private String firstLetter;

    private Integer level;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter == null ? null : firstLetter.trim();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public int compareTo(GetAddressCity o) {
        return getFirstLetter().compareTo(o.getFirstLetter());
    }

    @Override
    public boolean isSuspension() {
        return true;
    }

    @Override
    public String getSuspension() {
        return getFirstLetter();
    }
}