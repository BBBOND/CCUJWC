package com.kim.ccujwc.view.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于存储登陆时的信息
 * Created by 伟阳 on 2016/3/15.
 */
public class MySharedPreferences {

    private Context context;
    private static MySharedPreferences mySharedPreferences = null;

    public static MySharedPreferences getInstance(Context context) {
        if (mySharedPreferences == null) {
            mySharedPreferences = new MySharedPreferences(context);
        }
        return mySharedPreferences;
    }

    public MySharedPreferences(Context context) {
        this.context = context;
    }

    public void saveLoginInfo(String account, String password, boolean isSave) {
        SharedPreferences user = context.getSharedPreferences("login", Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
        SharedPreferences.Editor editor = user.edit();
        editor.putString("account", account);
        editor.putString("password", password);
        editor.putBoolean("isSave", isSave);
        editor.apply();
    }

    public Map<String, Object> readLoginInfo() {
        Map<String, Object> map = new HashMap<>();
        SharedPreferences user = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        map.put("account", user.getString("account", ""));
        map.put("password", user.getString("password", ""));
        map.put("isSave", user.getBoolean("isSave", false));
        return map;
    }

}
