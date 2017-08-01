package com.game.mvp.category.view;


import com.baselibrary.BaseView;
import com.baselibrary.model.game.category.CategoryLink;
import com.baselibrary.model.game.category.CategoryResult;

import java.util.List;

/**
 * Created by 74234 on 2017/6/11.
 */

public interface  CategoryView extends BaseView<CategoryResult> {
    void initHeader(List<CategoryLink> categoryLinks);
}
