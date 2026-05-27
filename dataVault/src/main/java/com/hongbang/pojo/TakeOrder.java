package com.hongbang.pojo;

public class TakeOrder {
    private int id;
    private int appFormId;
    private int user;
    private int takeOrderStatus;

    public TakeOrder(int id, int appFormId, int user, int takeOrderStatus) {
        this.id = id;
        this.appFormId = appFormId;
        this.user = user;
        this.takeOrderStatus = takeOrderStatus;
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

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getTakeOrderStatus() {
        return takeOrderStatus;
    }

    public void setTakeOrderStatus(int takeOrderStatus) {
        this.takeOrderStatus = takeOrderStatus;
    }

    @Override
    public String toString() {
        return "TakeOrder{" +
                "id=" + id +
                ", appFormId=" + appFormId +
                ", user=" + user +
                ", takeOrderStatus=" + takeOrderStatus +
                '}';
    }
}
