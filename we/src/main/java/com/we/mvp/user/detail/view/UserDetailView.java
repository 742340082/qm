package com.we.mvp.user.detail.view;


import com.baselibrary.BaseView;
import com.we.mvp.user.modle.User;

import java.util.List;

public  interface UserDetailView extends BaseView<List<User>>
{
      void uploadProgress(long paramLong1, long paramLong2);
}
