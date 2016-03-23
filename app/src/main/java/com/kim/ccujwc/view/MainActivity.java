package com.kim.ccujwc.view;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.kim.ccujwc.R;
import com.kim.ccujwc.common.App;
import com.kim.ccujwc.common.MyHttpUtil;

import org.apache.commons.httpclient.HttpClient;

import java.io.File;
import java.io.IOException;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;

    private FragmentManager fm = getSupportFragmentManager();

    private MainFragment mainFragment;
    private SchoolCardFragment schoolCardFragment;
    private ScheduleFragment scheduleFragment;
    private ScoreFragment scoreFragment;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                ((TextView) findViewById(R.id.tv_name)).setText(App.Name);
                ((TextView) findViewById(R.id.tv_studentCode)).setText(App.Account);
                return true;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        toolbar.setTitle("首页");

        getStudentIfno();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initFragment();
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
                    HttpClient client = new HttpClient();
                    return MyHttpUtil.getStudentImage(client, MainActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            ImageView iv = (ImageView) findViewById(R.id.imageView);
            assert iv != null;
            if (result) {
                iv.setImageDrawable(Drawable.createFromPath(getFilesDir().getPath() + "/" + App.Account + ".jpg"));
            } else {
                iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_main));
            }
            super.onPostExecute(result);
        }
    }

    private void getStudentIfno() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient client = new HttpClient();
                try {
                    MyHttpUtil.getName(client);
                } catch (Exception e) {
                    App.Name = "获取失败";
                }
                Message message = handler.obtainMessage();
                message.what = 1;
                message.sendToTarget();
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
        if (isNetWorkConn()) {
            FragmentTransaction transaction = null;
            if (id == R.id.nav_main) {
                toolbar.setTitle("首页");
                transaction = fm.beginTransaction();
                if (mainFragment == null)
                    mainFragment = new MainFragment();
                transaction.replace(R.id.frag_main, mainFragment);
                transaction.commit();
            } else if (id == R.id.nav_schoolCard) {
                toolbar.setTitle("学籍卡片");
                transaction = fm.beginTransaction();
                if (schoolCardFragment == null)
                    schoolCardFragment = new SchoolCardFragment();
                transaction.replace(R.id.frag_main, schoolCardFragment);
                transaction.commit();
            } else if (id == R.id.nav_schedule) {
                toolbar.setTitle("个人课表");
                transaction = fm.beginTransaction();
                if (scheduleFragment == null)
                    scheduleFragment = new ScheduleFragment();
                transaction.replace(R.id.frag_main, scheduleFragment);
                transaction.commit();
            } else if (id == R.id.nav_scoreQuery) {
                toolbar.setTitle("成绩查询");
                transaction = fm.beginTransaction();
                if (scoreFragment == null)
                    scoreFragment = new ScoreFragment();
                transaction.replace(R.id.frag_main, scoreFragment);
                transaction.commit();
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
            builder.setMessage("有任何问题请反馈至jwy8645@163.com");
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
            builder.setPositiveButton("好的", null);
            builder.create().show();
        } else if (id == R.id.nav_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("关于");
            builder.setMessage("感谢您使用本软件，你们的支持是我最大的动力，如果你有余力也可以请我吃个盒饭。\n支付宝账号:609076290@qq.com");
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
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
