package com.shop.e.eshopl.API;

import android.support.annotation.NonNull;

import com.shop.e.eshopl.entity.GoodsInfoReq;
import com.shop.e.eshopl.entity.GoodsInfoRsp;
import com.shop.e.eshopl.network.NetLinks;
import com.shop.e.eshopl.network.RequestParam;
import com.shop.e.eshopl.network.ResponseEntity;
import com.shop.e.eshopl.util.APIInterface;

/**
 * 服务器接口：商品详情的请求
 * Created by lt on 2017-05-31.
 */

public class APIGoodsInfo implements APIInterface{
    public GoodsInfoReq goodsInfoReq;

    /**
     * 请求体数据的初始化和请求体数据填充
     * @param goodsId
     */
    public APIGoodsInfo(int goodsId) {
        goodsInfoReq=new GoodsInfoReq();
        goodsInfoReq.setGoodsId(goodsId);
    }

    @NonNull
    @Override
    public String getPath() {
        return NetLinks.GOODS;
    }

    @NonNull
    @Override
    public RequestParam getRequestParam() {
        return goodsInfoReq;
    }

    @NonNull
    @Override
    public Class<? extends ResponseEntity> getResponseEntity() {
        return GoodsInfoRsp.class;
    }
}
