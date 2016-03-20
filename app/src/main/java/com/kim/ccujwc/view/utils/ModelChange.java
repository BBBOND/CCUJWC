package com.kim.ccujwc.view.utils;

import com.kim.ccujwc.model.Course;
import com.kim.ccujwc.model.UICourse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 伟阳 on 2016/3/15.
 */
public class ModelChange {
    public static List<UICourse> course2UICourse(List<Course> courses) {
        List<UICourse> uiCourses = new ArrayList<>();
        List<String> coursesTime = new ArrayList<>();
        for (Course course : courses) {
            if (!coursesTime.contains(course.getCourseStartTime())) {
                coursesTime.add(course.getCourseStartTime());
            }
        }
        UICourse uiCourse = null;
        for (String time : coursesTime) {
            uiCourse = new UICourse();
            uiCourse.setCourseStartTime(time);
            uiCourses.add(uiCourse);
        }
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            int index = coursesTime.indexOf(course.getCourseStartTime());
            uiCourse = uiCourses.get(index);
            List<Course> cs = uiCourse.getCourses();
            if (cs == null) {
                cs = new ArrayList<>();
            }
            cs.add(course);
            uiCourse.setCourses(cs);
            if (uiCourse.getShowText() == null || uiCourse.getShowText().equals(""))
                uiCourse.setShowText(course.getCourseName() + "@" + course.getCoursePlace());
            else
                uiCourse.setShowText(uiCourse.getShowText() + "\n----\n" + course.getCourseName() + "@" + course.getCoursePlace());
        }
        return uiCourses;
    }
}
