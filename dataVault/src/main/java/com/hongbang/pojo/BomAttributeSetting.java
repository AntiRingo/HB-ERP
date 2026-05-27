package com.hongbang.pojo;

public class BomAttributeSetting {
    private int id;
    private int attributeId;

    public BomAttributeSetting(int id, int attributeId) {
        this.id = id;
        this.attributeId = attributeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    @Override
    public String toString() {
        return "BomAttributeSetting{" +
                "id=" + id +
                ", attributeId=" + attributeId +
                '}';
    }
}
