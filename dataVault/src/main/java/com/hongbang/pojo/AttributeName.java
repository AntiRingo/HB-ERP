package com.hongbang.pojo;

public class AttributeName {
    private int id;
    private int parentId ;
    private String name;
    private String unit;
    private int length;
    private int publicApplication;

    public AttributeName(int id , int parentId, String name, String unit, int length, int publicApplication) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.unit = unit;
        this.length = length;
        this.publicApplication = publicApplication;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPublicApplication() {
        return publicApplication;
    }

    public void setPublicApplication(int publicApplication) {
        this.publicApplication = publicApplication;
    }

    @Override
    public String toString() {
        return "AttributeName{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", length=" + length +
                ", publicApplication=" + publicApplication +
                '}';
    }
}
