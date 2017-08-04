package com.game.adapter.gamedownload;

import android.content.Context;
import android.content.DialogInterface;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselibrary.api.download.DownloadAidl;
import com.baselibrary.api.download.DownloadInfo;
import com.baselibrary.api.download.HttpDownload;
import com.baselibrary.base.activity.MyApplication;
import com.baselibrary.model.game.Game;
import com.baselibrary.utils.GameUtil;
import com.baselibrary.utils.UIUtils;
import com.baselibrary.utils.ViewUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.R;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static okhttp3.internal.Internal.instance;

/**
 * Created by 74234 on 2017/7/20.
 */

public class GameDownloadSuccessAdapter extends BaseQuickAdapter<DownloadInfo, BaseViewHolder> {
    private DownloadAidl downloadAidl;
    private Context context;

    public GameDownloadSuccessAdapter(int layoutResId, List<DownloadInfo> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final DownloadInfo downloadInfo) {
        downloadAidl = MyApplication.getDownloadAidl();
        final int[] downloadState = {downloadInfo.getDownloadState()};
        View convertView = helper.getConvertView();
        ViewUtil.initCutOff(helper,helper.getLayoutPosition());
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("你确定删除该任务吗");
                alertDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            downloadAidl.cancle(downloadInfo);
                            getData().remove(helper.getLayoutPosition());
                            GameDownloadSuccessAdapter.this.notifyItemRemoved(helper.getLayoutPosition());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
                alertDialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
                return false;
            }
        });

        RelativeLayout rl_game_top = helper.getView(R.id.rl_game_top);
        rl_game_top.setVisibility(View.GONE);
        ImageView iv_game_top_icon = helper.getView(R.id.iv_game_top_icon);
        TextView tv_game_top_title = helper.getView(R.id.tv_game_top_title);

        TextView tv_game_top_download_number = helper.getView(R.id.tv_game_top_download_number);
        TextView tv_game_top_tag_size = helper.getView(R.id.tv_game_top_tag_size);
        TextView tv_game_top_type = helper.getView(R.id.tv_game_top_type);


        final Button btn_game_download = helper.getView(R.id.btn_game_download);
        btn_game_download.setText("安装");
        final Game game = downloadInfo.getDownloadInfo();


        Glide.with(UIUtils.getContext())
                .load(game.getIcopath())
                .bitmapTransform(new RoundedCornersTransformation(UIUtils.getContext(),24,0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_game_top_icon);
        tv_game_top_title.setText(game.getAppname());

        tv_game_top_download_number.setText(GameUtil.downloadCountSwitch(0, game.getNum_download()));
        tv_game_top_tag_size.setText(GameUtil.byteSwitch(0, game.getSize_byte()));
        tv_game_top_type.setText(game.getKind_name());




        btn_game_download.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // 根据当前状态来决定相关操作
                try {
                    if (downloadState[0] == HttpDownload.STATE_NONE || downloadState[0] == HttpDownload.STATE_ERROR
                            || downloadState[0] == HttpDownload.STATE_CANCLE || downloadState[0] == HttpDownload.STATE_PAUSE
                            ) {
                        // 开始下载
                        if (downloadInfo != null && instance != null) {
                            downloadAidl.download(downloadInfo);
                        }

                    } else if (downloadState[0] == HttpDownload.STATE_WAITING
                            || downloadState[0] == HttpDownload.STATE_DOWNLOAD) {
                        // 暂停下载
                        if (downloadInfo != null && instance != null) {
                            downloadAidl.pause(downloadInfo);
                        }

                    } else if (downloadState[0] == HttpDownload.STATE_SUCCESS) {
                        // 开始安装
                        if (downloadInfo != null && instance != null) {
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
