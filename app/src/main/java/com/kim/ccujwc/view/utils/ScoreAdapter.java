package com.kim.ccujwc.view.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kim.ccujwc.R;
import com.kim.ccujwc.model.Grade;

import java.util.List;

/**
 * Created by 伟阳 on 2016/3/13.
 */
public class ScoreAdapter extends BaseAdapter {

    private List<Grade> gradeList = null;
    private LayoutInflater inflater;
    private int viewId;

    public ScoreAdapter(Context context, int viewId, List<Grade> gradeList) {
        this.gradeList = gradeList;
        inflater = LayoutInflater.from(context);
        this.viewId = viewId;
    }

    @Override
    public int getCount() {
        return gradeList.size();
    }

    @Override
    public Object getItem(int position) {
        return gradeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Grade grade = gradeList.get(position);
        View view = null;
        Holder holder;
        if (convertView != null) {
            view = convertView;
            holder = (Holder) convertView.getTag();
        } else {
            view = inflater.inflate(viewId, null);
            holder = new Holder();
            holder.tvIsPass = (TextView) view.findViewById(R.id.tv_isPass);
            holder.tvSemester = (TextView) view.findViewById(R.id.tv_semester);
            holder.tvCourseNumber = (TextView) view.findViewById(R.id.tv_courseNumber);
            holder.tvCourseName = (TextView) view.findViewById(R.id.tv_courseName);
            holder.tvScore = (TextView) view.findViewById(R.id.tv_score);
            holder.tvCredit = (TextView) view.findViewById(R.id.tv_credit);
            holder.tvPeriod = (TextView) view.findViewById(R.id.tv_period);
            holder.tvCourseNature = (TextView) view.findViewById(R.id.tv_courseNature);
            holder.tvCourseType = (TextView) view.findViewById(R.id.tv_courseType);
            holder.tvExamNature = (TextView) view.findViewById(R.id.tv_examNature);
            holder.tvExamMethod = (TextView) view.findViewById(R.id.tv_examMethod);
            holder.tvMark = (TextView) view.findViewById(R.id.tv_mark);
            holder.tvReSemester = (TextView) view.findViewById(R.id.tv_reSemester);
            view.setTag(holder);
        }
        holder.tvIsPass.setText(grade.getIsPass());
        holder.tvSemester.setText(grade.getSemester());
        holder.tvCourseNumber.setText(grade.getCourseNumber());
        holder.tvCourseName.setText(grade.getCourseName());
        holder.tvScore.setText(grade.getSocre());
        holder.tvCredit.setText(grade.getCredit());
        holder.tvPeriod.setText(grade.getPeriod());
        holder.tvCourseNature.setText(grade.getCourseNature());
        holder.tvCourseType.setText(grade.getCourseType());
        holder.tvExamNature.setText(grade.getExamMethod());
        holder.tvExamMethod.setText(grade.getExamMethod());
        holder.tvMark.setText(grade.getMark());
        holder.tvReSemester.setText(grade.getReSemester());
        return view;
    }

    class Holder {
        TextView tvIsPass;
        TextView tvSemester;
        TextView tvCourseNumber;
        TextView tvCourseName;
        TextView tvScore;
        TextView tvCredit;
        TextView tvPeriod;
        TextView tvCourseNature;
        TextView tvCourseType;
        TextView tvExamNature;
        TextView tvExamMethod;
        TextView tvMark;
        TextView tvReSemester;
    }
}
