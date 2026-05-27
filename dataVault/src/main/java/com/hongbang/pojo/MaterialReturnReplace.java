package com.hongbang.pojo;

public class MaterialReturnReplace {
    private int id;
    private int receiveId;
    private int newProduct;
    private int newVault;
    private String remark;


    public MaterialReturnReplace(int id, int receiveId, int newProduct, int newVault, String remark) {
        this.id = id;
        this.receiveId = receiveId;
        this.newProduct = newProduct;
        this.newVault = newVault;
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(int receiveId) {
        this.receiveId = receiveId;
    }

    public int getNewProduct() {
        return newProduct;
    }

    public void setNewProduct(int newProduct) {
        this.newProduct = newProduct;
    }

    public int getNewVault() {
        return newVault;
    }

    public void setNewVault(int newVault) {
        this.newVault = newVault;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "MaterialReturnReplace{" +
                "id=" + id +
                ", receiveId=" + receiveId +
                ", newProduct=" + newProduct +
                ", newVault=" + newVault +
                ", remark='" + remark + '\'' +
                '}';
    }
}
