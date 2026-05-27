package com.hongbang.pojo;

public class Examine {
    private int id;
    private int appFormId;//申请单id
    private int minister;//部长审核


    public Examine(int id, int appFormId, int minister, int boss) {
        this.id = id;
        this.appFormId = appFormId;
        this.minister = minister;

    }

    public Examine() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppFormId() {
        return appFormId;
    }

    public void setAppFormId(int appFormId) {
        this.appFormId = appFormId;
    }

    public int getMinister() {
        return minister;
    }

    public void setMinister(int minister) {
        this.minister = minister;
    }


    @Override
    public String toString() {
        return "Examine{" +
                "id=" + id +
                ", appFormId=" + appFormId +
                ", minister=" + minister +
                '}';
    }
}
