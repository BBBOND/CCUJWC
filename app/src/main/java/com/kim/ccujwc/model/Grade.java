package com.kim.ccujwc.model;

/**
 * Created by 伟阳 on 2016/3/12.
 */
public class Grade {
    private String isPass;//通过否
    private String semester;//开课学期
    private String courseNumber;//课程编号
    private String courseName;//课程名称
    private String socre;//总成绩
    private String credit;//学分
    private String period;//学时
    private String courseNature;//课程性质
    private String courseType;//课程类别
    private String examNature;//考试性质
    private String examMethod;//考核方式
    private String mark;//标志
    private String reSemester;//补重学期

    public String getIsPass() {
        return isPass;
    }

    public void setIsPass(String isPass) {
        this.isPass = isPass;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSocre() {
        return socre;
    }

    public void setSocre(String socre) {
        this.socre = socre;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getCourseNature() {
        return courseNature;
    }

    public void setCourseNature(String courseNature) {
        this.courseNature = courseNature;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getExamNature() {
        return examNature;
    }

    public void setExamNature(String examNature) {
        this.examNature = examNature;
    }

    public String getExamMethod() {
        return examMethod;
    }

    public void setExamMethod(String examMethod) {
        this.examMethod = examMethod;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getReSemester() {
        return reSemester;
    }

    public void setReSemester(String reSemester) {
        this.reSemester = reSemester;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "isPass='" + isPass + '\'' +
                ", semester='" + semester + '\'' +
                ", courseNumber='" + courseNumber + '\'' +
                ", courseName='" + courseName + '\'' +
                ", socre='" + socre + '\'' +
                ", credit='" + credit + '\'' +
                ", period='" + period + '\'' +
                ", courseNature='" + courseNature + '\'' +
                ", courseType='" + courseType + '\'' +
                ", examNature='" + examNature + '\'' +
                ", examMethod='" + examMethod + '\'' +
                ", mark='" + mark + '\'' +
                ", reSemester='" + reSemester + '\'' +
                '}';
    }
}
