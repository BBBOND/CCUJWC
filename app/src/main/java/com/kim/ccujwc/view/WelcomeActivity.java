package com.kim.ccujwc.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;

import com.kim.ccujwc.R;
import com.kim.ccujwc.common.App;
import com.kim.ccujwc.common.Common;
import com.kim.ccujwc.view.utils.MySharedPreferences;

import java.util.Map;

/**
 * 欢迎界面
 * Created by 伟阳 on 2016/3/12.
 */
public class WelcomeActivity extends BaseActivity {

    Handler checkConnHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if ((boolean) msg.obj) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                        finish();
                    }
                }, 2000);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
                builder.setTitle("警告");
                builder.setMessage("检测到无网络连接,请检查网络后重试！");
                builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                });
                builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
            }
            return (boolean) msg.obj;
        }
    });

    Handler autoLoginHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                MySharedPreferences msp = MySharedPreferences.getInstance(WelcomeActivity.this);
                try {
                    Map<String, Object> map = msp.readLoginInfo();
                    String account = (String) map.get("account");
                    if (!account.equals(App.Account)) {
                        msp.clearAll();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sendBroadcast(new Intent(Common.RECEIVER));
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
                return true;
            } else if (msg.what == 0) {
                checkNetWorkConn();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setScreen();

        new Handler().postDelayed(new CheckLogin(), 2000);
    }

    class CheckLogin implements Runnable {
        @Override
        public void run() {
            MySharedPreferences sharedPreferences = MySharedPreferences.getInstance(WelcomeActivity.this);
            Map<String, Object> map = sharedPreferences.readLoginInfo();
            Message message = autoLoginHandler.obtainMessage();
            boolean isAutoLogin = false;
            try {
                isAutoLogin = (boolean) map.get("isAutoLogin");
            } catch (Exception e) {
                isAutoLogin = false;
            }
            if (isAutoLogin) {
                message.what = 1;
                App.Account = (String) map.get("account");
                App.PWD = (String) map.get("password");
            } else {
                message.what = 0;
            }
            message.sendToTarget();
        }
    }

    private void checkNetWorkConn() {
        new Thread(new Runnable() {
            public void run() {
                Message message = checkConnHandler.obtainMessage();
                message.obj = isNetWorkConn();
                message.sendToTarget();
            }
        }).start();
    }
}
