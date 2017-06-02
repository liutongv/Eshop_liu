package com.shop.e.eshopl.adapter;

import android.view.View;
import android.widget.TextView;

import com.shop.e.eshopl.R;
import com.shop.e.eshopl.base.BaseListAdapter;
import com.shop.e.eshopl.entity.CategoryPrimary;
import butterknife.BindView;;

/**
 * 一级分类适配器
 * Created by lt on 2017-05-23.
 */

public class CategoryAdapter extends BaseListAdapter<CategoryPrimary,CategoryAdapter.ViewHolder> {


    @Override
    protected int getItemLayout() {
        return R.layout.item_primary_category;
    }

    // viewHolder
    @Override
    protected ViewHolder getItemViewHolder(View view) {
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseListAdapter.ViewHolder {
        @BindView(R.id.text_category)
        TextView mTextCategory;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        // 绑定数据
        @Override
        protected void bind(int position) {
            mTextCategory.setText(getItem(position).getName());
        }
    }
}
