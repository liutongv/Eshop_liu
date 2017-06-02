package com.shop.e.eshopl.util;

/**
 * Created by lt on 2017-05-31.
 */

import android.support.annotation.NonNull;
import com.shop.e.eshopl.network.RequestParam;
import com.shop.e.eshopl.network.ResponseEntity;

/**
 * 请求路径，请求参数，响应体的实体类型
 * 每一个实现类都代表一个服务器接口
 */
public interface APIInterface {
    @NonNull String getPath();  //请求路径
    @NonNull RequestParam getRequestParam();  //请求参数
    @NonNull Class<? extends ResponseEntity> getResponseEntity();  //响应体的实体类型

}
