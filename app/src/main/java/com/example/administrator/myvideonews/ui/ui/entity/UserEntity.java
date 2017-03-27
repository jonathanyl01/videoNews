package com.example.administrator.myvideonews.ui.ui.entity;

/**
 * Created by Administrator on 2017/3/21 0021.
 */
//用户的实体类
public class UserEntity {

    private String username;
    private String password;


    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
