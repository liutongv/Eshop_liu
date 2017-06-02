package com.shop.e.eshopl.entity;

import com.google.gson.annotations.SerializedName;
import com.shop.e.eshopl.network.ResponseEntity;

import java.util.List;

/**
 * 首页商品分类接口响应体.
 */
public class HomeCategoryRsp extends ResponseEntity {


    @SerializedName("data")
    private List<CategoryHome> mData;

    public List<CategoryHome> getData() {
        return mData;
    }

}
