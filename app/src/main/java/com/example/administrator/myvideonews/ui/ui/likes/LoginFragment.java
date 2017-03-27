package com.example.administrator.myvideonews.ui.ui.likes;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.administrator.myvideonews.R;
import com.example.administrator.myvideonews.bombapi.BombClient;
import com.example.administrator.myvideonews.bombapi.UserApi;
import com.example.administrator.myvideonews.bombapi.result.ErrorResult;
import com.example.administrator.myvideonews.bombapi.result.UserResult;
import com.example.administrator.myvideonews.commons.ToastUtils;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class LoginFragment extends DialogFragment {

    @BindView(R.id.etUsername)
    TextInputEditText etUsername;
    @BindView(R.id.etPassword)
    TextInputEditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_login, container, false);


        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btnLogin)
    public void onClick() {

        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        //用户名和密码不能为空
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            ToastUtils.showShort(R.string.username_or_password_can_not_be_null);
            return;
        }


        //显示进度条
        btnLogin.setVisibility(View.GONE);

        UserApi userApi = BombClient.getInstance().getUserApi();
        Call<UserResult> call = userApi.login(username, password);

        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {

                btnLogin.setVisibility(View.VISIBLE);

                //登录未成功
                if (!response.isSuccessful()) {

                    try {
                        String error = response.errorBody().string();
                        ErrorResult errorResult = new Gson().fromJson(error, ErrorResult.class);
                        ToastUtils.showShort(errorResult.getError());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;

                }
                //登陆成功
                UserResult result = response.body();
                listener.loginSuccess(username,result.getObjectId());
                ToastUtils.showShort(R.string.login_success);

            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {

                btnLogin.setVisibility(View.VISIBLE);
                ToastUtils.showShort(t.getMessage());
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    //当登录成功之后触发的方法
    public interface OnLoginSuccessListener {
        /**
         * 当登录成功时，将来调用
         */
        void loginSuccess(String username, String objectId);
    }

    private OnLoginSuccessListener listener;

    public void setListener(@Nullable OnLoginSuccessListener listener) {

        this.listener = listener;


    }

}
