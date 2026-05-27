package com.hongbang.pojo;

public class ProductExamine {
    private  int id;//提交的物料信息审核
    private  int userId;
    private  int productId;
    private  int vault;
    private  int status;//审核状态
    private  String note;
    private  String appTime;
    private int examineId;//审核人id

    public ProductExamine(int id, int userId, int productId, int vault, int status, String note, String appTime, int examineId) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.vault = vault;
        this.status = status;
        this.note = note;
        this.appTime = appTime;
        this.examineId = examineId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getnote() {
        return note;
    }

    public void setnote(String note) {
        this.note = note;
    }

    public String getAppTime() {
        return appTime;
    }

    public void setAppTime(String appTime) {
        this.appTime = appTime;
    }

    public int getExamineId() {
        return examineId;
    }

    public void setExamineId(int examineId) {
        this.examineId = examineId;
    }

    @Override
    public String toString() {
        return "ProductExamine{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", vault=" + vault +
                ", status=" + status +
                ", note='" + note + '\'' +
                ", appTime='" + appTime + '\'' +
                ", examineId=" + examineId +
                '}';
    }
}
