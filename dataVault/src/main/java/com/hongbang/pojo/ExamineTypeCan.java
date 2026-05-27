package com.hongbang.pojo;

public class ExamineTypeCan {
    private int id;
    private int examineId;
    private int  canPrice;
    private int canNumber;


    public ExamineTypeCan(int id, int examineId, int canPrice, int canNumber) {
        this.id = id;
        this.examineId = examineId;
        this.canPrice = canPrice;
        this.canNumber = canNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExamineId() {
        return examineId;
    }

    public void setExamineId(int examineId) {
        this.examineId = examineId;
    }

    public int getCanPrice() {
        return canPrice;
    }

    public void setCanPrice(int canPrice) {
        this.canPrice = canPrice;
    }

    public int getCanNumber() {
        return canNumber;
    }

    public void setCanNumber(int canNumber) {
        this.canNumber = canNumber;
    }

    @Override
    public String toString() {
        return "ExamineTypeCan{" +
                "id=" + id +
                ", examineId=" + examineId +
                ", canPrice=" + canPrice +
                ", canNumber=" + canNumber +
                '}';
    }
}
