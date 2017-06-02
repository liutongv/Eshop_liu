package com.shop.e.eshopl.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.shop.e.eshopl.entity.GoodsInfo;
import com.shop.e.eshopl.fragment.GoodsInfoFragment;
import com.shop.e.eshopl.util.TestFragment;

/**
 * 商品页面的适配器
 * Created by lt on 2017-05-31.
 */

public class GoodsAdapter extends FragmentPagerAdapter{
    public GoodsInfo mGoodsInfo;//数据源
    public GoodsAdapter(FragmentManager fm,GoodsInfo mGoodsInfo) {
        super(fm);
        this.mGoodsInfo=mGoodsInfo;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                //商品页面
                return GoodsInfoFragment.newInstance(mGoodsInfo);
            case 1:
                //详情
                return TestFragment.newInstance("商品详情");
            case 2:
                //评价
                return TestFragment.newInstance("商品评价");
            default:
                throw new UnsupportedOperationException("Position:"+position);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
