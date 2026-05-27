package com.hongbang.pojo;

public class Balance {
    private int id;//每日结存
    private int productId;//物料id
    private int vault;//仓库
    private String todayIn;//今日收入
    private String todayOut;//今日支出
    private String today;//今日结存
    private String time;//时间
    private String todayInNumber;//今日收入数量
    private String todayOutNumber;//今日支出数量
    private String todayNumber;//今日结存数量

    public Balance(int id, int productId, int vault, String todayIn, String todayOut, String today, String time, String todayInNumber, String todayOutNumber, String todayNumber) {
        this.id = id;
        this.productId = productId;
        this.vault = vault;
        this.todayIn = todayIn;
        this.todayOut = todayOut;
        this.today = today;
        this.time = time;
        this.todayInNumber = todayInNumber;
        this.todayOutNumber = todayOutNumber;
        this.todayNumber = todayNumber;
    }

    public Balance() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getVault() {
        return vault;
    }

    public void setVault(int vault) {
        this.vault = vault;
    }

    public String getTodayIn() {
        return todayIn;
    }

    public void setTodayIn(String todayIn) {
        this.todayIn = todayIn;
    }

    public String getTodayOut() {
        return todayOut;
    }

    public void setTodayOut(String todayOut) {
        this.todayOut = todayOut;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTodayInNumber() {
        return todayInNumber;
    }

    public void setTodayInNumber(String todayInNumber) {
        this.todayInNumber = todayInNumber;
    }

    public String getTodayOutNumber() {
        return todayOutNumber;
    }

    public void setTodayOutNumber(String todayOutNumber) {
        this.todayOutNumber = todayOutNumber;
    }

    public String getTodayNumber() {
        return todayNumber;
    }

    public void setTodayNumber(String todayNumber) {
        this.todayNumber = todayNumber;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "id=" + id +
                ", productId=" + productId +
                ", vault=" + vault +
                ", todayIn='" + todayIn + '\'' +
                ", todayOut='" + todayOut + '\'' +
                ", today='" + today + '\'' +
                ", time='" + time + '\'' +
                ", todayInNumber='" + todayInNumber + '\'' +
                ", todayOutNumber='" + todayOutNumber + '\'' +
                ", todayNumber='" + todayNumber + '\'' +
                '}';
    }
}
