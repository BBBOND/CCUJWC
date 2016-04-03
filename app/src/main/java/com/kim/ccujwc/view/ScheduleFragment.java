package com.kim.ccujwc.view;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;

import com.kim.ccujwc.R;
import com.kim.ccujwc.common.App;
import com.kim.ccujwc.common.MyHttpUtil;
import com.kim.ccujwc.model.Course;
import com.kim.ccujwc.model.UICourse;
import com.kim.ccujwc.view.utils.LoadingView;
import com.kim.ccujwc.view.utils.ModelChange;
import com.kim.ccujwc.view.utils.ShapeLoadingView;

import org.apache.commons.httpclient.HttpClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class ScheduleFragment extends BaseFragment {

    private static final String TAG = "ScheduleFragment";

    private LoadingView loadView;
    private GridLayout glCourse;
    private TextView tvRow;
    private TextView tvCol;

    private int connCount = 0;

    Handler getCourseErrorHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                if (connCount <= 3) {
                    connCount++;
                    new GetCourse().execute();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("警告");
                    builder.setMessage("获取课表失败，是否重试？");
                    builder.setNegativeButton("取消", null);
                    builder.setPositiveButton("重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            connCount = 0;
                            new GetCourse().execute();
                        }
                    });
                    builder.create().show();
                }
            }
            return true;
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_schedule, null);

        initView(view);

        new GetCourse().execute();

        return view;
    }

    private void initView(View view) {
        loadView = (LoadingView) view.findViewById(R.id.loadView);
        glCourse = (GridLayout) view.findViewById(R.id.gl_course);
        tvCol = (TextView) view.findViewById(R.id.tv_col);
        tvRow = (TextView) view.findViewById(R.id.tv_row);
    }

    class GetCourse extends AsyncTask<Void, Void, List<Course>> {

        @Override
        protected List<Course> doInBackground(Void... params) {
            if (App.courses != null) {
                return App.courses;
            } else {
                Calendar now = Calendar.getInstance();
                int year = now.get(Calendar.YEAR);
                int month = now.get(Calendar.MONTH) + 1;
                String dateStr = "";
                if (month >= 2 && month <= 7) {
                    dateStr = (year - 1) + "-" + year + "-2";
                } else {
                    dateStr = year + "-" + (year + 1) + "-1";
                }
                HttpClient client = new HttpClient();
                try {
                    return MyHttpUtil.getCourses(client, dateStr);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = getCourseErrorHandler.obtainMessage();
                    message.what = 1;
                    message.sendToTarget();
                    return null;
                }
            }
        }

        @Override
        protected void onPreExecute() {
            loadView.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Course> result) {
            if (result != null) {
                HashSet<Course> courses = new HashSet<>();
                courses.addAll(result);
                result.clear();
                result.addAll(courses);
                courses.clear();
                App.courses = result;
                List<UICourse> uiCourses = ModelChange.course2UICourse(result);
                try {
                    for (int j = 0; j < uiCourses.size(); j++) {
                        final UICourse course = uiCourses.get(j);
                        int courseTime = 0;
                        if (!course.getCourseStartTime().equals("")) {
                            courseTime = Integer.parseInt(course.getCourseStartTime());
                            String timeStr = Integer.toString(courseTime);

                            int x = Integer.parseInt(timeStr.charAt(0) + "") - 1;
                            int y = Integer.parseInt(timeStr.charAt(2) + "") / 2;

                            TextView tv = getView(course, x, y);
                            glCourse.addView(tv);
                            if (timeStr.length() > 5 && timeStr.length() <= 9) {
                                y = Integer.parseInt(timeStr.charAt(6) + "") / 2;
                                tv = getView(course, x, y);
                                glCourse.addView(tv);
                            } else if (timeStr.length() > 9 && timeStr.length() <= 13) {
                                y = Integer.parseInt(timeStr.charAt(10) + "") / 2;
                                tv = getView(course, x, y);
                                glCourse.addView(tv);
                            } else if (timeStr.length() > 13 && timeStr.length() <= 17) {
                                y = Integer.parseInt(timeStr.charAt(14) + "") / 2;
                                tv = getView(course, x, y);
                                glCourse.addView(tv);
                            } else if (timeStr.length() > 17 && timeStr.length() <= 21) {
                                y = Integer.parseInt(timeStr.charAt(18) + "") / 2;
                                tv = getView(course, x, y);
                                glCourse.addView(tv);
                            }
                        }
                    }
                    loadView.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(result);
        }
    }

    @NonNull
    private TextView getView(final UICourse course, int x, int y) throws Exception {
        GridLayout.Spec row = GridLayout.spec(y);
        GridLayout.Spec col = GridLayout.spec(x);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(row, col);
        params.width = tvCol.getWidth();
        params.height = tvRow.getHeight();
        TextView tv = new TextView(getContext());
        tv.setLayoutParams(params);
        tv.setBackgroundResource(R.drawable.course_card_background);
        tv.setText(course.getShowText());
        tv.setPadding(5, 5, 2, 2);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Course> courses = course.getCourses();
                String message = "";
                for (int i = 0; i < courses.size(); i++) {
                    if (i > 0) {
                        message += "\n--------\n";
                    }
                    message += courses.get(i).toString();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("详细信息");
                builder.setMessage(message);
                builder.setPositiveButton("确定", null);
                builder.create().show();
            }
        });
        return tv;
    }
}
