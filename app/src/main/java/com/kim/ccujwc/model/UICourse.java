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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UICourse uiCourse = (UICourse) o;

        if (showText != null ? !showText.equals(uiCourse.showText) : uiCourse.showText != null)
            return false;
        if (courseStartTime != null ? !courseStartTime.equals(uiCourse.courseStartTime) : uiCourse.courseStartTime != null)
            return false;
        return courses != null ? courses.equals(uiCourse.courses) : uiCourse.courses == null;

    }

    @Override
    public int hashCode() {
        int result = showText != null ? showText.hashCode() : 0;
        result = 31 * result + (courseStartTime != null ? courseStartTime.hashCode() : 0);
        result = 31 * result + (courses != null ? courses.hashCode() : 0);
        return result;
    }
}
