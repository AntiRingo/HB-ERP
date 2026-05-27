package com.hongbang.pojo;

public class FormCk {
    private int id;//入库具体条目id
    private int appFormId;//申请单id
    private int productId;//物料id
    private int vault;//仓库类型
    private double ckNumber;//实时出库数量
    private String ckTime;//出库时间
    private int ckSort;//出库类型
    private double ckTotal;//出库后已出库总数


    public FormCk(int id, int appFormId, int productId, int vault, double ckNumber, String ckTime, int ckSort, double ckTotal) {
        this.id = id;
        this.appFormId = appFormId;
        this.productId = productId;
        this.vault = vault;
        this.ckNumber = ckNumber;
        this.ckTime = ckTime;
        this.ckSort = ckSort;
        this.ckTotal = ckTotal;
    }

    public FormCk() {

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

    public double getCkNumber() {
        return ckNumber;
    }

    public void setCkNumber(double ckNumber) {
        this.ckNumber = ckNumber;
    }

    public String getCkTime() {
        return ckTime;
    }

    public void setCkTime(String ckTime) {
        this.ckTime = ckTime;
    }

    public int getCkSort() {
        return ckSort;
    }

    public void setCkSort(int ckSort) {
        this.ckSort = ckSort;
    }

    public double getCkTotal() {
        return ckTotal;
    }

    public void setCkTotal(double ckTotal) {
        this.ckTotal = ckTotal;
    }

    @Override
    public String toString() {
        return "FormCk{" +
                "id=" + id +
                ", appFormId=" + appFormId +
                ", productId=" + productId +
                ", vault=" + vault +
                ", ckNumber=" + ckNumber +
                ", ckTime='" + ckTime + '\'' +
                ", ckSort=" + ckSort +
                ", ckTotal=" + ckTotal +
                '}';
    }
}
