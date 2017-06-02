package com.shop.e.eshopl.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 所有ListView适配器的基类
 * Created by lt on 2017-05-25.
 */

public abstract class BaseListAdapter<T,V extends BaseListAdapter.ViewHolder> extends BaseAdapter{
    //数据源
    private List<T> mData=new ArrayList<>();
    /**
     * 数据的填充
     */
    public void reset(List<T> data){
        mData.clear();
        if(data!=null) mData.addAll(data);
        notifyDataSetChanged();
    }
    /**
     * 数据的添加
     */
    public void addAll(List<T> data){
        if(data!=null) mData.addAll(data);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mData==null?0:mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=LayoutInflater.from(parent.getContext()).inflate(getItemLayout(),parent,false);
            viewHolder=getItemViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder= (ViewHolder) convertView.getTag();
        //绑定数据，对外提供一个方法，通过子类来实现
        viewHolder.bind(position);
        return convertView;
    }

    /**
     * 子条目布局
     */
   @LayoutRes protected abstract int getItemLayout();
    /**
     * 告诉我们子类具体的ViewHolder
     */
    protected abstract V getItemViewHolder(View view);
    /**
     * 处理ViewHolder视图相关
     */
    public abstract class ViewHolder{
        View mItemView;
        public ViewHolder(View itemView){
            ButterKnife.bind(this,itemView);
            mItemView=itemView;
        }
        protected abstract void bind(int position);

        protected final Context getContext(){
            return mItemView.getContext();
        }
    }
}
