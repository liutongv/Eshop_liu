package com.shop.e.eshopl.network;

import android.os.Handler;
import android.os.Looper;

import com.shop.e.eshopl.R;
import com.shop.e.eshopl.util.LogUtils;
import com.shop.e.eshopl.util.OkHttpSingle;
import com.shop.e.eshopl.wrapper.ToastWrapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
/**
 * 可以进行UI刷新操作
 * Created by lt on 2017-05-23.
 */

public abstract class UICallBack implements Callback{
    // 创建一个运行在主线程的Handler：通过构造方法中传入主线程的Looper
    Handler mHandler=new Handler(Looper.getMainLooper());
    private Class<? extends ResponseEntity> mResponseType;// 要转换的数据类型
    //运行在后台
    @Override
    public void onFailure(final Call call, final IOException e) {
        //Handler在什么线程，run方法就在什么线程
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //主线程，可以刷新UI
                onFailureUI(call,e);
            }
        });
    }
    //运行在后台
    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
         mHandler.post(new Runnable() {
             @Override
             public void run() {
                 //主线程，可以刷新UI
                 try {
                     onResponseUI(call,response);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         });
    }

    /**
     * 请求失败
     * @param call
     * @param e
     */
    public  void onFailureUI(Call call, IOException e){
        // 对于请求失败进行处理
        ToastWrapper.show(R.string.error_network);
        LogUtils.error("onFailureInUI",e);
        onBusinessResponse(false,null);
    }

    /**
     * 请求成功
     * @param call
     * @param response
     * @throws IOException
     */
    public void onResponseUI(Call call, Response response) throws IOException{
        //首先判断是不是真正的成功
        if(response.isSuccessful()){
            //response转换为具体的实体类  response有具体的实体类型ResponseType
            ResponseEntity responseEntity = OkHttpSingle.createOkHttp().getResponseEntity(response, mResponseType);
            //判断这个类是不是为空
            if(responseEntity==null||responseEntity.getStatus()==null){
                throw new RuntimeException("Fatal Api Error");
            }
            //判断是不是响应数据正确
            if(responseEntity.getStatus().isSucceed()){
                //对外提供一个必须实现的数据处理的方法
                onBusinessResponse(true,responseEntity);
            }else{
                ToastWrapper.show(responseEntity.getStatus().getErrorDesc());
                onBusinessResponse(false,responseEntity);
            }
        }
    }

    /**
     * 给调用者实现的方法，拿到数据并去处理数据
     * @param isSuccess
     * @param responseEntity
     */
    protected abstract void onBusinessResponse(boolean isSuccess, ResponseEntity responseEntity);

    /**
     * 告诉UICallBack要转换的实体类型
     * @param responseType
     */
    public void setResponseType(Class<? extends ResponseEntity> responseType) {
        mResponseType = responseType;
    }
}
