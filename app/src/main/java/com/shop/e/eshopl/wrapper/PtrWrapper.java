package com.shop.e.eshopl.wrapper;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.shop.e.eshopl.R;

import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicDefaultFooter;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 下拉刷新和加载的包装
 * Created by lt on 2017-05-25.
 */

public abstract class PtrWrapper {
    private PtrFrameLayout mRefreshLayout;

    /**
     * Activity中刷新
     * @param activity
     * @param isNeedLoad
     */
    public PtrWrapper(Activity activity,boolean isNeedLoad){
        mRefreshLayout= ButterKnife.findById(activity, R.id.standard_refresh_layout);
        initPtr(isNeedLoad);
    }

    /**
     * Fragment中刷新
     * @param fragment
     * @param isNeedLoad
     */
    public PtrWrapper(Fragment fragment, boolean isNeedLoad){
        mRefreshLayout=ButterKnife.findById(fragment.getView(),R.id.standard_refresh_layout);
        initPtr(isNeedLoad);
    }

    /**
     * 初始化操作
     * @param isNeedLoad
     */
    private void initPtr(boolean isNeedLoad){
        if(mRefreshLayout!=null){
            mRefreshLayout.disableWhenHorizontalMove(true);
        }
        //设置刷新的布局:头布局
        initPtrHeader();
        //需要加载，设置尾部布局:加载的布局
        if(isNeedLoad){
            initPtrFooter();
        }
       //设置刷新和加载的Handler
        mRefreshLayout.setPtrHandler(mPtrHandler);

    }

    /**
     * 刷新和加载的Handler
     */
    private PtrDefaultHandler2 mPtrHandler=new PtrDefaultHandler2() {
        /**
         * 加载会触发
         * @param frame
         */
        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            onLoadMore();
        }
        /**
         * 刷新会触发
         * @param frame
         */
        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            onRefresh();
        }
    };

    /**
     * 设置尾部布局
     */
    private void initPtrFooter() {
        PtrClassicDefaultFooter footer=new PtrClassicDefaultFooter(mRefreshLayout.getContext());
        mRefreshLayout.setFooterView(footer);
        mRefreshLayout.addPtrUIHandler(footer);
    }

    /**
     * 设置头布局
     */
    private void initPtrHeader() {
        PtrClassicDefaultHeader header=new PtrClassicDefaultHeader(mRefreshLayout.getContext());
        mRefreshLayout.setHeaderView(header);
        mRefreshLayout.addPtrUIHandler(header);
    }

    /**
     * 自动刷新的方法
     */
    public void autoRefresh(){
        mRefreshLayout.autoRefresh();
    }

    /**
     * 延时自动刷新
     * @param delay
     */
    public void postRefreshDelayed(long delay){
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.autoRefresh();
            }
        },delay);
    }

    /**
     * 停止刷新
     */
    public void stopRefresh(){
        if(isRefreshing()){
            mRefreshLayout.refreshComplete();
        }
    }

    /**
     * 是不是正在刷新
     * @return
     */
    public boolean isRefreshing(){
        return mRefreshLayout.isRefreshing();
    }

    /**
     * 让调用者必须处理刷新的事件
     */
    protected abstract void onRefresh();

    /**
     * 让调用者必须处理加载的事件
      */
    protected  abstract void onLoadMore();
}
