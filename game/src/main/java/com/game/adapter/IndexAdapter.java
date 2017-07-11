package com.game.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselibrary.utils.FileUtil;
import com.baselibrary.utils.StringUtil;
import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.R;
import com.game.mvp.index.modle.AdListGame;
import com.game.mvp.index.modle.IndexAdList;
import com.game.mvp.index.modle.IndexExt;
import com.game.mvp.model.Game;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private int downloadState;


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
        LinearLayout ll_game_subject_tags = helper.getView(R.id.ll_game_subject_tags);

        Glide.with(UIUtils.getContext())
                .load(subjectList.getExt().getPic_url_3_2())
                .placeholder(R.drawable.lufei)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.lufei).centerCrop()
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        ll_subject.setBackgroundDrawable(resource);
                    }
                });

        List<Game> list = subjectList.getExt().getList();

        for (int i = 0; i < list.size(); i++) {
            Game game = list.get(i);

            LinearLayout linearLayout = (LinearLayout) ll_game_subject_tags.getChildAt(i);
            CircleImageView circleImageView = (CircleImageView) linearLayout.getChildAt(0);
            TextView textView = (TextView) linearLayout.getChildAt(1);
            RelativeLayout relativeLayout = (RelativeLayout) linearLayout.getChildAt(2);

            TextView textView1 = (TextView) relativeLayout.getChildAt(0);


            Glide.with(UIUtils.getContext())
                    .load(game.getIcopath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(circleImageView);
            textView1.setText(FileUtil.byteSwitch(0, game.getSize_byte()));
            textView.setText(game.getAppname());
        }


    }

    private void initSubject3(BaseViewHolder helper, AdListGame item) {
        IndexAdList subjectList = item.getSubjectList();
        helper.setText(R.id.tv_game_subject_title, subjectList.getTitle());
        helper.setText(R.id.tv_game_subject_desc, subjectList.getDesc());
        ImageView iv_subject_icon = helper.getView(R.id.iv_game_subject_icon);
        Glide.with(UIUtils.getContext())
                .load(subjectList.getImg())
                .placeholder(R.drawable.lufei)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.lufei).centerCrop()
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
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop()
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
        LinearLayout ll_game_subject_tags = helper.getView(R.id.ll_game_subject_tags);


        List<Game> list = subjectList.getExt().getList();
        for (int i = 0; i < list.size(); i++) {
            LinearLayout linearLayout = (LinearLayout) ll_game_subject_tags.getChildAt(i);
            CircleImageView circleImageView = (CircleImageView) linearLayout.getChildAt(0);
            TextView textView = (TextView) linearLayout.getChildAt(1);
            Game game = list.get(i);

            Glide.with(UIUtils.getContext())
                    .load(game.getIcopath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(circleImageView);
            textView.setText(game.getAppname());

        }


        ll_subject_desc.setText(subjectList.getDesc());
        tv_subject_title.setText(subjectList.getTitle());

        IndexExt ext = subjectList.getExt();
        switch (subjectList.getType()) {
            case 1:
                Glide.with(UIUtils.getContext())
                        .load(ext.getPic_url_3_2())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.lufei)
                        .error(R.drawable.lufei)
                        .centerCrop()
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
                        .placeholder(R.drawable.lufei)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.lufei).centerCrop()
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
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(UIUtils.getContext(), GameDetailActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                UIUtils.getContext().startActivity(intent);
//            }
//        });

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
                .placeholder(R.drawable.lufei)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.lufei).fitCenter()
                .into(iv_game_icon);
        tv_game_title.setText(game.getAppname());
        tv_download_count.setText(FileUtil.downloadCountSwitch(0, game.getNum_download()));
        tv_download_size.setText(FileUtil.byteSwitch(2, game.getSize_byte()));
        tv_game_desc.setText(game.getReview());


//        final HttpDownload instance = HttpDownload.getInstance();
//        Logger.e("TAG","HttpDownload:"+instance.hashCode()+""+"---position"+helper.getLayoutPosition());
//        final HttpDownload.DownLoadInfo downLoadInfo = new HttpDownload.DownLoadInfo(game.getGame_id(), game.getDownurl());
//        HttpDownload.DownloadObserver downloadObserver = new HttpDownload.DownloadObserver() {
//
//            @Override
//            public void onDownloadStateChanged(HttpDownload.DownLoadInfo info) {
//                downloadState = info.getDownloadState();
//                switch (downloadState) {
//                    case HttpDownload.STATE_DOWNLOAD:
//                        btn_downlaod.setText("下载中");
//                        break;
//                    case HttpDownload.STATE_NONE:
//                        btn_downlaod.setText("空闲");
//                        break;
//                    case HttpDownload.STATE_CANCLE:
//                        ll_game.setVisibility(View.VISIBLE);
//                        ll_download.setVisibility(View.GONE);
//                        instance.cancle(info);
//                        break;
//                    case HttpDownload.STATE_SUCCESS:
//                        btn_downlaod.setText("安装");
//                        ll_game.setVisibility(View.VISIBLE);
//                        ll_download.setVisibility(View.GONE);
//                        instance.install(UIUtils.getContext(), info);
//                        break;
//                    case HttpDownload.STATE_ERROR:
//                        ToastUtils.makeShowToast(UIUtils.getContext(), "下载失败");
//                        btn_downlaod.setText("下载");
//                        break;
//                    case HttpDownload.STATE_PAUSE:
//                        btn_downlaod.setText("继续");
//                        break;
//                    case HttpDownload.STATE_WAITING:
//                        btn_downlaod.setText("等待");
//                        break;
//                }
//            }
//
//            @Override
//            public void onDownloadProgressChanged(HttpDownload.DownLoadInfo info) {
//                int progress = info.getProgress();
//                pb_download_progress.setProgress(progress);
//                String contentLength = FileUtil.byteSwitch(2, info.getContentLength() + "");
//                String currentPosition = FileUtil.byteSwitch(2, info.getCurrentPosition() + "");
//                tv_download_state.setText(currentPosition + "/" + contentLength);
//                String downloadSpeed = FileUtil.downloadSpeed(2, info.getDownLoadSpeed());
//                tv_download_speed.setText(downloadSpeed);
//            }
//        };
//        instance.registerObserver(downloadObserver);
//
//        btn_downlaod.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View v) {
//                String string = btn_downlaod.getText().toString();
//                ToastUtils.makeShowToast(UIUtils.getContext(), helper.getLayoutPosition() + "");
//                ll_download.setVisibility(View.VISIBLE);
//                ll_game.setVisibility(View.GONE);
//                // 根据当前状态来决定相关操作
//                if (string .equals("空闲")
//                        || string .equals("继续")
//                        || string.equals("下载")) {
//                    // 开始下载
//                    instance.download(downLoadInfo);
//                } else if (string .equals("等待")
//                        || string .equals("下载中")) {
//                    // 暂停下载
//                    instance.pause(downLoadInfo);
//                } else if (string .equals("安装")) {
//                    // 开始安装
//                    instance.install(UIUtils.getContext(), downLoadInfo);
//                }
//            }
//        });
    }


}
