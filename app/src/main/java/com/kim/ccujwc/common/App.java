package com.kim.ccujwc.common;

import android.app.Application;

import com.kim.ccujwc.model.Course;
import com.kim.ccujwc.model.Grade;
import com.kim.ccujwc.model.News;
import com.kim.ccujwc.model.PersonGrade;
import com.kim.ccujwc.model.SchoolCard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class App extends Application {
    public static String __VIEWSTATE = "";
    public static String __EVENTVALIDATION = "";
    public static String Name = "";
    public static String Account = "";
    public static String PWD = "";
    public static String cookie = "";
    public static String cookie2 = "";

    private static Map<String, Object> localNews = null;
    private static Map<String, Object> localSchoolCard = null;
    private static Map<String, Object> localSchedule = null;
    private static Map<String, Object> localPersonGrade = null;

    public static void clearAll() {
        __EVENTVALIDATION = "";
        __VIEWSTATE = "";
        Name = "";
        Account = "";
        PWD = "";
        cookie = "";
        cookie2 = "";
        localNews = null;
        localSchoolCard = null;
        localSchedule = null;
        localPersonGrade = null;
    }

    public static Map<String, Object> getLocalNews() {
        return localNews;
    }

    public static void setLocalNews(List<News> newsList) {
        localNews = new HashMap<>();
        localNews.put("newslist", newsList);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
        localNews.put("savetime", sdf.format(new Date()));
    }

    public static void clearLocalNews(){
        localNews = null;
    }

    public static Map<String, Object> getLocalSchoolCard() {
        return localSchoolCard;
    }

    public static void setLocalSchoolCard(SchoolCard schoolCard) {
        localSchoolCard = new HashMap<>();
        localSchoolCard.put("schoolcard", schoolCard);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
        localSchoolCard.put("savetime", sdf.format(new Date()));
    }

    public static void clearLocalSchoolCard(){
        localSchoolCard = null;
    }

    public static Map<String, Object> getLocalSchedule() {
        return localSchedule;
    }

    public static void setLocalSchedule(List<Course> courseList) {
        localSchedule = new HashMap<>();
        localSchedule.put("courselist", courseList);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
        localSchedule.put("savetime", sdf.format(new Date()));
    }

    public static void clearLocalSchedule(){
        localSchedule = null;
    }

    public static Map<String, Object> getLocalPersonGrade() {
        return localPersonGrade;
    }

    public static void setLocalPersonGrade(PersonGrade personGrade) {
        localPersonGrade = new HashMap<>();
        localPersonGrade.put("persongrade", personGrade);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
        localPersonGrade.put("savetime", sdf.format(new Date()));
    }

    public static void clearLocalPersonGrade(){
        localPersonGrade = null;
    }
}
