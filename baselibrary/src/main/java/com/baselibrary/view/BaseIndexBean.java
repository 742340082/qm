package com.baselibrary.view;

/**
 * Created by 74234 on 2017/5/2.
 */

public abstract class BaseIndexBean implements ISuspensionInterface {
    public  abstract boolean isSuspension();
    public  abstract String getSuspension();
    @Override
    public boolean isShowSuspension() {
        return this.isSuspension();
    }

    @Override
    public String getSuspensionTag() {
        return getSuspension();
    }
}
