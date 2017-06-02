package com.shop.e.eshopl.fragment;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shop.e.eshopl.API.APIHomeBanner;
import com.shop.e.eshopl.API.APIHomeCategory;
import com.shop.e.eshopl.R;
import com.shop.e.eshopl.activity.GoodsActivity;
import com.shop.e.eshopl.adapter.HomeGoodsAdapter;
import com.shop.e.eshopl.banner.BannerAdapter;
import com.shop.e.eshopl.banner.BannerLayout;
import com.shop.e.eshopl.base.MyBaseFragment;
import com.shop.e.eshopl.entity.Banner;
import com.shop.e.eshopl.entity.HomeBannerRsp;
import com.shop.e.eshopl.entity.HomeCategoryRsp;
import com.shop.e.eshopl.entity.Picture;
import com.shop.e.eshopl.entity.SimpleGoods;
import com.shop.e.eshopl.network.ResponseEntity;
import com.shop.e.eshopl.util.OkHttpSingle;
import com.shop.e.eshopl.wrapper.PtrWrapper;
import com.shop.e.eshopl.network.UICallBack;
import com.shop.e.eshopl.wrapper.ToolbarWrapper;
import com.squareup.picasso.Picasso;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.GrayscaleTransformation;

/**
 * 首页Fragment
 * Created by Administrator on 2017-05-25.
 */

public class HomeFragment extends MyBaseFragment {

    @BindView(R.id.standard_toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.standard_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.list_home_goods)
    ListView mListHomeGoods;
    @BindView(R.id.standard_refresh_layout)
    PtrFrameLayout mRefreshLayout;
    private ImageView[] mIvPromotes = new ImageView[4];
    private TextView mTvPromotesGoods;
    private BannerAdapter<Banner> mBannerAdapter;
    private HomeGoodsAdapter mGoodsAdapter;
    private boolean mBannerRefresh = false;
    private boolean mCategoryRefresh = false;
    private PtrWrapper mPtrWrapper;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView() {

        // toolbar的处理
        new ToolbarWrapper(this).setCustomTitle(R.string.home_title);

        // 处理刷新相关
        mPtrWrapper = new PtrWrapper(this, false) {
            @Override
            protected void onRefresh() {
                mBannerRefresh = false;
                mCategoryRefresh = false;
                // 刷新数据
                getHomeData();
            }

            @Override
            protected void onLoadMore() {
                // 加载数据
            }
        };
        mPtrWrapper.postRefreshDelayed(50);
        // 设置适配器和头布局
        View view = LayoutInflater.from(getContext()).inflate(R.layout.partial_home_header, mListHomeGoods, false);
        // 找到头布局的控件
        BannerLayout bannerLayout = ButterKnife.findById(view, R.id.layout_banner);
        // 设置适配器
        // 图片的展示
        mBannerAdapter = new BannerAdapter<Banner>() {
            @Override
            protected void bind(ViewHolder holder, Banner data) {
                Picasso.with(getContext()).load(data.getPicture().getLarge()).into(holder.mImageView);
            }
        };
        bannerLayout.setAdapter(mBannerAdapter);

        // 促销商品
        mIvPromotes[0] = ButterKnife.findById(view, R.id.image_promote_one);
        mIvPromotes[1] = ButterKnife.findById(view, R.id.image_promote_two);
        mIvPromotes[2] = ButterKnife.findById(view, R.id.image_promote_three);
        mIvPromotes[3] = ButterKnife.findById(view, R.id.image_promote_four);

        // 促销单品的TextView
        mTvPromotesGoods = ButterKnife.findById(view, R.id.text_promote_goods);
        // 给ListView设置头布局
        mListHomeGoods.addHeaderView(view);
        // 给ListView设置适配器
        mGoodsAdapter = new HomeGoodsAdapter();
        mListHomeGoods.setAdapter(mGoodsAdapter);
    }

    /**
     * 获取数据
     */
    private void getHomeData() {
        // 轮播图和促销单品的请求
        UICallBack homeCallBack = new UICallBack() {
            @Override
            protected void onBusinessResponse(boolean isSuccess, ResponseEntity responseEntity) {
                if (isSuccess) {
                    mBannerRefresh = true;
                    HomeBannerRsp homeBannerRsp = (HomeBannerRsp) responseEntity;
                    // 拿到了数据，首先给Bannerlayout,另外是促销单品
                    mBannerAdapter.reset(homeBannerRsp.getData().getBanners());
                    setPromotesGoods(homeBannerRsp.getData().getGoodsList());
                }
                if (mBannerRefresh && mCategoryRefresh) {
                    // 两个请求都拿到了数据，停止刷新
                    mPtrWrapper.stopRefresh();
                }
            }
        };
        OkHttpSingle.createOkHttp().enqueue(new APIHomeBanner(),homeCallBack,getClass().getSimpleName());
        //分类和推荐商品
        UICallBack categoryCallBack = new UICallBack() {
            @Override
            protected void onBusinessResponse(boolean isSuccess, ResponseEntity responseEntity) {
                mCategoryRefresh = true;
                if (isSuccess) {
                    HomeCategoryRsp homeCategoryRsp = (HomeCategoryRsp) responseEntity;
                    // 拿到了商品分类的数据
                    mGoodsAdapter.reset(homeCategoryRsp.getData());
                }
                if (mBannerRefresh && mCategoryRefresh) {
                    // 两个数据都拿到了，停止刷新
                    mPtrWrapper.stopRefresh();
                }
            }
        };
        OkHttpSingle.createOkHttp().enqueue(new APIHomeCategory(),categoryCallBack,getClass().getSimpleName());
    }

    /**
     * 设置促销单品
     *
     * @param goodsList
     */
    private void setPromotesGoods(List<SimpleGoods> goodsList) {
        mTvPromotesGoods.setVisibility(View.VISIBLE);
        for (int i = 0; i < mIvPromotes.length; i++) {
            mIvPromotes[i].setVisibility(View.VISIBLE);

            // 图片地址的资源填充
            final SimpleGoods simpleGoods = goodsList.get(i);
            Picture picture = simpleGoods.getImg();

            // 数据的填充
            Picasso.with(getContext()).load(picture.getLarge())
                    .transform(new CropCircleTransformation())// 圆形图片
                    .transform(new GrayscaleTransformation())// 灰度
                    .into(mIvPromotes[i]);

            mIvPromotes[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到详情
                    int simpleGoodsId = simpleGoods.getId();
                    Intent intent=GoodsActivity.getStartIntent(getContext(),simpleGoodsId);
                    getActivity().startActivity(intent);
                }
            });
        }
    }
}
