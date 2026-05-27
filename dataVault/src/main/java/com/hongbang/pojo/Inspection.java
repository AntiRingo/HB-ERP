package com.hongbang.pojo;

public class Inspection {
    private int id;
    private int appId;
    private int productId;
    private int vault;
    private double passRate;//合格率
    private String notes;
    private int pass;//0是未质检，1是质检通过，2是质检不通过

    public Inspection(int id, int appId, int productId, int vault, double passRate, String notes, int pass) {
        this.id = id;
        this.appId = appId;
        this.productId = productId;
        this.vault = vault;
        this.passRate = passRate;
        this.notes = notes;
        this.pass = pass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
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

    public double getPassRate() {
        return passRate;
    }

    public void setPassRate(double passRate) {
        this.passRate = passRate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getPass() {
        return pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "Inspection{" +
                "id=" + id +
                ", appId=" + appId +
                ", productId=" + productId +
                ", vault=" + vault +
                ", passRate=" + passRate +
                ", notes='" + notes + '\'' +
                ", pass=" + pass +
                '}';
    }
}
