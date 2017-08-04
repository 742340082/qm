package com.game.mvp.detail;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.model.game.Game;
import com.baselibrary.model.game.detail.summary.GameSummaryRelate;
import com.baselibrary.model.game.detail.summary.GameSummaryResult;
import com.baselibrary.model.game.detail.summary.GameSummaryTagS;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.GameUtil;
import com.baselibrary.utils.RxBus;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.baselibrary.view.FlowLayout;
import com.baselibrary.view.ProgressArc;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.game.R;
import com.game.R2;
import com.game.adapter.gamedetail.GameDetailGameNews;
import com.game.adapter.gamedetail.GameDetailRecommend;
import com.game.config.ConfigGame;
import com.game.mvp.detail.presenter.GameDetailPresenter;
import com.game.mvp.detail.view.GameDetailSummaryView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by 74234 on 2017/7/27.
 */

public class GameSummaryFragment extends BaseFragmnet implements GameDetailSummaryView {
    //游戏信息
    @BindView(R2.id.iv_game_icon)
    ImageView iv_game_icon;
    @BindView(R2.id.tv_game_title)
    TextView tv_game_title;
    @BindView(R2.id.tv_game_version)
    TextView tv_game_version;
    @BindView(R2.id.tv_game_language)
    TextView tv_game_language;
    @BindView(R2.id.tv_game_download_count)
    TextView tv_game_download_count;
    @BindView(R2.id.rb_game_score)
    RatingBar rb_game_score;
    @BindView(R2.id.tv_game_score)
    TextView tv_game_score;
    @BindView(R2.id.tv_game_download_size)
    TextView tv_game_download_size;
    @BindView(R2.id.pba_gamedownload)
    ProgressArc pba_gamedownload;

    //游戏描述信息

    @BindView(R2.id.rl_detail_toggle)
    RelativeLayout rl_detail_toggle;
    @BindView(R2.id.tv_detail_des)
    TextView tv_detail_des;
    @BindView(R2.id.tv_game_detail_descinfo)
    TextView tv_game_detail_descinfo;
    @BindView(R2.id.iv_arrow)
    ImageView iv_arrow;


    //游戏标签
    @BindView(R2.id.fl_game_detail_gametag)
    FlowLayout fl_game_detail_gametag;
    //游戏资讯
    @BindView(R2.id.rv_game_detail_gamenews)
    RecyclerView rv_game_detail_gamenews;
    //游戏推荐
    @BindView(R2.id.rv_game_detail_recommend)
    RecyclerView rv_game_detail_recommend;

    @BindView(R2.id.fl_game_detail_summary)
    FrameLayout fl_game_detail_summary;

    @BindView(R2.id.ll_game_detail_desinfo)
    LinearLayout ll_game_detail_desinfo;
//    @BindView(R2.id.ll_game_detail_gameinfo)
//    LinearLayout ll_game_detail_gameinfo;
    @BindView(R2.id.ll_game_detail_gamenews)
    LinearLayout ll_game_detail_gamenews;
    @BindView(R2.id.ll_game_detail_gametag)
    LinearLayout ll_game_detail_gametag;
    @BindView(R2.id.ll_game_detail_recommend)
    LinearLayout ll_game_detail_recommend;
    @BindView(R2.id.ll_game_detail_summary)
    LinearLayout ll_game_detail_summary;





