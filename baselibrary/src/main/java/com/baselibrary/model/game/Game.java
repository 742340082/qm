package com.baselibrary.model.game;

import android.os.Parcel;
import android.os.Parcelable;

import com.baselibrary.model.game.category.detail.CategoryDetailResult;
import com.baselibrary.model.game.index.IndexExt;
import com.baselibrary.model.game.index.IndexResult;
import com.baselibrary.model.game.search.GameSearchResult;
import com.baselibrary.model.game.search.GameSmallSearchResult;
import com.baselibrary.model.game.top.TotalResult;
import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

/**
 * Created by 74234 on 2017/5/18.
 */

public class Game extends DataSupport implements Parcelable{
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


    private String desc;
    private String img;
    private String title;
    private int type;

    private  IndexExt indexExt;
    private TotalResult totalResult;
    private CategoryDetailResult categoryDetailResult;
     private IndexResult indexResult;
    private GameSmallSearchResult gameSmallSearchResult;
    private GameSearchResult gameSearchResult;


    protected Game(Parcel in) {
        appname = in.readString();
        audit_hide = in.readString();
        downurl = in.readString();
        fixed = in.readInt();
        emulator = in.readString();
        gift_show_cli = in.readString();
        guide_model = in.readString();
        hasGift = in.readInt();
        icon_tag = in.readInt();
        icopath = in.readString();
        game_id = in.readString();
        md5_file = in.readString();
        need_gplay = in.readString();
        num_download = in.readLong();
        packag = in.readString();
        price = in.readString();
        review = in.readString();
        sdk_version = in.readString();
        size = in.readDouble();
        size_byte = in.readString();
        star = in.readString();
        video_id = in.readLong();
        statFlag = in.readString();
        state = in.readInt();
        video_url = in.readString();
        kind_id = in.readString();
        kind_name = in.readString();
        desc = in.readString();
        img = in.readString();
        title = in.readString();
        type = in.readInt();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    public GameSearchResult getGameSearchResult() {
        return gameSearchResult;
    }

    public void setGameSearchResult(GameSearchResult gameSearchResult) {
        this.gameSearchResult = gameSearchResult;
    }

    public GameSmallSearchResult getGameSmallSearchResult() {
        return gameSmallSearchResult;
    }

    public void setGameSmallSearchResult(GameSmallSearchResult gameSmallSearchResult) {
        this.gameSmallSearchResult = gameSmallSearchResult;
    }

    public IndexExt getIndexExt() {
        return indexExt;
    }

    public void setIndexExt(IndexExt indexExt) {
        this.indexExt = indexExt;
    }

    public IndexResult getIndexResult() {
        return indexResult;
    }

    public void setIndexResult(IndexResult indexResult) {
        this.indexResult = indexResult;
    }

    public CategoryDetailResult getCategoryDetailResult() {
        return categoryDetailResult;
    }

    public void setCategoryDetailResult(CategoryDetailResult categoryDetailResult) {
        this.categoryDetailResult = categoryDetailResult;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

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

    public IndexExt getExt() {
        return ext;
    }

    public void setExt(IndexExt ext) {
        this.ext = ext;
    }

    private IndexExt ext;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appname);
        dest.writeString(audit_hide);
        dest.writeString(downurl);
        dest.writeInt(fixed);
        dest.writeString(emulator);
        dest.writeString(gift_show_cli);
        dest.writeString(guide_model);
        dest.writeInt(hasGift);
        dest.writeInt(icon_tag);
        dest.writeString(icopath);
        dest.writeString(game_id);
        dest.writeString(md5_file);
        dest.writeString(need_gplay);
        dest.writeLong(num_download);
        dest.writeString(packag);
        dest.writeString(price);
        dest.writeString(review);
        dest.writeString(sdk_version);
        dest.writeDouble(size);
        dest.writeString(size_byte);
        dest.writeString(star);
        dest.writeLong(video_id);
        dest.writeString(statFlag);
        dest.writeInt(state);
        dest.writeString(video_url);
        dest.writeString(kind_id);
        dest.writeString(kind_name);
        dest.writeString(desc);
        dest.writeString(img);
        dest.writeString(title);
        dest.writeInt(type);
    }
}
