package com.kim.ccujwc.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kim.ccujwc.common.App;
import com.kim.ccujwc.common.MyHttpUtil;
import com.kim.ccujwc.model.Course;
import com.kim.ccujwc.model.News;
import com.kim.ccujwc.model.PersonGrade;
import com.kim.ccujwc.view.utils.MySharedPreferences;

import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SyncService extends Service {

    private static final String TAG = "SyncService";

    int dayConnCount = 0;
    int monthConnCount = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service onStartCommand start!");
        flags = START_STICKY;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date when = calendar.getTime();
        Timer timerDay = new Timer();
        timerDay.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "Service syncOneDay start!");
                syncOneDay();
            }
        }, when);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        when = calendar.getTime();
        Timer timerMonth = new Timer();
        timerMonth.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "Service syncOneMonth start!");
                syncOneMonth();
            }
        }, when);

        return super.onStartCommand(intent, flags, startId);
    }

    public void syncOneDay() {
        try {
            MySharedPreferences msp = MySharedPreferences.getInstance(getApplicationContext());
            Map<String, Object> map = msp.readLoginInfo();
            App.Account = (String) map.get("account");
            App.PWD = (String) map.get("password");
            PersonGrade personGrade = MyHttpUtil.getGrade();
            msp.savePersonGrade(personGrade);
            List<News> newsList = MyHttpUtil.getNewsList();
            msp.saveNews(newsList);
        } catch (Exception e) {
            e.printStackTrace();
            if (dayConnCount <= 3) {
                syncOneDay();
                dayConnCount++;
            } else {
                dayConnCount = 0;
            }
        }
    }

    public void syncOneMonth() {
        try {
            MySharedPreferences msp = MySharedPreferences.getInstance(getApplicationContext());
            Map<String, Object> map = msp.readLoginInfo();
            App.Account = (String) map.get("account");
            App.PWD = (String) map.get("password");
            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            int month = now.get(Calendar.MONTH) + 1;
            String dateStr = "";
            if (month >= 2 && month <= 7) {
                dateStr = (year - 1) + "-" + year + "-2";
            } else {
                dateStr = year + "-" + (year + 1) + "-1";
            }
            List<Course> courseList = MyHttpUtil.getCourses(dateStr);
            msp.saveCourseList(courseList);
        } catch (Exception e) {
            e.printStackTrace();
            if (monthConnCount <= 3) {
                syncOneMonth();
                monthConnCount++;
            } else {
                monthConnCount = 0;
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Service onDestroy");
        startService(new Intent(getApplicationContext(), SyncService.class));
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
