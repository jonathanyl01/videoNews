package com.example.administrator.myvideonews.bombapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/3/16 0016.
 */

public class BombClient {

    private static BombClient bombClient;

    public static BombClient getInstance(){
        if (bombClient == null){
            bombClient = new BombClient();

        }
        return bombClient;

    }
    private OkHttpClient okHttpClient;
    private NewsApi newsApi;
    private Retrofit retrofit;
    private UserApi userApi;

    public UserApi getUserApi() {

        if (userApi == null){
            userApi = retrofit.create(UserApi.class);

        }

        return userApi;
    }

    public NewsApi getNewsApi() {
        if (newsApi == null){
            newsApi = retrofit.create(NewsApi.class);

        }
        return newsApi;
    }

    private BombClient(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                //添加Bomb必要的头字段的拦截器
                .addInterceptor(new BombIntercepted())
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-mm-dd HH:mm:ss")
                .create();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.bmob.cn/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


    }

  /*  public Call register(String username,String password){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("username",username);
            jsonObject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(null,json);

        Request request = new Request.Builder()
                .url("https://api.bmob.cn/1/users")
                //用于让bomb服务器，区分是哪一个应用
                .addHeader("X-Bmob-Application-Id", "623aaef127882aed89b9faa348451da3")
                //用于授权
                .addHeader("X-Bmob-REST-API-Key", "c00104962a9b67916e8cbcb9157255de")
                //请求和响应统一使用json格式
                .addHeader("Content-Type","application/json")
                .post(requestBody)
                .build();

        return okHttpClient.newCall(request);


    }


    public Call login(String username,String password){


        Request request = new Request.Builder()
                //              请求方式（get，post）(默认是Get请求)
                .get()
                //              请求路径（服务器接口），
                .url("https://api.bmob.cn/1/login" + "?"
                        +"username=" + username + "&"
                        +"password=" + password)
                //              请求头信息（根据服务器需要）
                //用于让bomb服务器，区分是哪一个应用
                .addHeader("X-Bmob-Application-Id", "623aaef127882aed89b9faa348451da3")
                //用于授权
                .addHeader("X-Bmob-REST-API-Key", "c00104962a9b67916e8cbcb9157255de")
                //请求和响应统一使用json格式
                .addHeader("Content-Type","application/json")
//                      构建出来
                .build();



        return okHttpClient.newCall(request);

    }*/


}