    private StatusLayoutManager mStatusLayoutManager;
    private GameDetailPresenter mPresenter;
    private String mGameId;
    private String mPackageName;
    private GameSummaryResult result;
    private boolean isOpen;
    private ViewGroup.LayoutParams mParams;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_game_detail_summary;
    }

    @Override
    public void initData() {
        mGameId = getArguments().getString(ConfigGame.GAME_SEND_GAMEDETAIL_ID);
        mPackageName = getArguments().getString(ConfigGame.GAME_SEND_GAMEDETAIL_PACKAGE);
        mPresenter = new GameDetailPresenter(this);
        mPresenter.initGameDetail(mGameId, mPackageName);
    }

    @Override
    public void initView() {
        ll_game_detail_summary.setVisibility(View.GONE);
        mStatusLayoutManager = StatusLayoutManager.newBuilder(getContext())
                .emptyDataView(R.layout.state_empty)
                .errorView(R.layout.state_error)
                .loadingView(R.layout.state_loading)
                .netWorkErrorView(R.layout.state_network_error)
                .retryViewId(R.id.iv_content_error)
                .onRetryListener(new OnRetryListener() {
                    @Override
                    public void retry() {
                        mPresenter.initGameDetail(mGameId, mPackageName);
                    }
                })
                .build();
        fl_game_detail_summary.addView(mStatusLayoutManager.getRootLayout(), fl_game_detail_summary.getChildCount() - 1);
    }

    @Override
    public void initListener() {
        rl_detail_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }

    @Override
    public void error(int error, String errorMessage) {
        ToastUtils.makeShowToast(getContext(), errorMessage);
        ConfigStateCodeUtil.error(error, mStatusLayoutManager);
    }

    @Override
    public void loading() {
        mStatusLayoutManager.showLoading();
    }

    @Override
    public void success(GameSummaryResult data) {
        // 将TextView高度设置为截断7行之后的高度
        result=data;
        mParams = tv_detail_des.getLayoutParams();
        mParams.height = getShortHeight();
        tv_detail_des.setLayoutParams(mParams);



        ll_game_detail_summary.setVisibility(View.VISIBLE);
        mStatusLayoutManager.showContent();

        RxBus.getDefault().post(ConfigGame.GAME_SEND_RX_DETAIL, data);
        //初始化游戏信息
        Glide.with(UIUtils.getContext())
                .load(data.getIcopath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_game_icon);
        tv_game_title.setText(data.getAppname());
        tv_game_version.setText(data.getVersion());

        tv_game_language.setText(GameUtil.getLanaguage(data.getLanguage()));
        tv_game_download_count.setText(GameUtil.downloadCountSwitch(0, data.getNum_download()));
        rb_game_score.setRating(Float.parseFloat(data.getComment().getScore()) / 2.0f);
        tv_game_score.setText(data.getComment().getScore());
        tv_game_download_size.setText(GameUtil.byteSwitch(2, data.getSize_byte()));

        //初始化游戏描述
        tv_detail_des.setText(data.getAppinfo());
        //初始化游戏资讯
        List<GameSummaryRelate> gameRelate = data.getGameRelate();
        if (gameRelate==null||gameRelate.size()==0)
        {
            ll_game_detail_gamenews.setVisibility(View.GONE);
        }else
        {

            GameDetailGameNews gameDetailGameNews=new GameDetailGameNews(R.layout.item_game_detail_summary_gamenews,gameRelate);
            gameDetailGameNews.openLoadAnimation(GameDetailGameNews.ALPHAIN);
            rv_game_detail_gamenews.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_game_detail_gamenews.setAdapter(gameDetailGameNews);
        }

        //初始化游戏标签
        List<GameSummaryTagS> tags = data.getTags();
        for (GameSummaryTagS tag: tags)
        {
            View view =  LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_game_detail_summary_gametag, null);
            TextView tv_game_detail_summary_gametag = (TextView) view.findViewById(R.id.tv_game_detail_summary_gametag);
            tv_game_detail_summary_gametag.setText(tag.getName());
            fl_game_detail_gametag.addView(view);
        }

        //初始化相关推荐
        List<Game> recommendGames = data.getRecommendGames();
        if (recommendGames==null||recommendGames.size()==0)
        {
            ll_game_detail_recommend.setVisibility(View.GONE);
        }else
        {
            GameDetailRecommend gameDetailRecommend=new GameDetailRecommend(R.layout.item_game_detail_summary_recommend,recommendGames);
            gameDetailRecommend.openLoadAnimation(GameDetailGameNews.ALPHAIN);
            rv_game_detail_recommend.setLayoutManager(new GridLayoutManager(getContext(),4));
            rv_game_detail_recommend.setAdapter(gameDetailRecommend);
        }

    }

    /**
     * 获取完整展示时的高度
     *
     * @return
     */
    private int getLongHeight() {
        int measuredWidth = tv_detail_des.getMeasuredWidth();

        // 结合模式和具体值,定义一个宽度和高度的参数
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth,
                View.MeasureSpec.EXACTLY);// 宽度填充屏幕,已经确定, 所以是EXACTLY
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(2000,
                View.MeasureSpec.AT_MOST);// 高度不确定, 模式是包裹内容, 有多高展示多高, 所以是AT_MOST.
        // 这里的2000是高度最大值, 也可以设置为屏幕高度

        // 模拟一个TextView
        TextView view = new TextView(UIUtils.getContext());
        view.setText(result.getAppinfo());
        // tvDes得到的规则要作用在模拟的textView上,保持其高度一致
        view.measure(widthMeasureSpec, heightMeasureSpec);

        // 返回测量的高度
        return view.getMeasuredHeight();
    }

    /**
     * 展开或收起描述信息
     */
    protected void toggle() {
        int shortHeight = getShortHeight();
        int longHeight = getLongHeight();

        ValueAnimator animator = null;
        if (!isOpen) {
            isOpen = true;
            if (shortHeight < longHeight) {
                animator = ValueAnimator.ofInt(shortHeight, longHeight);
            }
        } else {
            isOpen = false;
            if (shortHeight < longHeight) {
                animator = ValueAnimator.ofInt(longHeight, shortHeight);
            }
        }

        if (animator != null) {
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator arg0) {
                    int height = (Integer) arg0.getAnimatedValue();
                    mParams.height = height;
                    tv_detail_des.setLayoutParams(mParams);
                }
            });

            animator.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator arg0) {

                }

                @Override
                public void onAnimationRepeat(Animator arg0) {

                }

                @Override
                public void onAnimationEnd(Animator arg0) {
                    if (isOpen) {
                        iv_arrow.setImageResource(R.drawable.icon_game_arrow_top);
                        tv_game_detail_descinfo.setText("收起");
                    } else {
                        iv_arrow.setImageResource(R.drawable.icon_game_arrow_down);
                        tv_game_detail_descinfo.setText("展开");
                    }
                }

                @Override
                public void onAnimationCancel(Animator arg0) {

                }
            });

            animator.setDuration(200);
            animator.start();
        }
    }


    /**
     * 获取截断7行之后的高度
     *
     * @return
     */
    private int getShortHeight() {
        int measuredWidth = tv_detail_des.getMeasuredWidth();

        // 结合模式和具体值,定义一个宽度和高度的参数
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth,
                View.MeasureSpec.EXACTLY);// 宽度填充屏幕,已经确定, 所以是EXACTLY
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(2000,
                View.MeasureSpec.AT_MOST);// 高度不确定, 模式是包裹内容, 有多高展示多高, 所以是AT_MOST.
        // 这里的2000是高度最大值, 也可以设置为屏幕高度

        // 模拟一个TextView
        TextView view = new TextView(UIUtils.getContext());
        view.setMaxLines(4);// 最多展示7行
        view.setText(result.getAppinfo());
        // tvDes得到的规则要作用在模拟的textView上,保持其高度一致
        view.measure(widthMeasureSpec, heightMeasureSpec);

        // 返回测量的高度
        return view.getMeasuredHeight();
    }
}
