package com.example.administrator.myvideonews.bombapi;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

//网络连接用到的常量值
public class BombConst {


    //应用id用于让Bomb后台区分是哪一个应用
    String APPLICATION_ID = "623aaef127882aed89b9faa348451da3";

    // REST API的授权码
    String REST_API_KEY = "c00104962a9b67916e8cbcb9157255de";

    // 服务器的评论表表名
    String TABLE_COMMENTS = "Comments";
    // 服务器的评论表新闻字段(评论所对应的新闻)
    String FIELD_NEWS = "news";

    // 服务器的新闻表表名
    String TABLE_NEWS = "News";

    public String getFIELD_LIKES() {
        return FIELD_LIKES;
    }

    public String getAPPLICATION_ID() {
        return APPLICATION_ID;
    }

    public String getREST_API_KEY() {
        return REST_API_KEY;
    }

    public String getTABLE_COMMENTS() {
        return TABLE_COMMENTS;
    }

    public String getFIELD_NEWS() {
        return FIELD_NEWS;
    }

    public String getTABLE_NEWS() {
        return TABLE_NEWS;
    }

    public String getTABLE_USER() {
        return TABLE_USER;
    }

    public String getTABLE_LIKES() {
        return TABLE_LIKES;
    }

    // 服务器的新闻表收藏字段(此新闻被谁收藏)
    String FIELD_LIKES = "likes";

    // 服务器的用户表表名
    String TABLE_USER = "_User";

    // 服务器的收藏表表名
    String TABLE_LIKES = "likes";
}
