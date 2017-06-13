package com.game.mvp.model;

import com.game.mvp.recommend.modle.Ext;
import com.game.mvp.top.modle.TotalResult;
import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

/**
 * Created by 74234 on 2017/5/18.
 */

public class Game extends DataSupport {
    private String appname;
    private String audit_hide;
    private String downurl;
    private int fixed;
    private String emulator;
    private String gift_show_cli;
    private String guide_model;
    private int hasGift;
    private int icon_tag;
    private String icopath;

    @SerializedName("id")
    private String game_id;

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    private String md5_file;
    private String need_gplay;
    private long num_download;
    private String packag;
    private String price;
    private String review;
    private String sdk_version;
    private double size;
    private String size_byte;
    private String star;
    private long video_id;
    private String statFlag;
    private int state;
    private String video_url;

    private String kind_id;
    private String kind_name;
    private TotalResult totalResult;

    public TotalResult getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(TotalResult totalResult) {
        this.totalResult = totalResult;
    }

    public String getKind_id() {
        return kind_id;
    }

    public void setKind_id(String kind_id) {
        this.kind_id = kind_id;
    }

    public String getKind_name() {
        return kind_name;
    }

    public void setKind_name(String kind_name) {
        this.kind_name = kind_name;
    }

    private String desc;
    private String img;
    private String title;
    private int type;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Ext getExt() {
        return ext;
    }

    public void setExt(Ext ext) {
        this.ext = ext;
    }

    private Ext ext;

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getAudit_hide() {
        return audit_hide;
    }

    public void setAudit_hide(String audit_hide) {
        this.audit_hide = audit_hide;
    }

    public String getDownurl() {
        return downurl;
    }

    public void setDownurl(String downurl) {
        this.downurl = downurl;
    }

    public int getFixed() {
        return fixed;
    }

    public void setFixed(int fixed) {
        this.fixed = fixed;
    }

    public String getEmulator() {
        return emulator;
    }

    public void setEmulator(String emulator) {
        this.emulator = emulator;
    }

    public String getGift_show_cli() {
        return gift_show_cli;
    }

    public void setGift_show_cli(String gift_show_cli) {
        this.gift_show_cli = gift_show_cli;
    }

    public String getGuide_model() {
        return guide_model;
    }

    public void setGuide_model(String guide_model) {
        this.guide_model = guide_model;
    }

    public int getHasGift() {
        return hasGift;
    }

    public void setHasGift(int hasGift) {
        this.hasGift = hasGift;
    }

    public int getIcon_tag() {
        return icon_tag;
    }

    public void setIcon_tag(int icon_tag) {
        this.icon_tag = icon_tag;
    }

    public String getIcopath() {
        return icopath;
    }

    public void setIcopath(String icopath) {
        this.icopath = icopath;
    }


    public String getMd5_file() {
        return md5_file;
    }

    public void setMd5_file(String md5_file) {
        this.md5_file = md5_file;
    }

    public String getNeed_gplay() {
        return need_gplay;
    }

    public void setNeed_gplay(String need_gplay) {
        this.need_gplay = need_gplay;
    }

    public long getNum_download() {
        return num_download;
    }

    public void setNum_download(long num_download) {
        this.num_download = num_download;
    }

    public String getPackag() {
        return packag;
    }

    public void setPackag(String packag) {
        this.packag = packag;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getSdk_version() {
        return sdk_version;
    }

    public void setSdk_version(String sdk_version) {
        this.sdk_version = sdk_version;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getSize_byte() {
        return size_byte;
    }

    public void setSize_byte(String size_byte) {
        this.size_byte = size_byte;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public long getVideo_id() {
        return video_id;
    }

    public void setVideo_id(long video_id) {
        this.video_id = video_id;
    }

    public String getStatFlag() {
        return statFlag;
    }

    public void setStatFlag(String statFlag) {
        this.statFlag = statFlag;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }
}
