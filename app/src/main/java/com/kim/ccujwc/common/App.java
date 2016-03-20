package com.kim.ccujwc.common;

import android.app.Application;

import com.kim.ccujwc.model.Course;
import com.kim.ccujwc.model.PersonGrade;

import java.util.List;

/**
 * Created by 伟阳 on 2016/3/12.
 */
public class App extends Application {
    public static String __VIEWSTATE = "";
    public static String __EVENTVALIDATION = "";
    public static String Name = "";
    public static String Account = "";

    public static String PWD = "";
    public static String cookie = "";

    public static String cookie2 = "";

    public static PersonGrade personGrade = null;
    public static List<Course> courses = null;
}
