package com.hongbang.pojo;

public class UpdateNumberOfTimes {
    private int id;
    private int productId;
    private int updateNumber;

    public UpdateNumberOfTimes(int id, int productId, int updateNumber) {
        this.id = id;
        this.productId = productId;
        this.updateNumber = updateNumber;
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

    public int getUpdateNumber() {
        return updateNumber;
    }

    public void setUpdateNumber(int updateNumber) {
        this.updateNumber = updateNumber;
    }

    @Override
    public String toString() {
        return "UpdateNumberOfTimes{" +
                "id=" + id +
                ", productId=" + productId +
                ", updateNumber=" + updateNumber +
                '}';
    }
}
