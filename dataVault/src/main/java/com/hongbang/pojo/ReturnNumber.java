package com.hongbang.pojo;

public class ReturnNumber {
    private int id;
    private double number;
    private int cgId;
    private int type;

    public ReturnNumber(int id, double number, int cgId, int type) {
        this.id = id;
        this.number = number;
        this.cgId = cgId;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public int getCgId() {
        return cgId;
    }

    public void setCgId(int cgId) {
        this.cgId = cgId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ReturnNumber{" +
                "id=" + id +
                ", number=" + number +
                ", cgId=" + cgId +
                ", type=" + type +
                '}';
    }
}
