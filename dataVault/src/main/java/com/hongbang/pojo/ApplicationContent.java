package com.hongbang.pojo;

public class ApplicationContent {
    private int id;
    private int applicationId;//applicationForm表的id
    private int productId;//物料id
    private double appNumber;//申请数量
    private double actualNumber;//实际数量
    private int vault;//属于哪个仓库
    private double appPrice;//申请时的价格
    private int finProductId;//记录申请单中申请的零件用于制作哪一个产品
    private int finProductNumber;//记录申请成品的数量
    private int lastLevel;//上一级的ID
    private int bomTitleId;//产品的哪个BOM表

    public ApplicationContent(int id, int applicationId, int productId, double appNumber, double actualNumber, int vault, double appPrice, int finProductId, int finProductNumber, int lastLevel, int bomTitleId) {
        this.id = id;
        this.applicationId = applicationId;
        this.productId = productId;
        this.appNumber = appNumber;
        this.actualNumber = actualNumber;
        this.vault = vault;
        this.appPrice = appPrice;
        this.finProductId = finProductId;
        this.finProductNumber = finProductNumber;
        this.lastLevel = lastLevel;
        this.bomTitleId = bomTitleId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(double appNumber) {
        this.appNumber = appNumber;
    }

    public double getActualNumber() {
        return actualNumber;
    }

    public void setActualNumber(double actualNumber) {
        this.actualNumber = actualNumber;
    }

    public int getVault() {
        return vault;
    }

    public void setVault(int vault) {
        this.vault = vault;
    }

    public double getAppPrice() {
        return appPrice;
    }

    public void setAppPrice(double appPrice) {
        this.appPrice = appPrice;
    }

    public int getFinProductId() {
        return finProductId;
    }

    public void setFinProductId(int finProductId) {
        this.finProductId = finProductId;
    }

    public int getFinProductNumber() {
        return finProductNumber;
    }

    public void setFinProductNumber(int finProductNumber) {
        this.finProductNumber = finProductNumber;
    }

    public int getLastLevel() {
        return lastLevel;
    }

    public void setLastLevel(int lastLevel) {
        this.lastLevel = lastLevel;
    }

    public int getBomTitleId() {
        return bomTitleId;
    }

    public void setBomTitleId(int bomTitleId) {
        this.bomTitleId = bomTitleId;
    }

    @Override
    public String toString() {
        return "ApplicationContent{" +
                "id=" + id +
                ", applicationId=" + applicationId +
                ", productId=" + productId +
                ", appNumber=" + appNumber +
                ", actualNumber=" + actualNumber +
                ", vault=" + vault +
                ", appPrice=" + appPrice +
                ", finProductId=" + finProductId +
                ", finProductNumber=" + finProductNumber +
                ", lastLevel=" + lastLevel +
                ", bomTitleId=" + bomTitleId +
                '}';
    }
}
