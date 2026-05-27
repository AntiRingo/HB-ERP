package com.hongbang.pojo;

public class FinProduct {
    private int id;//产品仓库产品id
    private int finSortId;//分类id
    private String finProductName;//物料名称
    private String url;//图片地址
    private String finMaterialNumber;//物料号
    private double price;//价格
    private String finDescription;//描述
    private Integer finNumber;//仓库剩余量
    private int vault;
    private int deleteSign;//删除标志
    private String unit;
    private String priceUnit;

    public FinProduct(int id, int finSortId, String finProductName, String url, String finMaterialNumber, double price, String finDescription, Integer finNumber, int vault, int deleteSign, String unit, String priceUnit) {
        this.id = id;
        this.finSortId = finSortId;
        this.finProductName = finProductName;
        this.url = url;
        this.finMaterialNumber = finMaterialNumber;
        this.price = price;
        this.finDescription = finDescription;
        this.finNumber = finNumber;
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

    public int getFinSortId() {
        return finSortId;
    }

    public void setFinSortId(int finSortId) {
        this.finSortId = finSortId;
    }

    public String getFinProductName() {
        return finProductName;
    }

    public void setFinProductName(String finProductName) {
        this.finProductName = finProductName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFinMaterialNumber() {
        return finMaterialNumber;
    }

    public void setFinMaterialNumber(String finMaterialNumber) {
        this.finMaterialNumber = finMaterialNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getFinDescription() {
        return finDescription;
    }

    public void setFinDescription(String finDescription) {
        this.finDescription = finDescription;
    }

    public Integer getFinNumber() {
        return finNumber;
    }

    public void setFinNumber(Integer finNumber) {
        this.finNumber = finNumber;
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
        return "FinProduct{" +
                "id=" + id +
                ", finSortId=" + finSortId +
                ", finProductName='" + finProductName + '\'' +
                ", url='" + url + '\'' +
                ", finMaterialNumber='" + finMaterialNumber + '\'' +
                ", price=" + price +
                ", finDescription='" + finDescription + '\'' +
                ", finNumber=" + finNumber +
                ", vault=" + vault +
                ", deleteSign=" + deleteSign +
                ", unit='" + unit + '\'' +
                ", priceUnit='" + priceUnit + '\'' +
                '}';
    }
}
