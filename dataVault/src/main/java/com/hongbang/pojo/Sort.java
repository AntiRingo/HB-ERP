package com.hongbang.pojo;

public class Sort {
    private int id;
    private int parentId;
    private String name;
    private int level;
    private String describe;
    private String code;
    private int vault;

    public Sort(int id, int parentId, String name, int level, String describe, String code, int vault) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.level = level;
        this.describe = describe;
        this.code = code;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getVault() {
        return vault;
    }

    public void setVault(int vault) {
        this.vault = vault;
    }

    @Override
    public String toString() {
        return "Sort{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", describe='" + describe + '\'' +
                ", code='" + code + '\'' +
                ", vault=" + vault +
                '}';
    }
}
