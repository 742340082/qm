package com.game.adapter.gameindex;

import android.os.RemoteException;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baselibrary.api.download.DownloadAidl;
import com.baselibrary.api.download.DownloadInfo;
import com.baselibrary.api.download.DownloadObserver;
import com.baselibrary.api.download.HttpDownload;
import com.baselibrary.base.activity.MyApplication;
import com.baselibrary.model.game.Game;
import com.baselibrary.model.game.index.AdListGame;
import com.baselibrary.model.game.index.IndexAdList;
import com.baselibrary.model.game.index.IndexExt;
import com.baselibrary.utils.GameUtil;
import com.baselibrary.utils.StringUtil;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.R;

import java.util.HashMap;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by 74234 on 2017/4/26.
 */

public class IndexAdapter extends BaseMultiItemQuickAdapter<AdListGame, BaseViewHolder> {


    public final static int GAME_RECOMMEND_NORMAL = 1;
    public final static int GAME_RECOMMEND_SUBJECT1 = 2;
    public final static int GAME_RECOMMEND_SUBJECT2 = 3;
    public final static int GAME_RECOMMEND_SUBJECT3 = 4;
    public final static int GAME_RECOMMEND_SUBJECT4 = 5;
    private static int k;
    private DownloadAidl downloadAidl;
    private HashMap<String, DownloadInfo> downloadInfoHashMap = new HashMap<>();

    public IndexAdapter(List<AdListGame> data) {
        super(data);
        addItemType(GAME_RECOMMEND_NORMAL, R.layout.item_game_index_normal);
        addItemType(GAME_RECOMMEND_SUBJECT1, R.layout.item_game_index_subject1);
        addItemType(GAME_RECOMMEND_SUBJECT2, R.layout.item_game_index_subject2);
        addItemType(GAME_RECOMMEND_SUBJECT3, R.layout.item_game_index_subject3);
        addItemType(GAME_RECOMMEND_SUBJECT4, R.layout.item_game_index_subject4);
    }

    @Override
    protected void convert(BaseViewHolder helper, AdListGame item) {
        if (downloadAidl == null) {
            downloadAidl = MyApplication.getDownloadAidl();
        }

        int itemType = item.getItemType();
        switch (itemType) {
            case GAME_RECOMMEND_NORMAL:
                initNormal(helper, item);
                break;
            case GAME_RECOMMEND_SUBJECT1:
                initSubject1(helper, item);
                break;
            case GAME_RECOMMEND_SUBJECT2:
                initSubject2(helper, item);
                break;
            case GAME_RECOMMEND_SUBJECT3:
                initSubject3(helper, item);
                break;
            case GAME_RECOMMEND_SUBJECT4:
                initSubject4(helper, item);
                break;

        }
    }

    private void initSubject4(BaseViewHolder helper, AdListGame item) {
        IndexAdList subjectList = item.getSubjectList();
        helper.setText(R.id.tv_game_subject_title, subjectList.getTitle());
        final LinearLayout ll_subject = helper.getView(R.id.ll_game_subject);
        final RecyclerView rv_game_index_subject = helper.getView(R.id.rv_game_index_subject);

        Glide.with(UIUtils.getContext())
                .load(subjectList.getExt().getPic_url_3_2())
                .bitmapTransform(new RoundedCornersTransformation(UIUtils.getContext(),10,0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        ll_subject.setBackgroundDrawable(resource);
                    }
                });

