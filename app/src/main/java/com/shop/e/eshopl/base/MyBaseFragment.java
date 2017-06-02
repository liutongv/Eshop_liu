package com.shop.e.eshopl.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shop.e.eshopl.util.OkHttpSingle;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 通用Fragment的父类
 * Created by lt on 2017-05-23.
 */

public abstract class MyBaseFragment extends Fragment{
    private Unbinder mUnbinder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentViewLayout(), container, false);
        mUnbinder=ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

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
    public void onDestroy() {
        super.onDestroy();
        //取消请求
        OkHttpSingle.createOkHttp().cancelByTag(getClass().getSimpleName());
        mUnbinder.unbind();
        mUnbinder=null;
    }
}
