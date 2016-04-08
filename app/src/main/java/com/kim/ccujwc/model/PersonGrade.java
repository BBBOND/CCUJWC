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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonGrade that = (PersonGrade) o;

        if (gradeList != null ? !gradeList.equals(that.gradeList) : that.gradeList != null)
            return false;
        if (totalCredits != null ? !totalCredits.equals(that.totalCredits) : that.totalCredits != null)
            return false;
        if (compulsoryCredits != null ? !compulsoryCredits.equals(that.compulsoryCredits) : that.compulsoryCredits != null)
            return false;
        if (limitCredits != null ? !limitCredits.equals(that.limitCredits) : that.limitCredits != null)
            return false;
        if (professionalElectiveCredits != null ? !professionalElectiveCredits.equals(that.professionalElectiveCredits) : that.professionalElectiveCredits != null)
            return false;
        if (optionalCredits != null ? !optionalCredits.equals(that.optionalCredits) : that.optionalCredits != null)
            return false;
        if (gradePointAverage != null ? !gradePointAverage.equals(that.gradePointAverage) : that.gradePointAverage != null)
            return false;
        if (totalStudyHours != null ? !totalStudyHours.equals(that.totalStudyHours) : that.totalStudyHours != null)
            return false;
        if (totalCoursesNumber != null ? !totalCoursesNumber.equals(that.totalCoursesNumber) : that.totalCoursesNumber != null)
            return false;
        return failedCoursesNumber != null ? failedCoursesNumber.equals(that.failedCoursesNumber) : that.failedCoursesNumber == null;

    }

    @Override
    public int hashCode() {
        int result = gradeList != null ? gradeList.hashCode() : 0;
        result = 31 * result + (totalCredits != null ? totalCredits.hashCode() : 0);
        result = 31 * result + (compulsoryCredits != null ? compulsoryCredits.hashCode() : 0);
        result = 31 * result + (limitCredits != null ? limitCredits.hashCode() : 0);
        result = 31 * result + (professionalElectiveCredits != null ? professionalElectiveCredits.hashCode() : 0);
        result = 31 * result + (optionalCredits != null ? optionalCredits.hashCode() : 0);
        result = 31 * result + (gradePointAverage != null ? gradePointAverage.hashCode() : 0);
        result = 31 * result + (totalStudyHours != null ? totalStudyHours.hashCode() : 0);
        result = 31 * result + (totalCoursesNumber != null ? totalCoursesNumber.hashCode() : 0);
        result = 31 * result + (failedCoursesNumber != null ? failedCoursesNumber.hashCode() : 0);
        return result;
    }
}
