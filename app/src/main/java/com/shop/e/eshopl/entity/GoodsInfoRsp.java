package com.shop.e.eshopl.entity;
import com.google.gson.annotations.SerializedName;
import com.shop.e.eshopl.network.ResponseEntity;

// 商品详情的响应体
public class GoodsInfoRsp extends ResponseEntity {

    @SerializedName("data")
    private GoodsInfo mData;

    public GoodsInfo getData() {
        return mData;
    }

}
