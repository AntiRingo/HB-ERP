package com.hongbang.pojo;

public class ExamineAllType {
    private int id;
    private int typeId;
    private int departmentId;
    private int level;
    private int step;
    private int deleteSign;

    public ExamineAllType(int id, int typeId, int departmentId, int level, int step, int deleteSign) {
        this.id = id;
        this.typeId = typeId;
        this.departmentId = departmentId;
        this.level = level;
        this.step = step;
        this.deleteSign = deleteSign;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getDeleteSign() {
        return deleteSign;
    }

    public void setDeleteSign(int deleteSign) {
        this.deleteSign = deleteSign;
    }

    @Override
    public String toString() {
        return "ExamineAllType{" +
                "id=" + id +
                ", typeId=" + typeId +
                ", departmentId=" + departmentId +
                ", level=" + level +
                ", step=" + step +
                ", deleteSign=" + deleteSign +
                '}';
    }
}
