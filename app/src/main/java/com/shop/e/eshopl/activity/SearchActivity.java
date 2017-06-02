package com.shop.e.eshopl.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shop.e.eshopl.API.APISearch;
import com.shop.e.eshopl.R;
import com.shop.e.eshopl.adapter.SearchAdapter;
import com.shop.e.eshopl.base.MyBaseActivity;
import com.shop.e.eshopl.entity.Filter;
import com.shop.e.eshopl.entity.Paginated;
import com.shop.e.eshopl.entity.Pagination;
import com.shop.e.eshopl.entity.SearchReq;
import com.shop.e.eshopl.entity.SearchRsp;
import com.shop.e.eshopl.entity.SimpleGoods;
import com.shop.e.eshopl.network.NetLinks;
import com.shop.e.eshopl.network.ResponseEntity;
import com.shop.e.eshopl.network.UICallBack;
import com.shop.e.eshopl.util.LogUtils;
import com.shop.e.eshopl.util.OkHttpSingle;
import com.shop.e.eshopl.view.SearchView;
import com.shop.e.eshopl.wrapper.PtrWrapper;
import com.shop.e.eshopl.wrapper.ToastWrapper;
import com.shop.e.eshopl.wrapper.ToolbarWrapper;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import butterknife.OnItemClick;
import okhttp3.Call;

/**
 * 商品搜索
 * Created by lt on 2017-05-26.
 */

public class SearchActivity extends MyBaseActivity{
    private static final String EXTRA_SEARCH_FILTER ="extra_search_filter" ;
    @BindView(R.id.list_goods)
    ListView mGoodsListView;
    @BindViews({R.id.text_is_hot,R.id.text_most_expensive,R.id.text_cheapest})
    List<TextView> mTvOrderList;
    private Filter mFilter;
    @BindView(R.id.search_view)
    SearchView mSearchView;
    PtrWrapper ptrWrapper;
    SearchAdapter adapter;
    public Pagination mPagination=new Pagination();  //分页的参数
    Call mSearchCall;
    Paginated paginated;
    long refreshTime;
    /**
     * 对外提供一个跳转的数据,为了规范数据的传递
     * 转换成字符串传递，作用是不将Filter序列化
     */
    public static Intent getStartIntent(Context context, Filter filter){
        Intent intent=new Intent(context,SearchActivity.class);
        intent.putExtra(EXTRA_SEARCH_FILTER,new Gson().toJson(filter));
        return intent;
    }
    @Override
    public int getContentViewLayout() {
        return R.layout.activity_search_goods;
    }
    @Override
    public void initView() {
       //视图的绑定
        new ToolbarWrapper(this);
        // 一进入，默认选择热销
        mTvOrderList.get(0).setActivated(true);
        // 传递的数据取出来
        String filterStr = getIntent().getStringExtra(EXTRA_SEARCH_FILTER);
        mFilter=new Gson().fromJson(filterStr, Filter.class);
        //刷新和加载的处理
        ptrWrapper=new PtrWrapper(this,true) {
            @Override
            protected void onRefresh() {
                //进行网络数据的刷新
                searchGoods(true);
            }

            @Override
            protected void onLoadMore() {
                //网络数据的加载  判断上次请求的结果里面是不是还有更多数据
                if(paginated.hasMore()){
                   searchGoods(false);
                }else{
                    ptrWrapper.stopRefresh();
                    ToastWrapper.show(R.string.msg_load_more_complete);
                }


            }
        };
        mSearchView.setOnSearchListener(new SearchView.OnSearchListener() {
            @Override
            public void search(String query) {
                mFilter.setKeywords(query);
                ptrWrapper.autoRefresh();
            }
        });
        //数据源  适配器
        adapter=new SearchAdapter();
        mGoodsListView.setAdapter(adapter);
        //自动刷新
        ptrWrapper.postRefreshDelayed(50);
    }
    //跳转到详情页
    @OnItemClick(R.id.list_goods)
    public void goodsItemClick(int position){
        int id = adapter.getItem(position).getId();
        Intent intent = GoodsActivity.getStartIntent(this, id);
        startActivity(intent);
    }

    /**
     * 搜索商品的方法里进行网络获取
     */
    private void searchGoods(boolean isRefresh) {

        if(mSearchCall!=null){
            mSearchCall.cancel();
        }
        if(isRefresh){
            refreshTime = System.currentTimeMillis();
            //刷新的页数从1开始
            mPagination.reset();  //将页数设置为1
            //将ListView定位到第一条
            mGoodsListView.setSelection(0);
        }else{
            mPagination.next();
            LogUtils.debug("Load More Page"+mPagination.getPage());
        }
        SearchReq searchReq=new SearchReq();
        searchReq.setFilter(mFilter);
        searchReq.setPagination(mPagination);
        //获取数据
        UICallBack uiCallBack=new UICallBack() {
            @Override
            protected void onBusinessResponse(boolean isSuccess, ResponseEntity responseEntity) {
                ptrWrapper.stopRefresh();
                mSearchCall=null;
                if(isSuccess){
                    SearchRsp searchRsp= (SearchRsp) responseEntity;
                    //将当前分页结果取出，便于下次加载的时候进行判断是否还需要进行
                    paginated = searchRsp.getPaginated();
                    //获取数据源
                    List<SimpleGoods> goodsList=searchRsp.getData();
                    if(mPagination.isFirst()){
                        //刷新得到的数据
                        adapter.reset(goodsList);
                    }else{
                        adapter.addAll(goodsList);
                    }
                }
            }
        };
        mSearchCall=OkHttpSingle.createOkHttp().enqueue(new APISearch(mFilter,mPagination),uiCallBack,getClass().getSimpleName());
    }

    /**
     * 排序的切换
     */
    @OnClick({R.id.text_is_hot, R.id.text_most_expensive, R.id.text_cheapest})
    public void onClicks(View v){
        //如果当前显示的已经是选择的此项，不再触发
        if(v.isActivated()) return;
        //如果正在刷新，不再触发
        if(ptrWrapper.isRefreshing()) return;
        //将三个都设置为noIsActivated
        for (TextView sortView:mTvOrderList ) {
            sortView.setActivated(false);
        }
        //选择的
        v.setActivated(true);
        //排序字段
        String sortBy = null;
        switch (v.getId()){
            case R.id.text_is_hot:
                sortBy=Filter.SORT_IS_HOT;
                break;
            case R.id.text_most_expensive:
                sortBy=Filter.SORT_PRICE_DESC;  //价格的降序
                break;
            case R.id.text_cheapest:
                sortBy=Filter.SORT_PRICE_ASC; //价格的升序
                break;
        }
        mFilter.setSortBy(sortBy);
        //如果刷新数据，可能会出现由于切换过快导致Tab切换了，而数据没有及时更新
        //若本次刷新和上次刷新之间间隔两秒以上，立即刷新，否则延时刷新
        long time = refreshTime + 2000 - System.currentTimeMillis();
        time=time<0?0:time;
        ptrWrapper.postRefreshDelayed(time);
    }
}
