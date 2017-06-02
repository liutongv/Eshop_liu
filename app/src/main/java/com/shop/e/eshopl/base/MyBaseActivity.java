package com.shop.e.eshopl.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;

import com.shop.e.eshopl.util.OkHttpSingle;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lt on 2017-05-23.
 */

public abstract class MyBaseActivity extends TransitionActivity{
    private Unbinder mUnbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置视图
        setContentView(getContentViewLayout());
        mUnbinder=ButterKnife.bind(this);
        initView();
    }
    /**
     * 处理视图
     */
    public abstract void initView();
    /**
     * 由子类设置展示的视图
     */
    @LayoutRes public abstract int getContentViewLayout();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消请求
        OkHttpSingle.createOkHttp().cancelByTag(getClass().getSimpleName());
        mUnbinder.unbind();  //解绑
        mUnbinder=null;
    }
}
