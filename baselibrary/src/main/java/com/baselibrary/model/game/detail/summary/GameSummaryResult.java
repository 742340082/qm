package com.baselibrary.model.game.detail.summary;

import com.baselibrary.model.game.Game;

import java.util.List;

/**
 * Created by 74234 on 2017/7/25.
 */

public class GameSummaryResult {
    private int advertise;
    private String appinfo;
    private String appname;
    private String audit_hide;
    private String dateline;
    private int declState;
    private String devid;
    private String downurl;
    private String emulator;
    private String forumsId;
    private String from_user;
    private String guide_model;
    private String icopath;
    private String id;
    private String kind_id;
    private String kind_name;
    private int language;
    private String md5_file;
    private String need_gplay;
    private String network;
    private String news_id;
    private int news_tab;
    private String note;
    private long num_download;
    private long num_inst;
    private long num_like;
    private String packag;
    private int quanId;
    private String report_url;
    private String review;
    private String sdk_version;
    private double size;
    private String size_byte;
    private String star;
    private int state;
    private int verify;
    private String version;
    private String versioncode;
    private String video_id;
    private String video_poster;
    private String video_url;
    private int virus;

    private GameSummaryComment comment;
    private List<String> screenpath;
    private List<GameSummaryTagS> tags;
    private List<GameSummaryRelate> gameRelate;
    private List<Game> recommendGames;

    public List<Game> getRecommendGames() {
        return recommendGames;
    }

    public void setRecommendGames(List<Game> recommendGames) {
        this.recommendGames = recommendGames;
    }

    public List<GameSummaryRelate> getGameRelate() {
        return gameRelate;
    }

    public void setGameRelate(List<GameSummaryRelate> gameRelate) {
        this.gameRelate = gameRelate;
    }

    public int getAdvertise() {
        return advertise;
    }

    public void setAdvertise(int advertise) {
        this.advertise = advertise;
    }

    public String getAppinfo() {
        return appinfo;
    }

    public void setAppinfo(String appinfo) {
        this.appinfo = appinfo;
    }

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

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public int getDeclState() {
        return declState;
    }

    public void setDeclState(int declState) {
        this.declState = declState;
    }

    public String getDevid() {
        return devid;
    }

    public void setDevid(String devid) {
        this.devid = devid;
    }

    public String getDownurl() {
        return downurl;
    }

    public void setDownurl(String downurl) {
        this.downurl = downurl;
    }

    public String getEmulator() {
        return emulator;
    }

    public void setEmulator(String emulator) {
        this.emulator = emulator;
    }

    public String getForumsId() {
        return forumsId;
    }

    public void setForumsId(String forumsId) {
        this.forumsId = forumsId;
    }

    public String getFrom_user() {
        return from_user;
    }

    public void setFrom_user(String from_user) {
        this.from_user = from_user;
    }

    public String getGuide_model() {
        return guide_model;
    }

    public void setGuide_model(String guide_model) {
        this.guide_model = guide_model;
    }

    public String getIcopath() {
        return icopath;
    }

    public void setIcopath(String icopath) {
        this.icopath = icopath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
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

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public int getNews_tab() {
        return news_tab;
    }

    public void setNews_tab(int news_tab) {
        this.news_tab = news_tab;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getNum_download() {
        return num_download;
    }

    public void setNum_download(long num_download) {
        this.num_download = num_download;
    }

    public long getNum_inst() {
        return num_inst;
    }

    public void setNum_inst(long num_inst) {
        this.num_inst = num_inst;
    }

    public long getNum_like() {
        return num_like;
    }

    public void setNum_like(long num_like) {
        this.num_like = num_like;
    }

    public String getPackag() {
        return packag;
    }

    public void setPackag(String packag) {
        this.packag = packag;
    }

    public int getQuanId() {
        return quanId;
    }

    public void setQuanId(int quanId) {
        this.quanId = quanId;
    }

    public String getReport_url() {
        return report_url;
    }

    public void setReport_url(String report_url) {
        this.report_url = report_url;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getVerify() {
        return verify;
    }

    public void setVerify(int verify) {
        this.verify = verify;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(String versioncode) {
        this.versioncode = versioncode;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getVideo_poster() {
        return video_poster;
    }

    public void setVideo_poster(String video_poster) {
        this.video_poster = video_poster;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public int getVirus() {
        return virus;
    }

    public void setVirus(int virus) {
        this.virus = virus;
    }

    public GameSummaryComment getComment() {
        return comment;
    }

    public void setComment(GameSummaryComment comment) {
        this.comment = comment;
    }

    public List<String> getScreenpath() {
        return screenpath;
    }

    public void setScreenpath(List<String> screenpath) {
        this.screenpath = screenpath;
    }

    public List<GameSummaryTagS> getTags() {
        return tags;
    }

    public void setTags(List<GameSummaryTagS> tags) {
        this.tags = tags;
    }
}
