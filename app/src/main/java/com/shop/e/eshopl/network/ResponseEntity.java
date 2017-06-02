package com.shop.e.eshopl.network;

import com.google.gson.annotations.SerializedName;
import com.shop.e.eshopl.entity.Status;

/**
 * 响应体的实体基类，为了防止直接实例化
 * Created by lt on 2017-05-26.
 */

public abstract class ResponseEntity {
    // 每一个响应的实体类都有Status，提取到基类中
    @SerializedName("status")
    private Status mStatus;

    public Status getStatus() {
        return mStatus;
    }
}
