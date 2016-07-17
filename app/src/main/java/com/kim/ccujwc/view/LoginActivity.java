package com.kim.ccujwc.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.dd.processbutton.iml.ActionProcessButton;
import com.kim.ccujwc.R;
import com.kim.ccujwc.common.App;
import com.kim.ccujwc.common.Common;
import com.kim.ccujwc.common.MyHttpUtil;
import com.kim.ccujwc.view.utils.MySharedPreferences;

import java.util.Map;

/**
 * 登录界面
 * Created by 伟阳 on 2016/3/12.
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    private TextInputLayout edtAccount;
    private TextInputLayout edtPassword;
    private ActionProcessButton btnLogin;
    private CheckBox cbRemember;
    private CheckBox cbAutoLogin;

    private int connCount = 0;
    private int initParamsCount = 0;

    Handler connectFailedHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                new InitParams().execute();
                return true;
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("警告");
                builder.setMessage("无法连接教务处主页,请检查网络后重试！");
                builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setPositiveButton("重试", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initParamsCount = 0;
                        new InitParams().execute();
                    }
                });
                builder.create().show();
                return false;
            }
        }
    });

    Handler loginErrorHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                if (connCount <= 3) {
                    connCount++;
                    new Login().execute();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("警告");
                    builder.setMessage("服务器较为拥堵，是否重试?");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            edtAccount.getEditText().setEnabled(true);
                            edtPassword.getEditText().setEnabled(true);
                            btnLogin.setProgress(0);
                            btnLogin.setBackgroundColor(getResources().getColor(R.color.blue_normal));
                            btnLogin.setText("登录");
                            btnLogin.setEnabled(true);
                        }
                    });
                    builder.setPositiveButton("重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            connCount = 0;
                            new Login().execute();
                        }
                    });
                    builder.create().show();
                }
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        initEvent();
        new InitParams().execute();
    }

    private void initView() {
        edtAccount = (TextInputLayout) findViewById(R.id.edtAccount);
        edtPassword = (TextInputLayout) findViewById(R.id.edtPassword);
        btnLogin = (ActionProcessButton) findViewById(R.id.btnLogin);
        cbRemember = (CheckBox) findViewById(R.id.cb_remember);
        cbAutoLogin = (CheckBox) findViewById(R.id.cb_autoLogin);

        btnLogin.setEnabled(false);
        btnLogin.setText("准备中...");
        btnLogin.setBackgroundColor(getResources().getColor(R.color.blue_pressed));

        MySharedPreferences mspf = MySharedPreferences.getInstance(LoginActivity.this);
        Map<String, Object> user = mspf.readLoginInfo();
        boolean isSave = false;
        try {
            isSave = (boolean) user.get("isSave");
        } catch (Exception e) {
            isSave = false;
        }
        boolean isAutoLogin = false;
        try {
            isAutoLogin = (boolean) user.get("isAutoLogin");
        } catch (Exception e) {
            isAutoLogin = false;
        }
        if (isAutoLogin) {
            edtAccount.getEditText().setText((CharSequence) user.get("account"));
            edtPassword.getEditText().setText((CharSequence) user.get("password"));
            cbAutoLogin.setChecked(true);
            cbRemember.setChecked(true);
        } else if (isSave) {
            edtAccount.getEditText().setText((CharSequence) user.get("account"));
            edtPassword.getEditText().setText((CharSequence) user.get("password"));
            cbRemember.setChecked(true);
        }
    }

    private void initEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = edtAccount.getEditText().getText().toString().trim();
                String pwd = edtPassword.getEditText().getText().toString().trim();
                if (account.equals("")) {
                    edtAccount.setError("学号不能为空!");
                } else if (pwd.equals("")) {
                    edtPassword.setError("密码不能为空");
                } else {
                    App.Account = account;
                    App.PWD = pwd;
                    if (isNetWorkConn()) {
                        new Login().execute();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                        builder.create().show();
                    }
                }
            }
        });

        if (edtAccount.getEditText() != null) {
            edtAccount.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    btnLogin.setBackgroundColor(getResources().getColor(R.color.blue_normal));
                    btnLogin.setText("登录");
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    edtAccount.setError("");
                    edtPassword.setError("");
                }
            });
        }
        if (edtPassword.getEditText() != null) {
            edtPassword.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    btnLogin.setBackgroundColor(getResources().getColor(R.color.blue_normal));
                    btnLogin.setText("登录");
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    edtAccount.setError("");
                    edtPassword.setError("");
                }
            });
        }

        cbAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbRemember.setChecked(true);
                }
            }
        });
        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    cbAutoLogin.setChecked(false);
                }
            }
        });
    }

    class InitParams extends AsyncTask<String, Void, Map<String, String>> {

        @Override
        protected Map<String, String> doInBackground(String... params) {
            try {
                return MyHttpUtil.getParams();
            } catch (Exception e) {
                if (initParamsCount <= 3) {
                    initParamsCount++;
                    Message message = connectFailedHandler.obtainMessage();
                    message.what = 1;
                    message.sendToTarget();
                }
                return null;
            }
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            App.cookie = result.get("Cookie");
            App.__EVENTVALIDATION = result.get("__EVENTVALIDATION");
            App.__VIEWSTATE = result.get("__VIEWSTATE");
            btnLogin.setEnabled(true);
            btnLogin.setText("登录");
            btnLogin.setBackgroundColor(getResources().getColor(R.color.blue_normal));
            super.onPostExecute(result);
        }
    }

    class Login extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return MyHttpUtil.login();
            } catch (Exception e) {
                e.printStackTrace();
                Message message = loginErrorHandler.obtainMessage();
                message.what = 1;
                message.sendToTarget();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            btnLogin.setEnabled(false);
            btnLogin.setMode(ActionProcessButton.Mode.ENDLESS);
            btnLogin.setText("登录中...");
            btnLogin.setBackgroundColor(getResources().getColor(R.color.blue_pressed));
            btnLogin.setProgress(50);
            btnLogin.drawProgress(new Canvas());
            edtPassword.getEditText().setEnabled(false);
            edtAccount.getEditText().setEnabled(false);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result != null) {
                if (result) {
                    MySharedPreferences msp = MySharedPreferences.getInstance(LoginActivity.this);
                    try {
                        Map<String, Object> map = msp.readLoginInfo();
                        String account = (String) map.get("account");
                        if (!account.equals(App.Account)) {
                            msp.clearAll();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    msp.saveLoginInfo(App.Account, App.PWD, cbRemember.isChecked(), cbAutoLogin.isChecked());

                    sendBroadcast(new Intent(Common.RECEIVER));

                    btnLogin.setBackgroundColor(getResources().getColor(R.color.green_complete));
                    btnLogin.setText("登录成功");
                    btnLogin.setProgress(0);

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    btnLogin.setEnabled(true);
                    btnLogin.setProgress(0);
                    edtAccount.getEditText().setEnabled(true);
                    edtAccount.setError("学号或密码错误");
                    edtPassword.getEditText().setEnabled(true);
                    edtPassword.setError("学号或密码错误");
                    btnLogin.setBackgroundColor(getResources().getColor(R.color.red_error));
                    btnLogin.setText("登录失败");
                }
            }
            super.onPostExecute(result);
        }
    }
}
