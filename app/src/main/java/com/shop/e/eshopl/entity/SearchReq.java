package com.shop.e.eshopl.entity;


import com.google.gson.annotations.SerializedName;
import com.shop.e.eshopl.network.RequestParam;

// 搜索商品的请求体
public class SearchReq extends RequestParam {

    @SerializedName("filter") private Filter mFilter;

    @SerializedName("pagination") private Pagination mPagination;

    public void setFilter(Filter filter) {
        mFilter = filter;
    }

    public void setPagination(Pagination pagination) {
        mPagination = pagination;
    }
}
