package com.hongbang.pojo;

public class FinLog {
    private int id;//成品半产品仓库出入库记录
    private String date;
    private int manager;//经手人
    private int userId;//申请人
    private int finFormId;//申请订单id
    private int finProductId;//申请的物料id
    private int number;//申请的数量

    public FinLog(int id, String date, int manager, int userId, int finFormId, int finProductId, int number) {
        this.id = id;
        this.date = date;
        this.manager = manager;
        this.userId = userId;
        this.finFormId = finFormId;
        this.finProductId = finProductId;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getManager() {
        return manager;
    }

    public void setManager(int manager) {
        this.manager = manager;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFinFormId() {
        return finFormId;
    }

    public void setFinFormId(int finFormId) {
        this.finFormId = finFormId;
    }

    public int getFinProductId() {
        return finProductId;
    }

    public void setFinProductId(int finProductId) {
        this.finProductId = finProductId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "FinLog{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", manager=" + manager +
                ", userId=" + userId +
                ", finFormId=" + finFormId +
                ", finProductId=" + finProductId +
                ", number=" + number +
                '}';
    }
}
