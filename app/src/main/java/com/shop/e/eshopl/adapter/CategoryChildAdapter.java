package com.shop.e.eshopl.adapter;

import android.view.View;
import android.widget.TextView;

import com.shop.e.eshopl.R;
import com.shop.e.eshopl.base.BaseListAdapter;
import com.shop.e.eshopl.entity.CategoryBase;
import butterknife.BindView;

/**
 * Created by lt on 2017-05-23.
 */

public class CategoryChildAdapter extends BaseListAdapter<CategoryBase,CategoryChildAdapter.ViewHolder> {
    @Override
    protected int getItemLayout() {
        return R.layout.item_children_category;
    }

    // 子类的具体的ViewHolder
    @Override
    protected CategoryChildAdapter.ViewHolder getItemViewHolder(View view) {
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseListAdapter.ViewHolder{

        @BindView(R.id.text_category_child)
        TextView mTextCategory;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        // 数据的绑定
        @Override
        protected void bind(int position) {
            mTextCategory.setText(getItem(position).getName());
        }
    }
}
