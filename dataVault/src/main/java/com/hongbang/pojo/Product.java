package com.hongbang.pojo;

public class Product {
    private int id;
    private int parentId;//物料属于哪个分类
    private String name;//物料名称
    private String url;//物料图片地址
    private String materialNumber;//物料物料号
    private double brand;//价格
    private String description;//物料描述
    private String number;//仓库剩余量
    private int vault;
    private int deleteSign;//删除标志
    private String unit;//数量的单位
    private String priceUnit;//价格单位

    public Product(int id, int parentId, String name, String url, String materialNumber, double brand, String description, String number, int vault, int deleteSign, String unit, String priceUnit) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.url = url;
        this.materialNumber = materialNumber;
        this.brand = brand;
        this.description = description;
        this.number = number;
        this.vault = vault;
        this.deleteSign = deleteSign;
        this.unit = unit;
        this.priceUnit = priceUnit;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public double getBrand() {
        return brand;
    }

    public void setBrand(double brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getVault() {
        return vault;
    }

    public void setVault(int vault) {
        this.vault = vault;
    }

    public int getDeleteSign() {
        return deleteSign;
    }

    public void setDeleteSign(int deleteSign) {
        this.deleteSign = deleteSign;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", materialNumber='" + materialNumber + '\'' +
                ", brand=" + brand +
                ", description='" + description + '\'' +
                ", number='" + number + '\'' +
                ", vault=" + vault +
                ", deleteSign=" + deleteSign +
                ", unit='" + unit + '\'' +
                ", priceUnit='" + priceUnit + '\'' +
                '}';
    }
}
