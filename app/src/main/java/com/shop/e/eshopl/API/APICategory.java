package com.shop.e.eshopl.API;

import android.support.annotation.NonNull;

import com.shop.e.eshopl.entity.CategoryRsp;
import com.shop.e.eshopl.network.NetLinks;
import com.shop.e.eshopl.network.RequestParam;
import com.shop.e.eshopl.network.ResponseEntity;
import com.shop.e.eshopl.util.APIInterface;

/**
 * 服务器接口：获取商品的分类
 * Created by lt on 2017-05-31.
 */

public class APICategory implements APIInterface{
    @NonNull
    @Override
    public String getPath() {
        return NetLinks.CATEGORY;
    }

    @NonNull
    @Override
    public RequestParam getRequestParam() {
        return null;
    }

    @NonNull
    @Override
    public Class<? extends ResponseEntity> getResponseEntity() {
        return CategoryRsp.class;
    }
}
