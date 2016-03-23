package com.kim.ccujwc.model;

/**
 * Created by kim on 16-3-19.
 */
public class FamilyMember {
    private String fullName;
    private String politicalLandscape;
    private String age;
    private String relationship;
    private String workUnit;
    private String position;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPoliticalLandscape() {
        return politicalLandscape;
    }

    public void setPoliticalLandscape(String politicalLandscape) {
        this.politicalLandscape = politicalLandscape;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "FamilyMember{" +
                "fullName='" + fullName + '\'' +
                ", politicalLandscape='" + politicalLandscape + '\'' +
                ", age='" + age + '\'' +
                ", relationship='" + relationship + '\'' +
                ", workUnit='" + workUnit + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
