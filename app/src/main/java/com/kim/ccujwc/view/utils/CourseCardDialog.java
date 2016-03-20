package com.kim.ccujwc.view.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.kim.ccujwc.R;
import com.kim.ccujwc.model.Course;

import java.util.List;

/**
 * Created by 伟阳 on 2016/3/15.
 */
public class CourseCardDialog {

    private Dialog dialog;
    private LayoutInflater inflater;
    private List<Course> courses;

    public CourseCardDialog(Context context, List<Course> courses) {
        this.courses = courses;
        dialog = new Dialog(context);
        inflater = LayoutInflater.from(context);
    }

    public void show() {
        View view = inflater.inflate(R.layout.course_card, null);
        dialog.setContentView(view);
    }
}
