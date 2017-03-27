package com.example.administrator.myvideonews.ui.ui.likes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.myvideonews.R;
import com.example.administrator.myvideonews.UserManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/15 0015.
 */
public class LinkesFragment extends Fragment implements LoginFragment.OnLoginSuccessListener,RegisterFragment.OnRegisterSussesListener {

    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnLogout)
    Button btnLogout;
    @BindView(R.id.likesListview)
    LikesListView likesListView;
    private View view;

    private RegisterFragment register;
    private LoginFragment loginFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_likes, container, false);

            ButterKnife.bind(this, view);
            // TODO: 2017/3/16 0016 判断用户登录信息，如果已经登录，则自动登录

            UserManager userManager = UserManager.getInstance();
            if (!userManager.isOffline()){
                userOnline(userManager.getUsername(),userManager.getObjectId());
            }
        }


        return view;
    }

    @OnClick({ R.id.btnRegister, R.id.btnLogin, R.id.btnLogout})
    public void onClick(View view) {
        switch (view.getId()) {
           //注册
            case R.id.btnRegister:
                if (register == null){
                    register = new RegisterFragment();
                    register.setListener(this);
                }
                register.show(getChildFragmentManager(),"Register Dialog");

                break;

            case R.id.btnLogin:
                if (loginFragment == null){
                    loginFragment = new LoginFragment();
                    loginFragment.setListener(this);
                }
                loginFragment.show(getChildFragmentManager(),"Login Dialog");

                break;
            case R.id.btnLogout:
                userOffline();
                break;
        }
    }

    //登陆成功
    @Override
    public void loginSuccess(String username, String objectId) {
        loginFragment.dismiss();

        //用户上线
        userOnline(username, objectId);

    }

    private void userOnline(String username,String objectId){

        //更新UI
        btnLogin.setVisibility(View.INVISIBLE);
        btnRegister.setVisibility(View.INVISIBLE);
        btnLogout.setVisibility(View.VISIBLE);
        divider.setVisibility(View.INVISIBLE);
        tvUsername.setText(username);

        UserManager.getInstance().setUsername(username);
        UserManager.getInstance().setObjectId(objectId);


        likesListView.autoRefresh();


    }

    //用户下线
    private void userOffline(){

        UserManager.getInstance().clear();

        btnLogin.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.INVISIBLE);
        divider.setVisibility(View.VISIBLE);
        tvUsername.setText(R.string.tourist);

        likesListView.clear();
    }


    @Override
    public void regesisterSuccess(String username, String objectId) {
       //关闭注册对话框
        register.dismiss();

        userOnline(username, objectId);
    }
}
