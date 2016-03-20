package com.kim.ccujwc.model;

import java.util.List;

/**
 * Created by 伟阳 on 2016/3/13.
 */
public class PersonGrade {

    private List<Grade> gradeList;
    private String totalCredits;
    private String compulsoryCredits;
    private String limitCredits;
    private String professionalElectiveCredits;
    private String optionalCredits;
    private String gradePointAverage;
    private String totalStudyHours;
    private String totalCoursesNumber;
    private String failedCoursesNumber;

    public List<Grade> getGradeList() {
        return gradeList;
    }

    public void setGradeList(List<Grade> gradeList) {
        this.gradeList = gradeList;
    }

    public String getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(String totalCredits) {
        this.totalCredits = totalCredits;
    }

    public String getCompulsoryCredits() {
        return compulsoryCredits;
    }

    public void setCompulsoryCredits(String compulsoryCredits) {
        this.compulsoryCredits = compulsoryCredits;
    }

    public String getLimitCredits() {
        return limitCredits;
    }

    public void setLimitCredits(String limitCredits) {
        this.limitCredits = limitCredits;
    }

    public String getProfessionalElectiveCredits() {
        return professionalElectiveCredits;
    }

    public void setProfessionalElectiveCredits(String professionalElectiveCredits) {
        this.professionalElectiveCredits = professionalElectiveCredits;
    }

    public String getOptionalCredits() {
        return optionalCredits;
    }

    public void setOptionalCredits(String optionalCredits) {
        this.optionalCredits = optionalCredits;
    }

    public String getGradePointAverage() {
        return gradePointAverage;
    }

    public void setGradePointAverage(String gradePointAverage) {
        this.gradePointAverage = gradePointAverage;
    }

    public String getTotalStudyHours() {
        return totalStudyHours;
    }

    public void setTotalStudyHours(String totalStudyHours) {
        this.totalStudyHours = totalStudyHours;
    }

    public String getTotalCoursesNumber() {
        return totalCoursesNumber;
    }

    public void setTotalCoursesNumber(String totalCoursesNumber) {
        this.totalCoursesNumber = totalCoursesNumber;
    }

    public String getFailedCoursesNumber() {
        return failedCoursesNumber;
    }

    public void setFailedCoursesNumber(String failedCoursesNumber) {
        this.failedCoursesNumber = failedCoursesNumber;
    }

    @Override
    public String toString() {
        return "PersonGrade{" +
                "gradeList=" + gradeList.toString() +
                ", totalCredits='" + totalCredits + '\'' +
                ", compulsoryCredits='" + compulsoryCredits + '\'' +
                ", limitCredits='" + limitCredits + '\'' +
                ", professionalElectiveCredits='" + professionalElectiveCredits + '\'' +
                ", optionalCredits='" + optionalCredits + '\'' +
                ", gradePointAverage='" + gradePointAverage + '\'' +
                ", totalStudyHours='" + totalStudyHours + '\'' +
                ", totalCoursesNumber='" + totalCoursesNumber + '\'' +
                ", failedCoursesNumber='" + failedCoursesNumber + '\'' +
                '}';
    }
}
