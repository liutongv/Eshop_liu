package com.shop.e.eshopl.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.shop.e.eshopl.R;
import com.shop.e.eshopl.base.MyBaseActivity;
import com.shop.e.eshopl.fragment.CategoryFragment;
import com.shop.e.eshopl.fragment.HomeFragment;
import com.shop.e.eshopl.util.TestFragment;
import butterknife.BindView;

/**
 * 主界面
 */
public class MainActivity extends MyBaseActivity implements OnTabSelectListener {
    @BindView(R.id.bb_main_bottom_bar)
    BottomBar mBottomBar;

    private HomeFragment mHomeFragment;   //首页
    private CategoryFragment mCategoryFragment;   //分类
    private TestFragment mCartFragment;
    private TestFragment mMineFragment;

    private Fragment mCurrentFragment;// 记录一下当前展示的Fragment

    // 布局的填充
    @Override
    public int getContentViewLayout() {
        return R.layout.activity_main;
    }

    // 视图的处理
    @Override
    public  void initView() {
        // 看一下FragmentManager里面是不是已经有了这些Fragment,如果有，手动恢复
        retrieveFragment();
        // 设置底部导航的选择监听
        mBottomBar.setOnTabSelectListener(this);
    }

    // 恢复因为系统重建造成的Fragment销毁后重新恢复
    private void retrieveFragment() {
        FragmentManager manager = getSupportFragmentManager();
        mHomeFragment = (HomeFragment) manager.findFragmentByTag(HomeFragment.class.getName());
        mCategoryFragment = (CategoryFragment) manager.findFragmentByTag(CategoryFragment.class.getName());
        mCartFragment = (TestFragment) manager.findFragmentByTag("CartFragment");
        mMineFragment = (TestFragment) manager.findFragmentByTag("MineFragment");
    }

    // 某一项底部导航栏被选择的时候会触发
    @Override
    public void onTabSelected(@IdRes int tabId) {
        // 根据tabId,处理不同的事件：切换展示不同的Fragment
        switch (tabId){
            case R.id.tab_home:   //首页
                if (mHomeFragment==null){
                    mHomeFragment = HomeFragment.newInstance();
                }
                // 切换Fragment
                switchFragment(mHomeFragment);
                break;
            case R.id.tab_category:   //分类

                if (mCategoryFragment==null){
                    mCategoryFragment = CategoryFragment.newInstance();
                }
                // 切换
                switchFragment(mCategoryFragment);
                break;
            case R.id.tab_cart:   //购物车
                if (mCartFragment==null){
                    mCartFragment = TestFragment.newInstance("CartFragment");
                }
                switchFragment(mCartFragment);
                break;
            case R.id.tab_mine:   //我的
                if (mMineFragment==null){
                    mMineFragment = TestFragment.newInstance("MineFragment");
                }
                switchFragment(mMineFragment);
                break;
            default:
                throw new UnsupportedOperationException("unSupport");
        }
    }

    /**
     * Fragment的切换
     * 第一种：replace
     * 第二种：add、show、hide
     * @param fragment
     */
    private void switchFragment(Fragment fragment) {
        /**
         * 1. 判断目标的fragment是不是已经在显示的状态
         * 2. 如果不是，隐藏当前展示的Fragment
         * 3. 判断目标的Fragment有没有添加到FragmentManager里面
         * 4. 重新记录一下当前的Fragment
         */
        if (mCurrentFragment==fragment) return;
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        // 隐藏当前Fragment
        if (mCurrentFragment!=null){
            transaction.hide(mCurrentFragment);
        }
        if (fragment.isAdded()){
            // 如果已经添加到FragmentManager里面，就展示
            transaction.show(fragment);
        }else {
            // 为了方便找到Fragment，设置Tag
            String tag;
            // 做一个判断，添加不同的tag
            if (fragment instanceof TestFragment){
                tag = ((TestFragment)fragment).getArgText();
            }else {
                // 将fragment的类名作为tag
                tag = fragment.getClass().getName();
            }
            // 添加
            transaction.add(R.id.fl_four_fragment,fragment,tag);
        }
        //提交
        transaction.commit();
        // 重新记录一下当前的Fragment
        mCurrentFragment = fragment;
    }

    // 处理一下返回键
    @Override
    public void onBackPressed() {
        if (mCurrentFragment!=mHomeFragment){
            // 如果不在首页上，就切换到首页
            mBottomBar.selectTabWithId(R.id.tab_home);
            return;
        }
        // 如果不是首页，不关闭程序，退到后台运行
        moveTaskToBack(true);
    }
}
