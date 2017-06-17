package com.game.adapter;

import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.R;
import com.game.config.ConfigGame;
import com.game.mvp.category.detail.CategoryDetailActivity;
import com.game.mvp.category.modle.CategoryData;
import com.game.mvp.category.modle.CategoryTag;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 74234 on 2017/6/11.
 */

public class CategoryAdapter extends BaseQuickAdapter<CategoryData,BaseViewHolder> {
    public CategoryAdapter(int layoutResId) {
        super(layoutResId);
    }

    public CategoryAdapter(int layoutResId, List<CategoryData> data) {

        super(layoutResId, data);
    }
    //记录TextView数量
    private static  int k;
    //记录LinearLayout数量
    private  int t;
    @Override
    protected void convert(BaseViewHolder helper,final CategoryData item) {
        LinearLayout convertView = (LinearLayout) helper.getConvertView();

        RelativeLayout relativeLayout = (RelativeLayout) convertView.getChildAt(0);
        CircleImageView civ_category_icon = (CircleImageView) relativeLayout.getChildAt(0);
        TextView tv_category_title = (TextView) relativeLayout.getChildAt(1);
        TextView tv_category_new_count = (TextView) relativeLayout.getChildAt(2);
        tv_category_title.setText(item.getName());

        SpannableStringBuilder builder = new SpannableStringBuilder("新增 "+item.getNew_count()+" 款");
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(UIUtils.getColor(R.color.red));
        builder.setSpan(blueSpan, 3, 3+(item.getNew_count()+"").length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tv_category_new_count.setText(builder);
        Glide.with(UIUtils.getContext())
                .load(item.getIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(civ_category_icon);
        List<CategoryTag> tags = item.getTags();
        t=0;
        for (int i=0;i<convertView.getChildCount();i++)
        {

            if (i!=0)
            {

                View childView =  convertView.getChildAt(i);
                if (childView instanceof LinearLayout)
                {
                    k+=t*4;
                    LinearLayout linearLayout= (LinearLayout) childView;
                    for (int j=0;j<linearLayout.getChildCount();j++)
                    {

                        View view =  linearLayout.getChildAt(j);
                        if (view instanceof TextView)
                        {
                           final TextView textView = (TextView) view;
                            textView.setText(tags.get(k).getName());

                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(UIUtils.getContext(), CategoryDetailActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra(ConfigGame.GAME_SEND_CATEGORY_DETAID_KID,item.getData_id());
                                    intent.putExtra(ConfigGame.GAME_SEND_CATEGORY_TAG,textView.getText().toString());
                                    UIUtils.getContext().startActivity(intent);
                                }
                            });

                            k++;
                        }

                    }
                    t++;
                }
                k=0;
            }
        }

        RelativeLayout relativeLayout1 = helper.getView(R.id.Rl_game_category);
        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UIUtils.getContext(), CategoryDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(ConfigGame.GAME_SEND_CATEGORY_DETAID_KID,item.getData_id());
                intent.putExtra(ConfigGame.GAME_SEND_CATEGORY_TAG,"全部");
                UIUtils.getContext().startActivity(intent);
            }
        });
    }
}
