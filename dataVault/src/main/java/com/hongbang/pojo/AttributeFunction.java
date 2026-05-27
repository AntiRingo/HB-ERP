package com.hongbang.pojo;

public class AttributeFunction {
    private int id;
    private int attNameId;
    private int range;
    private int autoCode;
    private int serialCode;
    private int uniqueCode;
    private int displayCode;

    public AttributeFunction(int id, int attNameId, int range, int autoCode, int serialCode, int uniqueCode, int displayCode) {
        this.id = id;
        this.attNameId = attNameId;
        this.range = range;
        this.autoCode = autoCode;
        this.serialCode = serialCode;
        this.uniqueCode = uniqueCode;
        this.displayCode = displayCode;
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

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getAutoCode() {
        return autoCode;
    }

    public void setAutoCode(int autoCode) {
        this.autoCode = autoCode;
    }

    public int getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(int serialCode) {
        this.serialCode = serialCode;
    }

    public int getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(int uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public int getDisplayCode() {
        return displayCode;
    }

    public void setDisplayCode(int displayCode) {
        this.displayCode = displayCode;
    }

    @Override
    public String toString() {
        return "AttributeFunction{" +
                "id=" + id +
                ", attNameId=" + attNameId +
                ", range=" + range +
                ", autoCode=" + autoCode +
                ", serialCode=" + serialCode +
                ", uniqueCode=" + uniqueCode +
                ", displayCode=" + displayCode +
                '}';
    }
}
