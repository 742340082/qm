package com.game.mvp.search;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.base.adapter.CommonShakeAdapter;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.FileUtil;
import com.baselibrary.utils.StringUtil;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.baselibrary.view.ClearEditText;
import com.baselibrary.view.shake.StellarMap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.game.R;
import com.game.R2;
import com.game.adapter.SearchAdapter;
import com.game.adapter.SearchFirstAdapter;
import com.game.adapter.SmallSearchAdapter;
import com.game.mvp.model.Game;
import com.game.mvp.search.model.GameSearchFirstResult;
import com.game.mvp.search.model.GameSearchHotWord;
import com.game.mvp.search.model.GameSearchResult;
import com.game.mvp.search.model.GameSmallSearchResult;
import com.game.mvp.search.presenter.GameSearchPresenter;
import com.game.mvp.search.view.GameSearchView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by 74234 on 2017/6/17.
 */

public class GameSearchActivity extends BaseActivity implements GameSearchView, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.et_game_search)
    ClearEditText et_game_search;
    @BindView(R2.id.stm_game_search_tags)
    StellarMap stm_game_search_tags;
    @BindView(R2.id.btn_game_switch_search)
    Button btn_game_switch_search;
    @BindView(R2.id.rv_game_small_search)
    RecyclerView rv_game_small_search;
    @BindView(R2.id.rv_game_search)
    RecyclerView rv_game_search;
    @BindView(R2.id.ll_game_search)
    LinearLayout ll_game_search;
    @BindView(R2.id.rl_game_search)
    RelativeLayout rl_game_search;
    @BindView(R2.id.srl_game_search)
    SwipeRefreshLayout srl_game_search;
    @BindView(R2.id.iv_game_search)
    ImageView iv_game_search;

    private GameSearchPresenter mPresenter;
    private SmallSearchAdapter smallSearchAdapter;
    private GameSearchResult searchResult;
    private StatusLayoutManager mStatusLayoutManager;
    private SearchAdapter searchAdapter;
    //当前聪明搜索的游戏名称
    private String currentSearchText;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_game_serarch;
    }

    @Override
    public void initView() {
        toolbar.inflateMenu(R.menu.game_menu_search);
        mStatusLayoutManager = StatusLayoutManager.newBuilder(this)
                .emptyDataView(R.layout.state_empty)
                .errorView(R.layout.state_error)
                .loadingView(R.layout.state_loading)
                .netWorkErrorView(R.layout.state_network_error)
                .retryViewId(R.id.iv_content_error)
                .onRetryListener(new OnRetryListener() {
                    @Override
                    public void retry() {
                        mPresenter.search(currentSearchText, 1);
                    }
                })
                .build();
        ll_game_search.addView(mStatusLayoutManager.getRootLayout(), ll_game_search.getChildCount() - 1);
        mStatusLayoutManager.showContent();
    }

    @Override
    public void initListener() {

        rl_game_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_game_search.setBackgroundColor(UIUtils.getColor(R.color.viewBackground));
                if (rv_game_small_search.getVisibility() == View.VISIBLE) {
                    rv_game_small_search.setVisibility(View.GONE);
                }
            }
        });

        iv_game_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        srl_game_search.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.search(currentSearchText, 1);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.menu_game_search) {
                    searchResult = null;
                    currentSearchText = et_game_search.getText().toString();
                    if (!StringUtil.isEmpty(currentSearchText)) {
                        mPresenter.search(currentSearchText, 1);
                    } else {
                        ToastUtils.makeShowToast(UIUtils.getContext(), "请输入搜索的内容");
                    }

                }
                return false;
            }
        });

        btn_game_switch_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stm_game_search_tags.zoomOut();
            }
        });
        et_game_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                //完成自己的事件
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    currentSearchText = et_game_search.getText().toString();
                    //点击搜索要做的操作
                    mPresenter.search(currentSearchText, 1);
                }
                return false;
            }
        });

        et_game_search.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence watcher, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence textWatcher, int start, int before, int count) {
               String currentSearchText = et_game_search.getText().toString().trim();

                if (!StringUtil.isEmpty(currentSearchText))
                {
                    Observable.just(currentSearchText)
                            .debounce(200L, TimeUnit.MILLISECONDS)
                            .switchMap(new Function<String, ObservableSource<String>>() {
                                @Override
                                public ObservableSource<String> apply(String str) throws Exception {
                                    return Observable.just(str);
                                }
                            }).subscribe(new Consumer<String>() {
                        public void accept(@NonNull String searchText) {
                            mPresenter.smallSearchGame(searchText);
                        }
                    });
                }else
                {
                    currentSearchText=null;
                    rv_game_small_search.setVisibility(View.GONE);
                    rl_game_search.setBackgroundColor(UIUtils.getColor(R.color.viewBackground));
                }


            }
        });
    }

    @Override
    public void initData() {
        mPresenter = new GameSearchPresenter(this);
        mPresenter.initGameSearch();
    }


    @Override
    public void onBackPressed() {
        if (rv_game_search.getVisibility() == View.VISIBLE || rv_game_small_search.getVisibility() == View.VISIBLE) {
            searchResult = null;
            srl_game_search.setVisibility(View.GONE);
            rv_game_search.setVisibility(View.GONE);
            rv_game_small_search.setVisibility(View.GONE);
            stm_game_search_tags.setVisibility(View.VISIBLE);
            btn_game_switch_search.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void error(int error, String errorMessage) {
        ToastUtils.makeShowToast(UIUtils.getContext(), errorMessage);
        ConfigStateCodeUtil.error(error, mStatusLayoutManager);
    }

    @Override
    public void loading() {
        if (searchResult == null) {
            mStatusLayoutManager.showLoading();
        }
    }


    @Override
    public void success(GameSearchFirstResult data) {
        // 设置数据源
        SearchFirstAdapter searchFirstAdapter = new SearchFirstAdapter(R.layout.item_game_search_tag, data.getHotWords());
        // 设置内部文字距边缘边距为10dip
        int dip2px = UIUtils.dip2px(10);
        stm_game_search_tags.setInnerPadding(dip2px, dip2px, dip2px, dip2px);
        stm_game_search_tags.setAdapter(searchFirstAdapter);
        // 设定展示规则,9行6列(具体以随机结果为准)
        stm_game_search_tags.setRegularity(6, 6);
        // 设置默认组为第0组
        stm_game_search_tags.setGroup(0, true);

        searchFirstAdapter.setOnItemClickListener(new CommonShakeAdapter.OnItemClickListener() {
            @Override
            public void itemClick(CommonShakeAdapter adapter, View view, int postion) {
                GameSearchHotWord gameSearchHotWord = (GameSearchHotWord) adapter.getItem(postion);
                if (StringUtil.isEmpty(gameSearchHotWord.getIcopath())) {
                    currentSearchText=gameSearchHotWord.getWord();
                    searchResult = null;
                    stm_game_search_tags.setVisibility(View.GONE);
                    btn_game_switch_search.setVisibility(View.GONE);
                    rv_game_small_search.setVisibility(View.GONE);
                    srl_game_search.setVisibility(View.VISIBLE);
                    rv_game_search.setVisibility(View.VISIBLE);
                    et_game_search.setText(gameSearchHotWord.getWord());
                    mPresenter.search(gameSearchHotWord.getWord(), 1);
                }
            }
        });


    }

    @Override
    public void successSmallSearch(GameSmallSearchResult gameSmallSearchResult) {
        if (gameSmallSearchResult!=null) {
            smallSearchAdapter.addData(gameSmallSearchResult.getData());
            smallSearchAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initSamllSearchHeader(Game game) {
        if (game!=null) {
            rv_game_small_search.setVisibility(View.VISIBLE);
            rl_game_search.setBackgroundColor(UIUtils.getColor(R.color.transparentGray));

            View viewHeander = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_game_top_total, new FrameLayout(UIUtils.getContext()));
            RelativeLayout rl_game_top = (RelativeLayout) viewHeander.findViewById(R.id.rl_game_top);
            ImageView iv_game_top_icon = (ImageView) viewHeander.findViewById(R.id.iv_game_top_icon);
            TextView tv_game_top_title = (TextView) viewHeander.findViewById(R.id.tv_game_top_title);
            TextView tv_game_top_download_number = (TextView) viewHeander.findViewById(R.id.tv_game_top_download_number);
            TextView tv_game_top_tag_size = (TextView) viewHeander.findViewById(R.id.tv_game_top_tag_size);
            TextView tv_game_top_type = (TextView) viewHeander.findViewById(R.id.tv_game_top_type);
            Button btn_download = (Button) viewHeander.findViewById(R.id.btn_game_download);

            rl_game_top.setVisibility(View.GONE);
            Glide.with(UIUtils.getContext())
                    .load(game.getIcopath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(iv_game_top_icon);
            tv_game_top_title.setText(game.getAppname());
            String size = FileUtil.byteSwitch(2, game.getSize_byte());
            tv_game_top_tag_size.setText(size);
            String downloadNum = FileUtil.downloadCountSwitch(0, game.getNum_download());
            tv_game_top_download_number.setText(downloadNum);
            tv_game_top_type.setVisibility(View.GONE);

            smallSearchAdapter = new SmallSearchAdapter(R.layout.item_game_smallsearch);
            smallSearchAdapter.openLoadAnimation(SmallSearchAdapter.ALPHAIN);
            rv_game_small_search.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
            rv_game_small_search.setAdapter(smallSearchAdapter);
            smallSearchAdapter.addHeaderView(viewHeander);


            smallSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    searchResult = null;
                    Game game1 = (Game) adapter.getItem(position);
                    currentSearchText = game1.getAppname();
                    et_game_search.setText(game1.getAppname());
                    mPresenter.search(currentSearchText, 1);
                }
            });
        }else
        {
            rv_game_small_search.setVisibility(View.GONE);
            rl_game_search.setBackgroundColor(UIUtils.getColor(R.color.viewBackground));
        }
    }

    @Override
    public void searchSuccess(GameSearchResult gameSearchResult) {
        stm_game_search_tags.setVisibility(View.GONE);
        btn_game_switch_search.setVisibility(View.GONE);
        rv_game_small_search.setVisibility(View.GONE);
        srl_game_search.setVisibility(View.VISIBLE);
        rv_game_search.setVisibility(View.VISIBLE);


        srl_game_search.setRefreshing(false);
        mStatusLayoutManager.showContent();
        rv_game_small_search.setVisibility(View.GONE);
        rl_game_search.setBackgroundColor(UIUtils.getColor(R.color.viewBackground));
        searchResult = gameSearchResult;
        if (searchResult.getPage() == 1) {
            searchAdapter = new SearchAdapter(R.layout.item_game_index_normal, searchResult.getData());
            searchAdapter.setEnableLoadMore(true);
            searchAdapter.setOnLoadMoreListener(this, rv_game_search);
            searchAdapter.openLoadAnimation(SearchAdapter.SLIDEIN_LEFT);
            rv_game_search.setLayoutManager(new LinearLayoutManager(this));
            rv_game_search.setAdapter(searchAdapter);
        } else {
            searchAdapter.addData(searchResult.getData());
            searchAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (searchAdapter.getItemCount() >= searchResult.getCount()) {
            searchAdapter.loadMoreEnd();
        } else {
            mPresenter.search(currentSearchText, searchResult.getStartKey());
        }
    }
}
