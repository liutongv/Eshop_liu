package com.shop.e.eshopl.wrapper;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.shop.e.eshopl.R;
import com.shop.e.eshopl.base.MyBaseActivity;
import com.shop.e.eshopl.base.MyBaseFragment;

import butterknife.ButterKnife;

/**
 * Toolbar包装类
 * Created by lt on 2017-05-25.
 */

public class ToolbarWrapper {
    private MyBaseActivity mMyBaseActivity;
    private TextView mTvTitle;

    /**
     * activity使用
     * @param activity
     */
    public ToolbarWrapper(MyBaseActivity activity){
        mMyBaseActivity=activity;
        Toolbar toolbar=ButterKnife.findById(activity, R.id.standard_toolbar);
        init(toolbar);
        // 默认标题不展示、有返回箭头
        setShowBack(true);
        setShowTitle(false);
    }


    /**
     * fragment使用
     * @param fragment
     */
    public ToolbarWrapper(MyBaseFragment fragment) {
        mMyBaseActivity = (MyBaseActivity) fragment.getActivity();
        Toolbar toolbar = ButterKnife.findById(fragment.getView(),R.id.standard_toolbar);
        init(toolbar);
        // Fragment设置显示选项菜单
        fragment.setHasOptionsMenu(true);

        // 不显示标题、不显示返回箭头
        setShowBack(false);
        setShowTitle(false);
    }

    /**
     * 绑定TextView和设置ActionBar
     * @param toolbar
     */
    private void init(Toolbar toolbar) {
        // 找到标题的TextView
        mTvTitle = ButterKnife.findById(toolbar, R.id.standard_toolbar_title);
        // 设置toolbar作为actionBar展示
        mMyBaseActivity.setSupportActionBar(toolbar);
    }

    /**
     * 设置标题的是否展示：可以链式调用，返回值是本身
     * @param isShowTitle
     * @return
     */
    public ToolbarWrapper setShowTitle(boolean isShowTitle){
        mMyBaseActivity.getSupportActionBar().setDisplayShowTitleEnabled(isShowTitle);
        return this;
    }

    /**
     * 设置返回箭头的是否展示
     * @param isShowBack
     * @return
     */
    public ToolbarWrapper setShowBack(boolean isShowBack){
        mMyBaseActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(isShowBack);
        return this;
    }

    /**
     * 设置自定义的标题
     * @param resId
     * @return
     */
    public ToolbarWrapper setCustomTitle(int resId){
        if (mTvTitle==null){
            throw new UnsupportedOperationException("No title TextView in toolbar");
        }
        mTvTitle.setText(resId);
        return this;
    }
}
