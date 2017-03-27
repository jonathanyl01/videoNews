package com.example.administrator.myvideonews.bombapi.result;

import com.example.administrator.myvideonews.ui.ui.entity.NewsEntity;

/**
 * Created by Administrator on 2017/3/22 0022.
 *//**
 * Created by Administrator on 2016/12/27 0027.
 */

//{
//        "success": true,
//        "error": null,
//        "data": {
//        "updatedAt": "2016-10-10 09:43:55" // 更新时间
//        }
//        }

public class CollectResult {

    private boolean success;
    private String error;
    private NewsEntity data;

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public NewsEntity getData() {
        return data;
    }

}
