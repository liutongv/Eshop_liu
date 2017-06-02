package com.shop.e.eshopl.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.gson.Gson;
import com.shop.e.eshopl.R;
import com.shop.e.eshopl.activity.GoodsActivity;
import com.shop.e.eshopl.banner.BannerAdapter;
import com.shop.e.eshopl.base.MyBaseFragment;
import com.shop.e.eshopl.entity.GoodsInfo;
import com.shop.e.eshopl.entity.Picture;
import com.squareup.picasso.Picasso;
import butterknife.BindView;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

/**
 * 商品的详细信息
 * Created by lt on 2017-06-01.
 */

public class GoodsInfoFragment extends MyBaseFragment {

    private static final String GOODS_INFO = "goods_info";
    GoodsInfo mGoodsInfo;
    @BindView(R.id.pager_goods_pictures)
    ViewPager pagerGoodsPictures;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.button_favorite)
    ImageButton buttonFavorite;
    @BindView(R.id.text_goods_name)
    TextView textGoodsName;
    @BindView(R.id.text_goods_price)
    TextView textGoodsPrice;
    @BindView(R.id.text_market_price)
    TextView textMarketPrice;
    /**
     * 提供一个创建方法，帮我们传递数据
     */
    public static GoodsInfoFragment newInstance(@NonNull GoodsInfo goodsInfo) {
        GoodsInfoFragment goodsInfoFragment = new GoodsInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GOODS_INFO, new Gson().toJson(goodsInfo));
        goodsInfoFragment.setArguments(bundle);
        return goodsInfoFragment;
    }

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_goods_info;
    }

    @Override
    public void initView() {
        //拿到传递的数据
        String string = getArguments().getString(GOODS_INFO);
        mGoodsInfo = new Gson().fromJson(string, GoodsInfo.class);
        //处理ViewPager
        BannerAdapter<Picture> adapter=new BannerAdapter<Picture>() {
            @Override
            protected void bind(ViewHolder holder, Picture data) {
                //数据和视图的绑定
                Picasso.with(getContext()).load(data.getLarge()).into(holder.mImageView);
            }
        };
        //设置适配器的数据
        adapter.reset(mGoodsInfo.getPictures());
        pagerGoodsPictures.setAdapter(adapter);
        //给圆点指示器设置ViewPager
        indicator.setViewPager(pagerGoodsPictures);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());

        //商品的名称
        textGoodsName.setText(mGoodsInfo.getName());
        //商品的价格
        textGoodsPrice.setText(mGoodsInfo.getShopPrice());
        //商品的市场价格  添加删除线
        String marketPrice = mGoodsInfo.getMarketPrice();
        SpannableString spannableString=new SpannableString(marketPrice);
        spannableString.setSpan(new StrikethroughSpan(),0,marketPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textMarketPrice.setText(spannableString);
        //是否加入收藏
        buttonFavorite.setSelected(mGoodsInfo.isCollected());
    }

    @OnClick({R.id.text_goods_details, R.id.text_goods_comments})
    public void onViewClicked(View view) {
        //通过GoodsActivity来完成切换，在activity中提供一个切换的方法
        GoodsActivity goodsActivity= (GoodsActivity) getActivity();
        switch (view.getId()) {
            case R.id.text_goods_details:
                //切换到商品详情Fragment
                goodsActivity.selectPage(1);
                break;
            case R.id.text_goods_comments:
                //切换到商品评价Fragment
                goodsActivity.selectPage(2);
                break;
        }
    }
}
