package com.shop.e.eshopl.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.shop.e.eshopl.API.APIGoodsInfo;
import com.shop.e.eshopl.R;
import com.shop.e.eshopl.adapter.GoodsAdapter;
import com.shop.e.eshopl.base.MyBaseActivity;
import com.shop.e.eshopl.entity.GoodsInfo;
import com.shop.e.eshopl.entity.GoodsInfoRsp;
import com.shop.e.eshopl.network.ResponseEntity;
import com.shop.e.eshopl.network.UICallBack;
import com.shop.e.eshopl.util.GoodsPopupWindow;
import com.shop.e.eshopl.util.OkHttpSingle;
import com.shop.e.eshopl.wrapper.ToastWrapper;
import com.shop.e.eshopl.wrapper.ToolbarWrapper;
import java.util.List;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * 商品详情界面
 * Created by lt on 2017-05-31.
 */

public class GoodsActivity extends MyBaseActivity implements ViewPager.OnPageChangeListener {
    private static final String EXTRA_GOODS_ID = "GOODS_ID";
    @BindView(R.id.pager_goods)
    ViewPager mGoodsAdapter;
    @BindViews({R.id.text_tab_goods, R.id.text_tab_details, R.id.text_tab_comments})
    List<TextView> mTvTabList;
    private GoodsInfo goodsInfo;
    private GoodsPopupWindow goodsPopupWindow;

    /**
     * 对外提供一个跳转的方法
     */
    public static Intent getStartIntent(Context context, int goodsId) {
        Intent intent = new Intent(context, GoodsActivity.class);
        intent.putExtra(EXTRA_GOODS_ID, goodsId);
        return intent;
    }

    /**
     * 布局的填充
     */
    @Override
    public int getContentViewLayout() {
        return R.layout.activity_goods;
    }

    /**
     * 视图的初始化
     */
    @Override
    public void initView() {
        //toolbar的处理
        new ToolbarWrapper(this);
        //ViewPager设置监听
        mGoodsAdapter.addOnPageChangeListener(this);
        //拿到传递的数据
        int goodsId=getIntent().getIntExtra(EXTRA_GOODS_ID, 0);
        //获取数据
        OkHttpSingle.createOkHttp().enqueue(new APIGoodsInfo(goodsId),mGoodsInfoCallBack,getClass().getSimpleName());

    }
   private UICallBack mGoodsInfoCallBack=new UICallBack() {
       @Override
       protected void onBusinessResponse(boolean isSuccess, ResponseEntity responseEntity) {
           if(isSuccess){
               GoodsInfoRsp goodsInfoRsp= (GoodsInfoRsp) responseEntity;
               goodsInfo = goodsInfoRsp.getData();
               mGoodsAdapter.setAdapter(new GoodsAdapter(getSupportFragmentManager(), goodsInfo));
               //默认选择tab第一项
               chooseTab(0);
           }
       }
   };
    /**
     * 改变选中的tab的样式
     * 选择器和字体大小
     */
    private void chooseTab(int position) {
        Resources resources = getResources();
        for (int i = 0; i < mTvTabList.size(); i++) {
            mTvTabList.get(i).setSelected(i == position);
            //改变字体的大小
            float textSize = i == position ? resources.getDimension(R.dimen.font_large)
                    : resources.getDimension(R.dimen.font_normal);
            mTvTabList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        }
    }

    /**
     * 三个TextView的tab切换事件
     */
    @OnClick({R.id.text_tab_goods, R.id.text_tab_details, R.id.text_tab_comments})
    public void onClickTab(TextView textview) {
        //需要知道点击的是哪一项  viewPager要切换到当前项  改变选中的样式
        int position = mTvTabList.indexOf(textview);
        mGoodsAdapter.setCurrentItem(position, false);
        chooseTab(position);
    }

    /**
     * button的点击事件
     * @param view
     */
    @OnClick({R.id.button_add_cart, R.id.button_buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_cart:
                showPopupWindow();
                break;
            case R.id.button_buy:
                showPopupWindow();
                break;
        }
    }
    /**
     * 展示商品选择的弹框
     */
    public void showPopupWindow(){
        if(goodsInfo==null)    return;
        if(goodsPopupWindow==null){
            goodsPopupWindow = new GoodsPopupWindow(this, goodsInfo);
        }
        goodsPopupWindow.show(new GoodsPopupWindow.OnConfirmListener() {
            @Override
            public void onConfirm(int number) {
                //具体操作
                ToastWrapper.show("Confirm:"+number);
                goodsPopupWindow.dismiss();
            }
        });

    }
    /**
     * 填充选项菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_goods,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 处理选项菜单的子条目选择事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_share){
            ToastWrapper.show(R.string.action_share);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /*----------------------------------ViewPager监听重写的方法----------------------------------*/
    /**
     * 滑动中
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 页面选择
     */
    @Override
    public void onPageSelected(int position) {
        chooseTab(position);
    }

    /**
     * 页面滑动状态变化后
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 对外提供一个切换页面的方法
     * @param position
     */
    public void selectPage(int position) {
        mGoodsAdapter.setCurrentItem(position,false);
        chooseTab(position);
    }
}
