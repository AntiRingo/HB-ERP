package com.hongbang.pojo;

public class FinAttributeValue {
    private int id;
    private int finAttNameId;
    private String code;
    private String finAttValue;

    public FinAttributeValue(int id, int finAttNameId, String code, String finAttValue) {
        this.id = id;
        this.finAttNameId = finAttNameId;
        this.code = code;
        this.finAttValue = finAttValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFinAttNameId() {
        return finAttNameId;
    }

    public void setFinAttNameId(int finAttNameId) {
        this.finAttNameId = finAttNameId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFinAttValue() {
        return finAttValue;
    }

    public void setFinAttValue(String finAttValue) {
        this.finAttValue = finAttValue;
    }

    @Override
    public String toString() {
        return "FinAttributeValue{" +
                "id=" + id +
                ", finAttNameId=" + finAttNameId +
                ", code='" + code + '\'' +
                ", finAttValue='" + finAttValue + '\'' +
                '}';
    }
}
