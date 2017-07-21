package com.game.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselibrary.api.download.DownloadInfo;
import com.baselibrary.api.download.HttpDownload;
import com.baselibrary.utils.FileUtil;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.R;
import com.game.mvp.model.Game;

import java.util.List;

/**
 * Created by 74234 on 2017/7/20.
 */

public class GameDownloadAdapter extends BaseQuickAdapter<DownloadInfo<Game> ,BaseViewHolder> {

    private int downloadState;
    private HttpDownload instance;

    public GameDownloadAdapter(int layoutResId, List<DownloadInfo<Game> > data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final DownloadInfo<Game> downloadInfo) {
        instance=  HttpDownload.getInstance();
        downloadState=downloadInfo.getDownloadState();
        View convertView = helper.getConvertView();
        RelativeLayout rl_game_top = helper.getView(R.id.rl_game_top);
        rl_game_top.setVisibility(View.GONE);
        ImageView iv_game_top_icon = helper.getView(R.id.iv_game_top_icon);
        TextView tv_game_top_title = helper.getView(R.id.tv_game_top_title);

        final LinearLayout ll_game_download_info = helper.getView(R.id.ll_game_download_info);
        TextView tv_game_top_download_number = helper.getView(R.id.tv_game_top_download_number);
        TextView tv_game_top_tag_size = helper.getView(R.id.tv_game_top_tag_size);
        TextView tv_game_top_type = helper.getView(R.id.tv_game_top_type);


        final  LinearLayout ll_game_download = helper.getView(R.id.ll_game_download);
       final ProgressBar pb_game_download_progress = helper.getView(R.id.pb_game_download_progress);
        final  TextView tv_game_download_state = helper.getView(R.id.tv_game_download_state);
        final  TextView tv_game_download_speed = helper.getView(R.id.tv_game_download_speed);
        final Button btn_game_download = helper.getView(R.id.btn_game_download);

        final Game game = downloadInfo.getDownloadInfo();


        Glide.with(UIUtils.getContext())
                .load(game.getIcopath())
                .placeholder(R.drawable.lufei)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.lufei).fitCenter()
                .into(iv_game_top_icon);
        tv_game_top_title.setText(game.getAppname());
        if (downloadInfo.getCurrentPosition()==0)
        {
            ll_game_download.setVisibility(View.GONE);
            ll_game_download_info.setVisibility(View.VISIBLE);


            tv_game_top_download_number.setText(FileUtil.downloadCountSwitch(0,game.getNum_download()));
            tv_game_top_tag_size.setText(FileUtil.byteSwitch(0,game.getSize_byte()));
            tv_game_top_type.setText(game.getKind_name());

        }else
        {
            switch (downloadState) {
                case HttpDownload.STATE_DOWNLOAD:
                    ll_game_download.setVisibility(View.VISIBLE);
                    ll_game_download_info.setVisibility(View.GONE);
                    btn_game_download.setText("下载中");

                    break;
                case HttpDownload.STATE_NONE:
                    btn_game_download.setText("空闲");
                    break;
                case HttpDownload.STATE_CANCLE:
                    ll_game_download_info.setVisibility(View.VISIBLE);
                    ll_game_download.setVisibility(View.GONE);
                    instance.cancle(downloadInfo);
                    break;
                case HttpDownload.STATE_SUCCESS:
                    btn_game_download.setText("安装");
                    ll_game_download_info.setVisibility(View.VISIBLE);
                    ll_game_download.setVisibility(View.GONE);
                    instance.install(UIUtils.getContext(), downloadInfo);
                    break;
                case HttpDownload.STATE_ERROR:
                    ToastUtils.makeShowToast(UIUtils.getContext(), "下载失败");
                    btn_game_download.setText("下载");
                    instance.cancle(downloadInfo);
                    instance.unregisterObserver(game.getGame_id());
                    break;
                case HttpDownload.STATE_PAUSE:
                    btn_game_download.setText("继续");
                    break;
                case HttpDownload.STATE_WAITING:
                    btn_game_download.setText("等待");
                    break;

            }


            ll_game_download.setVisibility(View.VISIBLE);
            ll_game_download_info.setVisibility(View.GONE);
            String contentLength = FileUtil.byteSwitch(2, downloadInfo.getContentLength() + "");
            String currentPostion = FileUtil.byteSwitch(2, downloadInfo.getCurrentPosition() + "");
            pb_game_download_progress.setProgress(downloadInfo.getPrecentNumber());
            tv_game_download_state.setText(currentPostion+"/"+contentLength);
            tv_game_download_speed.setText(downloadInfo.getPrecentNumber()+"/%");

            HttpDownload.DownloadObserver downloadObserver = new HttpDownload.DownloadObserver() {
                @Override
                public void onDownloadStateChanged(DownloadInfo info) {
                    downloadState = info.getDownloadState();
                    switch (downloadState) {
                        case HttpDownload.STATE_DOWNLOAD:
                            ll_game_download.setVisibility(View.VISIBLE);
                            ll_game_download_info.setVisibility(View.GONE);
                            btn_game_download.setText("下载中");

                            break;
                        case HttpDownload.STATE_NONE:
                            btn_game_download.setText("空闲");
                            break;
                        case HttpDownload.STATE_CANCLE:
                            ll_game_download_info.setVisibility(View.VISIBLE);
                            ll_game_download.setVisibility(View.GONE);
                            instance.cancle(downloadInfo);
                            break;
                        case HttpDownload.STATE_SUCCESS:
                            btn_game_download.setText("安装");
                            ll_game_download_info.setVisibility(View.VISIBLE);
                            ll_game_download.setVisibility(View.GONE);
                            instance.install(UIUtils.getContext(), downloadInfo);
                            break;
                        case HttpDownload.STATE_ERROR:
                            ToastUtils.makeShowToast(UIUtils.getContext(), "下载失败");
                            btn_game_download.setText("下载");
                            instance.cancle(downloadInfo);
                            instance.unregisterObserver(game.getGame_id());
                            break;
                        case HttpDownload.STATE_PAUSE:
                            btn_game_download.setText("继续");
                            break;
                        case HttpDownload.STATE_WAITING:
                            btn_game_download.setText("等待");
                            break;

                    }
                }


                @Override
                public void onDownloadProgressChanged(DownloadInfo info) {
                    int progress = info.getPrecentNumber();
                    pb_game_download_progress.setProgress(progress);
                    String contentLength = FileUtil.byteSwitch(2, info.getContentLength() + "");
                    String currentPosition = FileUtil.byteSwitch(2, info.getCurrentPosition() + "");
                    tv_game_download_state.setText(currentPosition + "/" + contentLength);
                    tv_game_download_speed.setText(info.getPrecentNumber() + "/%");
                }
            };

            instance.registerObserver(game.getGame_id(),downloadObserver);

            btn_game_download.setOnClickListener(new View.OnClickListener() {



                @Override
                public void onClick(View v) {
                    // 根据当前状态来决定相关操作
                    if (downloadState == HttpDownload.STATE_NONE|| downloadState == HttpDownload.STATE_ERROR
                            || downloadState == HttpDownload.STATE_CANCLE || downloadState == HttpDownload.STATE_PAUSE
                            ) {
                        // 开始下载
                        if (downloadInfo != null && instance != null) {
                            instance.download(downloadInfo);
                        }

                    } else if (downloadState == HttpDownload.STATE_WAITING
                            || downloadState == HttpDownload.STATE_DOWNLOAD) {
                        // 暂停下载
                        if (downloadInfo != null && instance != null) {
                            instance.pause(downloadInfo);
                        }

                    } else if (downloadState == HttpDownload.STATE_SUCCESS) {
                        // 开始安装
                        if (downloadInfo != null && instance != null) {
                            instance.install(UIUtils.getContext(), downloadInfo);
                        }

                    }
                }

            });
        }




    }



}
