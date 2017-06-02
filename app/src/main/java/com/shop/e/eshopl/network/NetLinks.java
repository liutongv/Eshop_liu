package com.shop.e.eshopl.network;

/**
 * 所有链接
 * Created by lt on 2017-05-22.
 */

public class NetLinks {
    //接口基础URL
    public static final String BASE_LINKS="http://106.14.32.204/eshop/emobile/?url=";
    //轮播图和促销商品
    public static final String HOME_DATA=BASE_LINKS+"/home/data";
    //商品的一级分类和推荐
    public static final String HOME_CATEGORY=BASE_LINKS+"/home/category";
    //商品的分类和搜索
    public static final String SEARCH=BASE_LINKS+"/search";
    //商品分类
    public static final String CATEGORY=BASE_LINKS+"/category";
    //获取品牌
    public static final String BRAND=BASE_LINKS+"/brand";
    //获取价格区间
    public static final String PRICE_RANGE=BASE_LINKS+"/price_range";
    //商品详情与收藏
    public static final String GOODS=BASE_LINKS+"/goods";
    //商品描述
    public static final String GOODS_DESC=BASE_LINKS+"/goods/desc";
    //收藏商品
    public static final String USER_COLLECT_CREATE=BASE_LINKS+"/user/collect/create";
    //取消收藏商品
    public static final String USER_COLLECT_DELETE=BASE_LINKS+"/user/collect/delete";
    //获取收藏列表
    public static final String USER_COLLECT_LIST=BASE_LINKS+"/user/collect/list";
    //用户登录
    public static final String USER_LOGIN=BASE_LINKS+"/user/signin";
    //获取注册字段
    public static final String USER_SIGNUPFIELDS=BASE_LINKS+"/user/signupFields";
    //用户注册
    public static final String USER_REG=BASE_LINKS+"/user/signup";

}
