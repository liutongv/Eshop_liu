package com.shop.e.eshopl.entity;

import com.google.gson.annotations.SerializedName;
import com.shop.e.eshopl.network.ResponseEntity;

import java.util.List;

/**
 * 暂时使用的商品分类的响应体
 * Created by Administrator on 2017-05-26.
 */

// 暂时使用的商品分类的响应体
public class CategoryRsp extends ResponseEntity{

    @SerializedName("data")
    private List<CategoryPrimary> mData;

    public List<CategoryPrimary> getData() {
        return mData;
    }
}
