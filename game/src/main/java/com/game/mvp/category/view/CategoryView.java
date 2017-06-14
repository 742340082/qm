package com.game.mvp.category.view;

import com.baselibrary.BaseView;
import com.game.mvp.category.modle.CategoryLink;
import com.game.mvp.category.modle.CategoryResult;

import java.util.List;

/**
 * Created by 74234 on 2017/6/11.
 */

public interface  CategoryView extends BaseView<CategoryResult> {
    void initHeader(List<CategoryLink> categoryLinks);
}
