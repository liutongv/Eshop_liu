package com.shop.e.eshopl.util;

import android.app.Application;

import com.shop.e.eshopl.wrapper.ToastWrapper;

/**
 * Created by lt on 2017-05-26.
 */

public class ShopApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //Toast包装类的初始化
        ToastWrapper.init(this);
    }
}
