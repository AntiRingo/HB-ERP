package com.hongbang.pojo;

public class Log {
    private int id;
    private String date;
    private int manager;//经手人ID
    private int appFormId;//申请单ID
    private int productId;//申请的物料ID
    private int vault;//申请的物料属于哪个仓库
    private double logNumber;//操作的数量
    private String url;//签字图片地址

    public Log(int id, String date, int manager, int appFormId, int productId, int vault, double logNumber, String url) {
        this.id = id;
        this.date = date;
        this.manager = manager;
        this.appFormId = appFormId;
        this.productId = productId;
        this.vault = vault;
        this.logNumber = logNumber;
        this.url = url;
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

    public int getAppFormId() {
        return appFormId;
    }

    public void setAppFormId(int appFormId) {
        this.appFormId = appFormId;
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

    public double getLogNumber() {
        return logNumber;
    }

    public void setLogNumber(double logNumber) {
        this.logNumber = logNumber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", manager=" + manager +
                ", appFormId=" + appFormId +
                ", productId=" + productId +
                ", vault=" + vault +
                ", logNumber=" + logNumber +
                ", url='" + url + '\'' +
                '}';
    }
}
