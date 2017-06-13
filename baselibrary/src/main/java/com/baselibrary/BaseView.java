package com.baselibrary;

public  interface BaseView<T> {
    void error(int error, String errorMessage);
    void loading();
    void success(T data);
}
