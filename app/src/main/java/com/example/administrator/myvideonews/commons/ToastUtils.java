package com.example.administrator.myvideonews.commons;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/3/16 0016.
 */

public class ToastUtils {
    private static Toast toast;
    private static Context context;

    public static void init(Context context){
        ToastUtils.context = context;

    }
    public static void showShort(String msg){
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }

        toast.show();
    }

    public static void showShort(int resId){
        showShort(context.getResources().getString(resId));
    }


}
