package com.kim.ccujwc.model;

/**
 * Created by 伟阳 on 2016/3/14.
 */
public class Course {
    private String courseNum;
    private String courseCode;
    private String courseName;
    private String courseTeacher;
    private String courseStartTime;
    private String courseStartWeek;
    private String courseEndWeek;
    private int isSingleWeek;
    private String coursePlace;
    private String courseClass;

    public String getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(String courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public String getCourseStartTime() {
        return courseStartTime;
    }

    public void setCourseStartTime(String courseStartTime) {
        this.courseStartTime = courseStartTime;
    }

    public String getCourseStartWeek() {
        return courseStartWeek;
    }

    public void setCourseStartWeek(String courseStartWeek) {
        this.courseStartWeek = courseStartWeek;
    }

    public String getCourseEndWeek() {
        return courseEndWeek;
    }

    public void setCourseEndWeek(String courseEndWeek) {
        this.courseEndWeek = courseEndWeek;
    }

    public int getIsSingleWeek() {
        return isSingleWeek;
    }

    public void setIsSingleWeek(int isSingleWeek) {
        this.isSingleWeek = isSingleWeek;
    }

    public String getCoursePlace() {
        return coursePlace;
    }

    public void setCoursePlace(String coursePlace) {
        this.coursePlace = coursePlace;
    }

    public String getCourseClass() {
        return courseClass;
    }

    public void setCourseClass(String courseClass) {
        this.courseClass = courseClass;
    }

    @Override
    public String toString() {
        String text = "临班编号：" + courseClass + '\n' +
                "课程名称：" + courseName + '\n' +
                "任课教师：" + courseTeacher + '\n' +
                "上课时间：" + courseStartTime + '\n' +
                "周数：" + courseStartWeek + '-' + courseEndWeek;

        if (isSingleWeek == 1) {
            text += "单周\n";
        } else if (isSingleWeek == 2) {
            text += "双周\n";
        } else {
            text += '\n';
        }
        text += "上课地点：" + coursePlace + '\n';
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (isSingleWeek != course.isSingleWeek) return false;
        if (courseNum != null ? !courseNum.equals(course.courseNum) : course.courseNum != null)
            return false;
        if (courseCode != null ? !courseCode.equals(course.courseCode) : course.courseCode != null)
            return false;
        if (courseName != null ? !courseName.equals(course.courseName) : course.courseName != null)
            return false;
        if (courseTeacher != null ? !courseTeacher.equals(course.courseTeacher) : course.courseTeacher != null)
            return false;
        if (courseStartTime != null ? !courseStartTime.equals(course.courseStartTime) : course.courseStartTime != null)
            return false;
        if (courseStartWeek != null ? !courseStartWeek.equals(course.courseStartWeek) : course.courseStartWeek != null)
            return false;
        if (courseEndWeek != null ? !courseEndWeek.equals(course.courseEndWeek) : course.courseEndWeek != null)
            return false;
        if (coursePlace != null ? !coursePlace.equals(course.coursePlace) : course.coursePlace != null)
            return false;
        return courseClass != null ? courseClass.equals(course.courseClass) : course.courseClass == null;

    }

    @Override
    public int hashCode() {
        int result = courseNum != null ? courseNum.hashCode() : 0;
        result = 31 * result + (courseCode != null ? courseCode.hashCode() : 0);
        result = 31 * result + (courseName != null ? courseName.hashCode() : 0);
        result = 31 * result + (courseTeacher != null ? courseTeacher.hashCode() : 0);
        result = 31 * result + (courseStartTime != null ? courseStartTime.hashCode() : 0);
        result = 31 * result + (courseStartWeek != null ? courseStartWeek.hashCode() : 0);
        result = 31 * result + (courseEndWeek != null ? courseEndWeek.hashCode() : 0);
        result = 31 * result + isSingleWeek;
        result = 31 * result + (coursePlace != null ? coursePlace.hashCode() : 0);
        result = 31 * result + (courseClass != null ? courseClass.hashCode() : 0);
        return result;
    }
}
