package com.example.administrator.myvideonews.ui.ui.likes;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.myvideonews.R;
import com.example.administrator.myvideonews.bombapi.BombClient;
import com.example.administrator.myvideonews.bombapi.UserApi;
import com.example.administrator.myvideonews.bombapi.result.ErrorResult;
import com.example.administrator.myvideonews.bombapi.result.UserResult;
import com.example.administrator.myvideonews.commons.ToastUtils;
import com.example.administrator.myvideonews.ui.ui.entity.UserEntity;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/3/16 0016.
 */

public class RegisterFragment extends DialogFragment {

    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_register, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btnRegister)
    public void onClick(View view) {
        final String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            ToastUtils.showShort(R.string.username_or_password_can_not_be_null);
            return;


        }

        UserApi userApi = BombClient.getInstance().getUserApi();
        UserEntity userEntity = new UserEntity(username,password);

        Call<UserResult> call = userApi.register(userEntity);

        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                btnRegister.setVisibility(View.VISIBLE);

                if (!response.isSuccessful()){

                    try {
                        String error = response.errorBody().string();
                        ErrorResult errorResult = new Gson().fromJson(error,ErrorResult.class);

                        ToastUtils.showShort(errorResult.getError());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;



                }

                UserResult userResult = response.body();
                listener.regesisterSuccess(username,userResult.getObjectId());

                ToastUtils.showShort(R.string.register_success);



            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {

                btnRegister.setVisibility(View.VISIBLE);

                ToastUtils.showShort(t.getMessage());
            }
        });

       /* Call call = BombClient.getInstance().register(username, password);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });*/


    }

    public interface OnRegisterSussesListener{

        void regesisterSuccess(String username,String objectId);
    }

    private OnRegisterSussesListener listener;

    public void setListener(OnRegisterSussesListener listener) {
        this.listener = listener;
    }
}
