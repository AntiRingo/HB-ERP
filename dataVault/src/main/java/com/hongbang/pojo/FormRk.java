package com.hongbang.pojo;

public class FormRk {
    private int id;//入库具体条目id
    private int appFormId;//申请单id
    private double actualPrice;//实时价格
    private int priceSort;//价格类型;0预估价格；1发票价格
    private int productId;//物料id
    private int vault;//仓库类型
    private String rkTime;//入库时间
    private int rkSort;//入库类型

    public FormRk(int id, int appFormId, double actualPrice, int priceSort, int productId, int vault, String rkTime, int rkSort) {
        this.id = id;
        this.appFormId = appFormId;
        this.actualPrice = actualPrice;
        this.priceSort = priceSort;
        this.productId = productId;
        this.vault = vault;
        this.rkTime = rkTime;
        this.rkSort = rkSort;
    }

    public FormRk(Integer integer, int applicationId, double appPrice, int priceSort, int productId, int vault, String date, int sortTwo) {
    }

    public FormRk() {

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

    public double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public int getPriceSort() {
        return priceSort;
    }

    public void setPriceSort(int priceSort) {
        this.priceSort = priceSort;
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

    public String getRkTime() {
        return rkTime;
    }

    public void setRkTime(String rkTime) {
        this.rkTime = rkTime;
    }

    public int getRkSort() {
        return rkSort;
    }

    public void setRkSort(int rkSort) {
        this.rkSort = rkSort;
    }

    @Override
    public String toString() {
        return "FormRk{" +
                "id=" + id +
                ", appFormId=" + appFormId +
                ", actualPrice=" + actualPrice +
                ", priceSort=" + priceSort +
                ", productId=" + productId +
                ", vault=" + vault +
                ", rkTime='" + rkTime + '\'' +
                ", rkSort=" + rkSort +
                '}';
    }
}
