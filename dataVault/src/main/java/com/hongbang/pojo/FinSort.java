package com.hongbang.pojo;

public class FinSort {
    private int id;//产品仓库分类
    private int parentId;//上级id
    private String finSortName;//分类名称
    private int finSortLevel;//分类等级
    private String finSortDescription;//分类描述
    private String finSortCode;//分类编码
    private int vault;

    public FinSort(int id, int parentId, String finSortName, int finSortLevel, String finSortDescription, String finSortCode, int vault) {
        this.id = id;
        this.parentId = parentId;
        this.finSortName = finSortName;
        this.finSortLevel = finSortLevel;
        this.finSortDescription = finSortDescription;
        this.finSortCode = finSortCode;
        this.vault = vault;
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

    public String getFinSortName() {
        return finSortName;
    }

    public void setFinSortName(String finSortName) {
        this.finSortName = finSortName;
    }

    public int getFinSortLevel() {
        return finSortLevel;
    }

    public void setFinSortLevel(int finSortLevel) {
        this.finSortLevel = finSortLevel;
    }

    public String getFinSortDescription() {
        return finSortDescription;
    }

    public void setFinSortDescription(String finSortDescription) {
        this.finSortDescription = finSortDescription;
    }

    public String getFinSortCode() {
        return finSortCode;
    }

    public void setFinSortCode(String finSortCode) {
        this.finSortCode = finSortCode;
    }

    public int getVault() {
        return vault;
    }

    public void setVault(int vault) {
        this.vault = vault;
    }

    @Override
    public String toString() {
        return "FinSort{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", finSortName='" + finSortName + '\'' +
                ", finSortLevel=" + finSortLevel +
                ", finSortDescription='" + finSortDescription + '\'' +
                ", finSortCode='" + finSortCode + '\'' +
                ", vault=" + vault +
                '}';
    }
}
