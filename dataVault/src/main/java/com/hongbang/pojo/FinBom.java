package com.hongbang.pojo;

public class FinBom {
    private int id;//BOM表
    private int bomTitleId;//标题信息(哪个物料的id，以及BOM表版本信息)
    private int status;//BOM表状态
    private int productId;//BOM表中存在哪些物料（零件仓库的）
    private int vault;//仓库标识
    private int number;//所需数量
    private String partNumber;//零件号
    private String notes;//备注

    public FinBom(int id, int bomTitleId, int status, int productId, int vault, int number, String partNumber, String notes) {
        this.id = id;
        this.bomTitleId = bomTitleId;
        this.status = status;
        this.productId = productId;
        this.vault = vault;
        this.number = number;
        this.partNumber = partNumber;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBomTitleId() {
        return bomTitleId;
    }

    public void setBomTitleId(int bomTitleId) {
        this.bomTitleId = bomTitleId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "FinBom{" +
                "id=" + id +
                ", bomTitleId=" + bomTitleId +
                ", status=" + status +
                ", productId=" + productId +
                ", vault=" + vault +
                ", number=" + number +
                ", partNumber='" + partNumber + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
