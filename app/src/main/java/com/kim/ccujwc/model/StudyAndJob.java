package com.kim.ccujwc.model;

/**
 * Created by kim on 16-3-19.
 */
public class StudyAndJob {

    private String period;
    private String schoolOrWorkUnit;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSchoolOrWorkUnit() {
        return schoolOrWorkUnit;
    }

    public void setSchoolOrWorkUnit(String schoolOrWorkUnit) {
        this.schoolOrWorkUnit = schoolOrWorkUnit;
    }

    @Override
    public String toString() {
        return "StudyAndJob{" +
                "period='" + period + '\'' +
                ", schoolOrWorkUnit='" + schoolOrWorkUnit + '\'' +
                '}';
    }
}
