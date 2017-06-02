package com.shop.e.eshopl.API;

import android.support.annotation.NonNull;

import com.shop.e.eshopl.entity.Filter;
import com.shop.e.eshopl.entity.Pagination;
import com.shop.e.eshopl.entity.SearchReq;
import com.shop.e.eshopl.entity.SearchRsp;
import com.shop.e.eshopl.network.NetLinks;
import com.shop.e.eshopl.network.RequestParam;
import com.shop.e.eshopl.network.ResponseEntity;
import com.shop.e.eshopl.util.APIInterface;

/**
 * 服务器接口：搜索商品
 * POST请求：有请求体
 * Created by lt on 2017-05-31.
 */

public class APISearch implements APIInterface{
    public SearchReq searchReq;//请求体

    public APISearch(Filter filter, Pagination pagination) {
        searchReq=new SearchReq();
        searchReq.setFilter(filter);
        searchReq.setPagination(pagination);
    }

    @NonNull
    @Override
    public String getPath() {
        return NetLinks.SEARCH;
    }

    @NonNull
    @Override
    public RequestParam getRequestParam() {
        return searchReq;
    }

    @NonNull
    @Override
    public Class<? extends ResponseEntity> getResponseEntity() {
        return SearchRsp.class;
    }
}
