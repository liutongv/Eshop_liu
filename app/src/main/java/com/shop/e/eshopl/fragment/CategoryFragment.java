package com.shop.e.eshopl.fragment;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import com.shop.e.eshopl.API.APICategory;
import com.shop.e.eshopl.R;
import com.shop.e.eshopl.activity.SearchActivity;
import com.shop.e.eshopl.adapter.CategoryAdapter;
import com.shop.e.eshopl.adapter.CategoryChildAdapter;
import com.shop.e.eshopl.base.MyBaseFragment;
import com.shop.e.eshopl.entity.CategoryBase;
import com.shop.e.eshopl.entity.CategoryPrimary;
import com.shop.e.eshopl.entity.CategoryRsp;
import com.shop.e.eshopl.entity.Filter;
import com.shop.e.eshopl.network.ResponseEntity;
import com.shop.e.eshopl.util.OkHttpSingle;
import com.shop.e.eshopl.network.UICallBack;
import com.shop.e.eshopl.wrapper.ToolbarWrapper;
import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;

/**
 * 分类Fragment
 * Created by lt on 2017-05-23.
 */

public class CategoryFragment extends MyBaseFragment {
    @BindView(R.id.standard_toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.standard_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.lv_category)
    ListView mListCategory;
    @BindView(R.id.lv_categoryChildren)
    ListView mListChildren;
    //一级分类数据源
    public List<CategoryPrimary> mData;
    //一级分类适配器
    CategoryAdapter mCategoryAdapter;
    //子分类数据源
    List<CategoryBase> mListChild;
    //子分类适配器
    CategoryChildAdapter mChildrenAdapter;

    /**
     * 布局填充
     * @return
     */
    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_category;
    }

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }
    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        initToolBar();
        // listView设置适配器
        mCategoryAdapter = new CategoryAdapter();
        mListCategory.setAdapter(mCategoryAdapter);
        mChildrenAdapter = new CategoryChildAdapter();
        mListChildren.setAdapter(mChildrenAdapter);

        // 去拿数据
        if (mData != null) {
            // 直接去更新UI
            updateCategory();

        } else {
            // 去进行网络请求获取数据
            UICallBack uiCallBack = new UICallBack() {
                @Override
                protected void onBusinessResponse(boolean isSuccess, ResponseEntity responseEntity) {
                    if (isSuccess) {
                        CategoryRsp categoryRsp = (CategoryRsp) responseEntity;
                        mData = categoryRsp.getData();
                        // 数据有了之后更新UI：拿到的Data是一级分类的信息，一级分类里面又包括二级分类
                        // 数据先给一级分类，默认选择一级分类的第一条，二级分类数据才能展示。
                        updateCategory();
                    }
                }
            };
            OkHttpSingle.createOkHttp().enqueue(new APICategory(),uiCallBack,getClass().getSimpleName());
        }

    }

    // 更新分类数据:数据的填充
    private void updateCategory() {
        mCategoryAdapter.reset(mData);
        // 切换展示二级分类
        chooseCategory(0);
    }

    // 根据一级分类的某一项展示二级分类
    private void chooseCategory(int position) {
        mListCategory.setItemChecked(position, true);
        // 填充二级分类
        mChildrenAdapter.reset(mCategoryAdapter.getItem(position).getChildren());
    }

    // 点击一级分类的item：切换展示二级分类的信息
    @OnItemClick(R.id.lv_category)
    public void onItemClick(int position) {
        chooseCategory(position);
    }

    // 点击二级分类的item：跳转页面
    @OnItemClick(R.id.lv_categoryChildren)
    public void onChildrenClick(int position) {
        //跳转到搜索的页面
        int id=mChildrenAdapter.getItem(position).getId();
        goToSearchActity(id);
    }
    /**
     * toolBar的处理
     */
    public void initToolBar() {
        // 利用包装好的toolbar
        new ToolbarWrapper(this).setCustomTitle(R.string.category_title);
    }

    /**
     * 处理搜索图标的展示
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_category, menu);
    }

    /**
     * 处理选项菜单
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //搜索图标
        if (id == R.id.menu_search) {
            int position=mListCategory.getCheckedItemPosition();
            int categoryId=mCategoryAdapter.getItem(position).getId();
            goToSearchActity(categoryId);
            return true;
        }
        //返回箭头
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * 写一个跳转方法
     */
    private void goToSearchActity(int categoryId){
        //根据ID跳转页面
        Filter filter=new Filter();
        filter.setCategoryId(categoryId);
        Intent intent=SearchActivity.getStartIntent(getContext(),filter);
        getActivity().startActivity(intent);
    }

}
