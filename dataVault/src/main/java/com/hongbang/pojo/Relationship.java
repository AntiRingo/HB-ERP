package com.hongbang.pojo;

public class Relationship {
    private int id;
    private int oldAppFormId;
    private int newAppFormId;
    private int appContentId;
    private int newAppContentId;
    private double number;

    public Relationship(int id, int oldAppFormId, int newAppFormId, int appContentId, int newAppContentId, double number) {
        this.id = id;
        this.oldAppFormId = oldAppFormId;
        this.newAppFormId = newAppFormId;
        this.appContentId = appContentId;
        this.newAppContentId = newAppContentId;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOldAppFormId() {
        return oldAppFormId;
    }

    public void setOldAppFormId(int oldAppFormId) {
        this.oldAppFormId = oldAppFormId;
    }

    public int getNewAppFormId() {
        return newAppFormId;
    }

    public void setNewAppFormId(int newAppFormId) {
        this.newAppFormId = newAppFormId;
    }

    public int getAppContentId() {
        return appContentId;
    }

    public void setAppContentId(int appContentId) {
        this.appContentId = appContentId;
    }

    public int getNewAppContentId() {
        return newAppContentId;
    }

    public void setNewAppContentId(int newAppContentId) {
        this.newAppContentId = newAppContentId;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Relationship{" +
                "id=" + id +
                ", oldAppFormId=" + oldAppFormId +
                ", newAppFormId=" + newAppFormId +
                ", appContentId=" + appContentId +
                ", newAppContentId=" + newAppContentId +
                ", number=" + number +
                '}';
    }
}
