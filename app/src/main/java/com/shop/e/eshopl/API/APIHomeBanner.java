package com.shop.e.eshopl.API;

import android.support.annotation.NonNull;

import com.shop.e.eshopl.entity.HomeBannerRsp;
import com.shop.e.eshopl.network.NetLinks;
import com.shop.e.eshopl.network.RequestParam;
import com.shop.e.eshopl.network.ResponseEntity;
import com.shop.e.eshopl.util.APIInterface;

/**
 * 服务器接口：首页的轮播图和促销单品
 * Created by lt on 2017-05-31.
 */

public class APIHomeBanner implements APIInterface{

    @NonNull
    @Override
    public String getPath() {
        return NetLinks.HOME_DATA;
    }

    @NonNull
    @Override
    public RequestParam getRequestParam() {
        return null;
    }

    @NonNull
    @Override
    public Class<? extends ResponseEntity> getResponseEntity() {
        return HomeBannerRsp.class;
    }
}
