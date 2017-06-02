package com.shop.e.eshopl.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.shop.e.eshopl.R;
import com.shop.e.eshopl.base.BaseListAdapter;
import com.shop.e.eshopl.entity.SimpleGoods;
import com.squareup.picasso.Picasso;
import butterknife.BindView;
/**
 * 搜索商品列表的适配器
 * Created by lt on 2017-05-27.
 */

public class SearchAdapter extends BaseListAdapter<SimpleGoods, SearchAdapter.ViewHolder> {
    @Override
    protected int getItemLayout() {
        return R.layout.item_search_goods;
    }

    @Override
    protected ViewHolder getItemViewHolder(View view) {
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseListAdapter.ViewHolder {
        @BindView(R.id.image_goods)
        ImageView imageGoods;
        @BindView(R.id.text_goods_name)
        TextView textGoodsName;
        @BindView(R.id.text_goods_price)
        TextView textGoodsPrice;
        @BindView(R.id.text_market_price)
        TextView textMarketPrice;
        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(int position) {
            //填充名称和价格
            SimpleGoods simpleGoods = getItem(position);
            textGoodsName.setText(simpleGoods.getName());
            textGoodsPrice.setText(simpleGoods.getShopPrice());
            //设置商场的价格  参用处理文本字符串的一个类:SpannableString
            String marketPrice = simpleGoods.getMarketPrice();
            //传入要处理的字符
            SpannableString spannableString=new SpannableString(marketPrice);
            //设置删除线
            spannableString.setSpan(new StrikethroughSpan(),0,marketPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //处理好的文本设置为textview
            textMarketPrice.setText(spannableString);
            Picasso.with(getContext()).load(simpleGoods.getImg().getLarge()).into(imageGoods);
        }
    }
}
