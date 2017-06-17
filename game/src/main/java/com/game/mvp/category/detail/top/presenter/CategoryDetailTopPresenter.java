package com.game.mvp.category.detail.top.presenter;

import com.game.mvp.category.detail.top.biz.CategoryDetailTopBiz;
import com.game.mvp.category.detail.top.biz.CategoryDetailTopBizImpl;
import com.game.mvp.category.detail.top.view.CategoryDetailTopView;

/**
 * Created by 74234 on 2017/6/15.
 */

public class CategoryDetailTopPresenter {

    private final CategoryDetailTopBiz categoryDetailBiz;

    public CategoryDetailTopPresenter(CategoryDetailTopView categoryDetailView)
    {
        categoryDetailBiz = new CategoryDetailTopBizImpl(categoryDetailView);
    }

    public void initCategoryDetail(int kid,int startSize,int endSize,int page,String subtype,int tagId)
    {
        categoryDetailBiz.initCategoryDetail(kid,startSize,endSize,page,subtype,tagId);
    }
}
