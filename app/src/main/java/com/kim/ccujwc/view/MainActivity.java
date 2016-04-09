package com.kim.ccujwc.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kim.ccujwc.R;
import com.kim.ccujwc.common.App;
import com.kim.ccujwc.common.Common;
import com.kim.ccujwc.common.MyHttpUtil;
import com.kim.ccujwc.service.SyncService;
import com.kim.ccujwc.view.utils.MySharedPreferences;

import java.io.File;
import java.io.IOException;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private ImageView iv;

    private FragmentManager fm = getSupportFragmentManager();

    private Fragment targetFragment;

    private MainFragment mainFragment;
    private SchoolCardFragment schoolCardFragment;
    private ScheduleFragment scheduleFragment;
    private ScoreFragment scoreFragment;

    private MySharedPreferences msp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("首页");
        msp = MySharedPreferences.getInstance(MainActivity.this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                ((TextView) findViewById(R.id.tv_name)).setText(App.Name);
                ((TextView) findViewById(R.id.tv_studentCode)).setText(App.Account);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initFragment();
        getStudentInfo();
        new GetStudentImage().execute();
    }

    class GetStudentImage extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            File file = new File(getFilesDir().getPath() + "/" + App.Account + ".jpg");
            if (file.exists()) {
                return true;
            } else {
                try {
                    return MyHttpUtil.getStudentImage(MainActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
//            iv = (ImageView) findViewById(R.id.imageView);
//            Log.d("--------", String.valueOf(iv == null));
//            try {
//                if (result) {
//                    iv.setImageDrawable(Drawable.createFromPath(getFilesDir().getPath() + "/" + App.Account + ".jpg"));
//
//                } else {
//                    iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_main));
//                }
//            } catch (Exception e) {
//                iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_main));
//            }
//            super.onPostExecute(result);
        }
    }

    private void getStudentInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String name = msp.readUserName();
                if (name != null && !name.equals("")) {
                    App.Name = name;
                } else {
                    try {
                        MyHttpUtil.getName();
                        if ((boolean) msp.readLoginInfo().get("isAutoLogin"))
                            msp.saveUserName(App.Name);
                    } catch (Exception e) {
                        App.Name = "获取失败";
                    }
                }
            }
        }).start();
    }

    private void initFragment() {
        if (mainFragment == null)
            mainFragment = new MainFragment();
        if (schoolCardFragment == null)
            schoolCardFragment = new SchoolCardFragment();
        if (scheduleFragment == null)
            scheduleFragment = new ScheduleFragment();
        if (scoreFragment == null)
            scoreFragment = new ScoreFragment();

        targetFragment = mainFragment;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean isAutoLogin = false;
        try {
            isAutoLogin = ((boolean) msp.readLoginInfo().get("isAutoLogin"));
        } catch (Exception e) {
            e.printStackTrace();
            isAutoLogin = false;
        }
        if (isAutoLogin || isNetWorkConn()) {
            FragmentTransaction transaction = null;
            if (id == R.id.nav_main) {
                toolbar.setTitle("首页");
                if (mainFragment == null) {
                    mainFragment = new MainFragment();
                }
                if (targetFragment != mainFragment) {
                    transaction = fm.beginTransaction();
                    transaction.replace(R.id.frag_main, mainFragment);
                    targetFragment = mainFragment;
                    transaction.commit();
                }
            } else if (id == R.id.nav_schoolCard) {
                toolbar.setTitle("学籍卡片");
                if (schoolCardFragment == null) {
                    schoolCardFragment = new SchoolCardFragment();
                }
                if (targetFragment != schoolCardFragment) {
                    transaction = fm.beginTransaction();
                    transaction.replace(R.id.frag_main, schoolCardFragment);
                    targetFragment = schoolCardFragment;
                    transaction.commit();
                }
            } else if (id == R.id.nav_schedule) {
                toolbar.setTitle("个人课表");
                if (scheduleFragment == null)
                    scheduleFragment = new ScheduleFragment();
                if (targetFragment != scheduleFragment) {
                    transaction = fm.beginTransaction();
                    transaction.replace(R.id.frag_main, scheduleFragment);
                    targetFragment = scheduleFragment;
                    transaction.commit();
                }
            } else if (id == R.id.nav_scoreQuery) {
                toolbar.setTitle("成绩查询");
                if (scoreFragment == null) {
                    scoreFragment = new ScoreFragment();
                }
                if (targetFragment != scoreFragment) {
                    transaction = fm.beginTransaction();
                    transaction.replace(R.id.frag_main, scoreFragment);
                    targetFragment = scoreFragment;
                    transaction.commit();
                }
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("警告");
            builder.setMessage("检测到无网络连接,请检查网络后重试！");
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                }
            });
            builder.create().show();
        }


        if (id == R.id.nav_reply) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("反馈");
            builder.setMessage("有任何问题请反馈至我的邮箱\n或反馈至GitHub");
            builder.setNegativeButton("复制账号", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        ClipboardManager c = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        c.setPrimaryClip(ClipData.newPlainText("支付宝账号", "jwy8645@163.com"));
                        Snackbar.make(toolbar, "复制成功!", Snackbar.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Snackbar.make(toolbar, "复制失败!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setPositiveButton("复制网址", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        ClipboardManager c = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        c.setPrimaryClip(ClipData.newPlainText("GitHub网址", "https://github.com/BBBOND/CCUJWC/issues"));
                        Snackbar.make(toolbar, "复制成功!", Snackbar.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Snackbar.make(toolbar, "复制失败!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
            builder.create().show();
        } else if (id == R.id.nav_donate) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("捐赠");
            builder.setMessage("感谢您使用本软件，你们的支持是我最大的动力，如果你有余力也可以请我吃个盒饭。");
            builder.setNegativeButton("复制账号", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        ClipboardManager c = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        c.setPrimaryClip(ClipData.newPlainText("支付宝账号", "609076290@qq.com"));
                        Snackbar.make(toolbar, "复制成功!", Snackbar.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Snackbar.make(toolbar, "复制失败!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setPositiveButton("知道了", null);
            builder.create().show();
        } else if (id == R.id.nav_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("关于");
            try {
                builder.setMessage("软件版本:" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
            } catch (PackageManager.NameNotFoundException e) {
                builder.setMessage("软件版本:获取失败");
            }
            builder.setPositiveButton("知道了", null);
            builder.create().show();
        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("提示");
            builder.setMessage("你即将注销帐号，是否继续？");
            builder.setNegativeButton("点错了", null);
            builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    App.clearAll();
                    MySharedPreferences msp = MySharedPreferences.getInstance(MainActivity.this);
                    msp.changeAutoLogin("isAutoLogin", false);
                    stopService(new Intent(MainActivity.this, SyncService.class));
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            });
            builder.create().show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        startService(new Intent(MainActivity.this, SyncService.class));
        super.onDestroy();
    }
}
