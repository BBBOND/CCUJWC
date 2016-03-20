package com.kim.ccujwc.model;

import java.util.List;

/**
 * Created by 伟阳 on 2016/3/15.
 */
public class UICourse {
    private String showText;
    private String courseStartTime;
    private List<Course> courses;

    public String getShowText() {
        return showText;
    }

    public void setShowText(String showText) {
        this.showText = showText;
    }

    public String getCourseStartTime() {
        return courseStartTime;
    }

    public void setCourseStartTime(String courseStartTime) {
        this.courseStartTime = courseStartTime;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
