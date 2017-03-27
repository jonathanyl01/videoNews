package com.example.administrator.myvideonews.bombapi;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class BombIntercepted implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //拦截到的请求
        Request request = chain.request();
        //请求的构造器
        Request.Builder builder = request.newBuilder();
        //用于让服务器区分是哪一个应用
        BombConst bombConst = new BombConst();
        builder.addHeader("X-Bmob-Application-Id",bombConst.APPLICATION_ID);
        //用于授权
        builder.addHeader("X-Bmob-REST-API-Key",bombConst.REST_API_KEY);
        //请求和响应统一使用Json格式
        builder.addHeader("Content-Type","application/json");
        //构建的到添加完自定义请求头的请求
        request = builder.build();
        //执行请求，拿到响应
        Response response = chain.proceed(request);



        return response;
    }
}
