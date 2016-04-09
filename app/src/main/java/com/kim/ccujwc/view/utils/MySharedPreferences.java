package com.kim.ccujwc.view.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.kim.ccujwc.model.Course;
import com.kim.ccujwc.model.Grade;
import com.kim.ccujwc.model.News;
import com.kim.ccujwc.model.PersonGrade;
import com.kim.ccujwc.model.SchoolCard;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 用于存储登陆时的信息
 * Created by 伟阳 on 2016/3/15.
 */
public class MySharedPreferences {

    private Context context;
    private static MySharedPreferences mySharedPreferences = null;

    private MySharedPreferences() {
    }

    private MySharedPreferences(Context context) {
        this.context = context;
    }

    public static MySharedPreferences getInstance(Context context) {
        if (mySharedPreferences == null) {
            mySharedPreferences = new MySharedPreferences(context);
        }
        return mySharedPreferences;
    }

    /**
     * 保存登录信息
     *
     * @param account
     * @param password
     * @param isSave
     * @param isAutoLogin
     */
    public void saveLoginInfo(String account, String password, boolean isSave, boolean isAutoLogin) {
        SharedPreferences login = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = login.edit();
        editor.putString("account", account);
        editor.putString("password", password);
        editor.putBoolean("isSave", isSave);
        editor.putBoolean("isAutoLogin", isAutoLogin);
        editor.apply();
    }

    public void changeAutoLogin(String key, boolean value) {
        SharedPreferences login = context.getSharedPreferences("login", Context.MODE_APPEND);
        SharedPreferences.Editor editor = login.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 获得登录信息
     *
     * @return
     */
    public Map<String, Object> readLoginInfo() {
        Map<String, Object> map = new HashMap<>();
        SharedPreferences login = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        Map<String, ?> temp = login.getAll();
        Set<String> set = temp.keySet();
        for (String key : set) {
            map.put(key, temp.get(key));
        }
        return map;
    }

    /**
     * 保存用户姓名
     *
     * @param name
     */
    public void saveUserName(String name) {
        SharedPreferences userInfo = context.getSharedPreferences("schoolcard", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString("name", name);
        editor.apply();
    }

    /**
     * 读取用户姓名
     *
     * @return
     */
    public String readUserName() {
        SharedPreferences userInfo = context.getSharedPreferences("schoolcard", Context.MODE_PRIVATE);
        return userInfo.getString("name", "");
    }

    /**
     * 保存学籍卡片
     *
     * @param schoolCard
     */
    public void saveSchoolCard(SchoolCard schoolCard) {
        SharedPreferences schoolcard = context.getSharedPreferences("schoolcard", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = schoolcard.edit();
        editor.putString("schoolcard", JSON.toJSONString(schoolCard));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
        editor.putString("savetime", sdf.format(new Date()));
        editor.apply();
    }

    /**
     * 读取学籍卡片
     *
     * @return
     */
    public Map<String, Object> readSchoolCard() {
        Map<String, Object> map = new HashMap<>();
        SchoolCard schoolCard = null;
        SharedPreferences schoolcard = context.getSharedPreferences("schoolcard", Context.MODE_PRIVATE);
        schoolCard = JSON.parseObject(schoolcard.getString("schoolcard", ""), SchoolCard.class);
        map.put("schoolcard", schoolCard);
        map.put("savetime", schoolcard.getString("savetime", ""));
        return map;
    }

    /**
     * 保存首页新闻
     *
     * @param newsList
     */
    public void saveNews(List<News> newsList) {
        SharedPreferences newslist = context.getSharedPreferences("newslist", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = newslist.edit();
        editor.putString("newslist", JSON.toJSONString(newsList));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
        editor.putString("savetime", sdf.format(new Date()));
        editor.apply();
    }

    /**
     * 读取首页新闻
     *
     * @return
     */
    public Map<String, Object> readNewsList() {
        Map<String, Object> map = new HashMap<>();
        List<News> newsList = null;
        SharedPreferences newslist = context.getSharedPreferences("newslist", Context.MODE_PRIVATE);
        newsList = JSON.parseArray(newslist.getString("newslist", ""), News.class);
        map.put("newslist", newsList);
        map.put("savetime", newslist.getString("savetime", ""));
        return map;
    }

    /**
     * 保持课表
     *
     * @param courseList
     */
    public void saveCourseList(List<Course> courseList) {
        SharedPreferences courselist = context.getSharedPreferences("courselist", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = courselist.edit();
        editor.putString("courselist", JSON.toJSONString(courseList));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
        editor.putString("savetime", sdf.format(new Date()));
        editor.apply();
    }

    /**
     * 读取课表
     *
     * @return
     */
    public Map<String, Object> readCourseList() {
        Map<String, Object> map = new HashMap<>();
        List<Course> courseList = null;
        SharedPreferences courselist = context.getSharedPreferences("courselist", Context.MODE_PRIVATE);
        courseList = JSON.parseArray(courselist.getString("courselist", ""), Course.class);
        map.put("courselist", courseList);
        map.put("savetime", courselist.getString("savetime", ""));
        return map;
    }

    /**
     * 保持成绩
     *
     * @param personGrade
     */
    public void savePersonGrade(PersonGrade personGrade) {
        SharedPreferences persongrade = context.getSharedPreferences("persongrade", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = persongrade.edit();
        editor.putString("persongrade", JSON.toJSONString(personGrade));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
        editor.putString("savetime", sdf.format(new Date()));
        editor.apply();
    }

    /**
     * 读取成绩
     *
     * @return
     */
    public Map<String, Object> readPersonGrade() {
        Map<String, Object> map = new HashMap<>();
        SharedPreferences gradelist = context.getSharedPreferences("persongrade", Context.MODE_PRIVATE);
        PersonGrade personGrade = JSON.parseObject(gradelist.getString("persongrade", ""), PersonGrade.class);
        map.put("persongrade", personGrade);
        map.put("savetime", gradelist.getString("savetime", ""));
        return map;
    }

    /**
     * 清除数据
     */
    public void clearAll() {
        SharedPreferences login = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences userInfo = context.getSharedPreferences("schoolcard", Context.MODE_PRIVATE);
        SharedPreferences schoolcard = context.getSharedPreferences("schoolcard", Context.MODE_PRIVATE);
        SharedPreferences courselist = context.getSharedPreferences("courselist", Context.MODE_PRIVATE);
        SharedPreferences gradelist = context.getSharedPreferences("persongrade", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = login.edit();
        editor.clear().apply();
        editor = userInfo.edit();
        editor.clear().apply();
        editor = schoolcard.edit();
        editor.clear().apply();
        editor = courselist.edit();
        editor.clear().apply();
        editor = gradelist.edit();
        editor.clear().apply();
    }
}
