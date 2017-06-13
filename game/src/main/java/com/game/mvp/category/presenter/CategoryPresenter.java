package com.game.mvp.category.presenter;

import com.game.mvp.category.biz.CategoryBiz;
import com.game.mvp.category.biz.CategoryBizImpl;
import com.game.mvp.category.view.CategoryView;

/**
 * Created by 74234 on 2017/6/11.
 */

public class CategoryPresenter {
    private CategoryBiz categoryBiz;
    public CategoryPresenter(CategoryView categoryView)
    {
        categoryBiz=new CategoryBizImpl(categoryView);
    }
    public void initGameCategory()
    {
        categoryBiz.initGameCategory();
    }
}
