package com.example.administrator.myvideonews.bombapi;

import com.example.administrator.myvideonews.bombapi.result.UserResult;
import com.example.administrator.myvideonews.ui.ui.entity.UserEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public interface UserApi {

    @GET("1/login")
    Call<UserResult> login(@Query("username") String username, @Query("password") String password);

    @POST("1/users")
    Call<UserResult> register(@Body UserEntity userEntity);


}
