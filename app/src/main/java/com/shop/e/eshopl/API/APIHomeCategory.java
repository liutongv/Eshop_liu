package com.shop.e.eshopl.API;

import android.support.annotation.NonNull;

import com.shop.e.eshopl.entity.HomeCategoryRsp;
import com.shop.e.eshopl.network.NetLinks;
import com.shop.e.eshopl.network.RequestParam;
import com.shop.e.eshopl.network.ResponseEntity;
import com.shop.e.eshopl.util.APIInterface;

/**
 * 服务器接口：首页的分类推荐商品
 * Created by lt on 2017-05-31.
 */

public class APIHomeCategory implements APIInterface{
    @NonNull
    @Override
    public String getPath() {
        return NetLinks.HOME_CATEGORY;
    }

    @NonNull
    @Override
    public RequestParam getRequestParam() {
        return null;
    }

    @NonNull
    @Override
    public Class<? extends ResponseEntity> getResponseEntity() {
        return HomeCategoryRsp.class;
    }
}
