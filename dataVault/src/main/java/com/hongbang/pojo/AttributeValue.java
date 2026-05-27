package com.hongbang.pojo;

public class AttributeValue {
    private int id;
    private int attNameId;
    private String code;
    private String attValue;

    public AttributeValue(int id, int attNameId, String code, String attValue) {
        this.id = id;
        this.attNameId = attNameId;
        this.code = code;
        this.attValue = attValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAttNameId() {
        return attNameId;
    }

    public void setAttNameId(int attNameId) {
        this.attNameId = attNameId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAttValue() {
        return attValue;
    }

    public void setAttValue(String attValue) {
        this.attValue = attValue;
    }

    @Override
    public String toString() {
        return "AttributeValue{" +
                "id=" + id +
                ", attNameId=" + attNameId +
                ", code='" + code + '\'' +
                ", attValue='" + attValue + '\'' +
                '}';
    }
}
