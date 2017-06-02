package com.shop.e.eshopl.util;

import com.google.gson.Gson;
import com.shop.e.eshopl.network.ResponseEntity;
import com.shop.e.eshopl.network.UICallBack;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * OkHttp进行单例优化
 * 网络核心类
 * Created by lt on 2017-05-22.
 */

public class OkHttpSingle {
    private final OkHttpClient mOkHttpClient;
    private static OkHttpSingle mOkHttpSingle;
    private Gson mGson;
    /**
     * 私有的构造方法
     */
    private OkHttpSingle() {
        mGson=new Gson();
        //日志拦截的创建
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //初始化OKHttpClient
        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    /**
     * 公有的创建方法
     */
    public static synchronized OkHttpSingle createOkHttp() {
        if (mOkHttpSingle == null) {
            mOkHttpSingle = new OkHttpSingle();
        }
        return mOkHttpSingle;
    }

    /**
     * 同步方法
     * 请求的构建(地址、方式、请求体)、数据的解析(具体解析的响应体的类型)都写到这个方法里面
     */
    public <T extends ResponseEntity> T excute(APIInterface apiInterface) throws IOException {
        // 构建请求
        Response response = newApiCall(apiInterface,null).execute();

        // 解析写到一个方法里面
        Class<T> clazz= (Class<T>) apiInterface.getResponseEntity();
        return getResponseEntity(response,clazz);
    }

    /**
     * 异步回调的方法：请求的构建，数据的解析(callback里面完成)
     * 1.构建请求
     * 2.执行：异步回调的方法
     * 3.在UICallBack重写的方法中：处理请求成功与失败
     * 4.在UICallBack重写的方法中：请求得到的响应体的数据解析，最终得到响应实体类
     *
     * 构建异步回调的时候
     * 请求构建(地址，请求体)
     * 数据的解析(Callback中有response，没有具体的实体类数据，需要我们告诉callback要换的类型
     * 没有具体的实体类数据，需要我们告诉callback要换的类型
     */
    public Call enqueue(APIInterface apiInterface,
                        UICallBack uiCallback,String tag){

        // 构建请求
        Call call = newApiCall(apiInterface,tag);
        // 告诉uicallback要转换的数据类型是什么
        uiCallback.setResponseType(apiInterface.getResponseEntity());
        // 为了规范，在当前的方法中直接执行
        call.enqueue(uiCallback);
        return call;
    }

    /**
     * 根据响应Response，将响应体解析为响应的具体的实体类
     * @param response
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public  <T extends ResponseEntity> T getResponseEntity(Response response, Class<T> clazz) throws IOException {
        // 没有成功
        if (!response.isSuccessful()){
            throw new IOException("Response code is "+response.code());
        }
        //成功
        return mGson.fromJson(response.body().string(),clazz);
    }

    /**
     * 构建请求的方法
     * @return
     */
    private Call newApiCall(APIInterface apiInterface,String tag) {

        // 请求拆开
        Request.Builder builder = new Request.Builder();

        builder.url(apiInterface.getPath());// 请求地址

        // 如果请求体不为空，是post请求
        if (apiInterface.getRequestParam()!=null){
            // 构建post请求
            String json = mGson.toJson(apiInterface.getRequestParam());
            RequestBody requestBody = new FormBody.Builder()
                    .add("json",json)
                    .build();
            builder.post(requestBody);
        }
        builder.tag(tag); //为了方便call模型的取消
        Request request = builder.build();
        return mOkHttpClient.newCall(request);
    }

    /**
     * 取消的方法
     * 在请求的时候添加tag，通过tag判断是否取消
     */
    public void cancelByTag(String tag){
        //获取
        for(Call call:mOkHttpClient.dispatcher().queuedCalls()){
            if(call.request().tag().equals(tag)){
                call.cancel();
            }
        }
        for (Call call:mOkHttpClient.dispatcher().runningCalls()){
            if(call.request().tag().equals(tag)){
                call.cancel();
            }
        }
    }

}