        IndexSubject4Adapter indexSubject4Adapter=new IndexSubject4Adapter(R.layout.item_game_index_subject4_tag,
                subjectList.getExt().getList());
        indexSubject4Adapter.openLoadAnimation(IndexSubject1Adapter.ALPHAIN);
        rv_game_index_subject.setLayoutManager(new GridLayoutManager(UIUtils.getContext(),4));
        rv_game_index_subject.setAdapter(indexSubject4Adapter);




    }

    private void initSubject3(BaseViewHolder helper, AdListGame item) {
        IndexAdList subjectList = item.getSubjectList();
        helper.setText(R.id.tv_game_subject_title, subjectList.getTitle());
        helper.setText(R.id.tv_game_subject_desc, subjectList.getDesc());
        ImageView iv_subject_icon = helper.getView(R.id.iv_game_subject_icon);
        Glide.with(UIUtils.getContext())
                .load(subjectList.getImg())
                .bitmapTransform(new RoundedCornersTransformation(UIUtils.getContext(),10,0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_subject_icon);
    }

    private void initSubject2(BaseViewHolder helper, AdListGame item) {
        IndexAdList subjectList = item.getSubjectList();

        LinearLayout convertView = (LinearLayout) helper.getConvertView();
        TextView tv_subject_title = helper.getView(R.id.tv_game_subject_title);
        tv_subject_title.setText(subjectList.getTitle());
        LinearLayout linearLayout = (LinearLayout) convertView.getChildAt(2);

        List<Game> games = subjectList.getExt().getList();

        k = 0;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {

            View childView = linearLayout.getChildAt(i);
            if (childView instanceof LinearLayout) {
                LinearLayout linearLayout1 = (LinearLayout) childView;
                for (int j = 0; j < linearLayout1.getChildCount(); j++) {
                    View view = linearLayout1.getChildAt(j);
                    if (view instanceof ImageView) {
                        final ImageView imageview = (ImageView) view;
                        Game game = games.get(k);
                        Glide.with(UIUtils.getContext())
                                .load(game.getImg())
                                .bitmapTransform(new RoundedCornersTransformation(UIUtils.getContext(),10,0))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(new SimpleTarget<GlideDrawable>() {
                                    @Override
                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                        imageview.setBackgroundDrawable(resource);
                                    }
                                });
                        k++;
                    }
                }
            }
        }

    }

    private void initSubject1(BaseViewHolder helper, AdListGame item) {
        IndexAdList subjectList = item.getSubjectList();

        final LinearLayout ll_subject = helper.getView(R.id.ll_game_subject);
        TextView tv_subject_title = helper.getView(R.id.tv_game_subject_title);
        TextView ll_subject_desc = helper.getView(R.id.ll_game_subject_desc);
        RecyclerView rv_game_index_subject = helper.getView(R.id.rv_game_index_subject);

        IndexSubject1Adapter indexSubject1Adapter=new IndexSubject1Adapter(R.layout.item_game_index_subject1_tag,
                subjectList.getExt().getList());
        indexSubject1Adapter.openLoadAnimation(IndexSubject1Adapter.ALPHAIN);
        rv_game_index_subject.setLayoutManager(new GridLayoutManager(UIUtils.getContext(),4));
        rv_game_index_subject.setAdapter(indexSubject1Adapter);

        ll_subject_desc.setText(subjectList.getDesc());
        tv_subject_title.setText(subjectList.getTitle());
        IndexExt ext = subjectList.getExt();
        switch (subjectList.getType()) {
            case 1:
                Glide.with(UIUtils.getContext())
                        .load(ext.getPic_url_3_2())
                        .bitmapTransform(new RoundedCornersTransformation(UIUtils.getContext(),10,0))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                ll_subject.setBackgroundDrawable(resource);
                            }
                        });
                break;
            case 13:
                Glide.with(UIUtils.getContext())
                        .load(subjectList.getImg())
                        .bitmapTransform(new RoundedCornersTransformation(UIUtils.getContext(),10,0))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                ll_subject.setBackgroundDrawable(resource);
                            }
                        });
                break;
        }

    }

    private void initNormal(final BaseViewHolder helper, final AdListGame item) {

        final Game game = item.getGame();
        View convertView = helper.getConvertView();

        ImageView iv_game_icon = helper.getView(R.id.iv_game_icon);
        TextView tv_game_title = helper.getView(R.id.tv_game_title);
        TextView tv_download_count = helper.getView(R.id.tv_game_download_count);
        TextView tv_download_size = helper.getView(R.id.tv_game_download_size);
        TextView tv_game_desc = helper.getView(R.id.tv_game_desc);
        TextView tv_game_vedio = helper.getView(R.id.tv_game_vedio);

        final Button btn_game_downlaod = helper.getView(R.id.btn_game_downlaod);
        final LinearLayout ll_game = helper.getView(R.id.ll_game);
        final LinearLayout ll_download = helper.getView(R.id.ll_game_download);
        final ProgressBar pb_download_progress = helper.getView(R.id.pb_game_download_progress);
        final TextView tv_download_state = helper.getView(R.id.tv_game_download_state);
        final TextView tv_download_speed = helper.getView(R.id.tv_game_download_speed);


        if (StringUtil.isEmpty(game.getVideo_url())) {
            tv_game_vedio.setVisibility(View.GONE);
        }
        Glide.with(UIUtils.getContext())
                .load(game.getIcopath())
                .bitmapTransform(new RoundedCornersTransformation(UIUtils.getContext(),24,0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_game_icon);
        tv_game_title.setText(game.getAppname());
        tv_download_count.setText(GameUtil.downloadCountSwitch(0, game.getNum_download()));
        tv_download_size.setText(GameUtil.byteSwitch(2, game.getSize_byte()));
        tv_game_desc.setText(game.getReview());


        try {
            initGameDownloadInfo(game, btn_game_downlaod, ll_game, ll_download,
                    pb_download_progress, tv_download_state, tv_download_speed);
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    private void initGameDownloadInfo(final Game game, final Button btn_game_downlaod,
                                      final LinearLayout ll_game, final LinearLayout ll_download,
                                      final ProgressBar pb_download_progress, final TextView tv_download_state,
                                      final TextView tv_download_speed ) throws RemoteException {

        final DownloadInfo downloadInfo = new DownloadInfo(game.getGame_id(), game.getDownurl(), game.getAppname());
        downloadInfo.setDownloadInfo(game);
        DownloadInfo downloadInfo1 = null;
        try {
            downloadInfo1 = downloadAidl.getDownloadInfo(game.getGame_id());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        downloadInfoHashMap.put(game.getGame_id(), downloadInfo);
        if (downloadInfo1 != null && downloadInfo1.getCurrentPosition() > 0) {
            downloadInfo.updateDownloadInfo(downloadInfo1);
        }

        final int[] downloadState = {0};


        DownloadObserver downloadObserver = new DownloadObserver.Stub() {

            @Override
            public void onDownloadStateChanged(DownloadInfo info) throws RemoteException {
                downloadState[0] = info.getDownloadState();
                switch (downloadState[0]) {
                    case HttpDownload.STATE_DOWNLOAD:
                        ll_download.setVisibility(View.VISIBLE);
                        ll_game.setVisibility(View.GONE);
                        btn_game_downlaod.setText("下载中");

                        break;
                    case HttpDownload.STATE_NONE:
                        btn_game_downlaod.setText("空闲");
                        break;
                    case HttpDownload.STATE_CANCLE:
                        ll_game.setVisibility(View.VISIBLE);
                        ll_download.setVisibility(View.GONE);
                        btn_game_downlaod.setText("下载");
                        break;
                    case HttpDownload.STATE_SUCCESS:
                        btn_game_downlaod.setText("安装");
                        ll_game.setVisibility(View.VISIBLE);
                        ll_download.setVisibility(View.GONE);
                        break;
                    case HttpDownload.STATE_ERROR:
                        ToastUtils.makeShowToast(UIUtils.getContext(), "下载失败");
                        btn_game_downlaod.setText("失败");
                        downloadAidl.download(downloadInfo);
                        downloadAidl.unregistDownloadObserver(game.hashCode() + "", game.getGame_id());
                        break;
                    case HttpDownload.STATE_PAUSE:
                        btn_game_downlaod.setText("继续");
                        break;
                    case HttpDownload.STATE_WAITING:
                        ll_game.setVisibility(View.VISIBLE);
                        ll_download.setVisibility(View.GONE);
                        btn_game_downlaod.setText("等待");
                        break;
                }

            }


            @Override
            public void onDownloadProgressChanged(DownloadInfo info) {
                int progress = info.getPrecentNumber();
                pb_download_progress.setProgress(progress);
                String contentLength = GameUtil.byteSwitch(2, info.getContentLength() + "");
                String currentPosition = GameUtil.byteSwitch(2, info.getCurrentPosition() + "");
                tv_download_state.setText(currentPosition + "/" + contentLength);
                tv_download_speed.setText(info.getPrecentNumber() + "/%");
            }
        };
        downloadAidl.registDownloadObserver(game.hashCode() + "", game.getGame_id(), downloadObserver);

        if (downloadInfo != null && downloadInfo.getCurrentPosition() > 0) {
            ll_download.setVisibility(View.VISIBLE);
            ll_game.setVisibility(View.GONE);
            pb_download_progress.setProgress(downloadInfo.getPrecentNumber());
            tv_download_speed.setText(downloadInfo.getPrecentNumber() + "/%");
            tv_download_state.setText(GameUtil.byteSwitch(2, downloadInfo.getCurrentPosition() + "") + "/"
                    + GameUtil.byteSwitch(2, downloadInfo.getContentLength() + ""));

            downloadState[0] = downloadInfo.getDownloadState();
            switch (downloadState[0]) {
                case HttpDownload.STATE_DOWNLOAD:
                    ll_download.setVisibility(View.VISIBLE);
                    ll_game.setVisibility(View.GONE);
                    btn_game_downlaod.setText("下载中");
                    downloadAidl.download(downloadInfo);
                    break;
                case HttpDownload.STATE_NONE:
                    btn_game_downlaod.setText("下载");
                    break;
                case HttpDownload.STATE_CANCLE:
                    ll_game.setVisibility(View.VISIBLE);
                    ll_download.setVisibility(View.GONE);
                    break;
                case HttpDownload.STATE_SUCCESS:
                    btn_game_downlaod.setText("安装");
                    ll_game.setVisibility(View.VISIBLE);
                    ll_download.setVisibility(View.GONE);
                    break;
                case HttpDownload.STATE_ERROR:
                    ToastUtils.makeShowToast(UIUtils.getContext(), "下载失败");
                    btn_game_downlaod.setText("下载");
                    break;
                case HttpDownload.STATE_PAUSE:
                    btn_game_downlaod.setText("继续");
                    break;
                case HttpDownload.STATE_WAITING:
                    btn_game_downlaod.setText("等待");
                    downloadAidl.download(downloadInfo);
                    break;
            }
        }

        btn_game_downlaod.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    // 根据当前状态来决定相关操作
                    if (downloadState[0] == HttpDownload.STATE_ERROR || downloadState[0] == HttpDownload.STATE_NONE
                            || downloadState[0] == HttpDownload.STATE_CANCLE || downloadState[0] == HttpDownload.STATE_PAUSE
                            ) {
                        // 开始下载
                        if (downloadInfo != null) {

                            downloadAidl.download(downloadInfo);
                        }
                    } else if (downloadState[0] == HttpDownload.STATE_WAITING
                            || downloadState[0] == HttpDownload.STATE_DOWNLOAD) {
                        // 暂停下载
                        if (downloadInfo != null) {
                            downloadAidl.pause(downloadInfo);
                        }

                    } else if (downloadState[0] == HttpDownload.STATE_SUCCESS) {
                        // 开始安装
                        if (downloadInfo != null) {
                            downloadAidl.install(downloadInfo);
                        }

                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        });
    }


}
