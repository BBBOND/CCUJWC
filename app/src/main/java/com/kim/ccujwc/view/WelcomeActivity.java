package com.kim.ccujwc.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.kim.ccujwc.R;
import com.kim.ccujwc.common.App;
import com.kim.ccujwc.common.MyHttpUtil;

import org.apache.commons.httpclient.HttpClient;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setScreen();
    }

    @Override
    protected void onResume() {
        checkNetWorkConn();
        super.onResume();
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
