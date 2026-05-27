package com.hongbang.pojo;

public class DepartmentFunction {
    private int id;
    private int departId;
    private int functionId;
    private int sort;
    private int level;

    public DepartmentFunction(int id, int departId, int functionId, int sort, int level) {
        this.id = id;
        this.departId = departId;
        this.functionId = functionId;
        this.sort = sort;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDepartId() {
        return departId;
    }

    public void setDepartId(int departId) {
        this.departId = departId;
    }

    public int getFunctionId() {
        return functionId;
    }

    public void setFunctionId(int functionId) {
        this.functionId = functionId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "DepartmentFunction{" +
                "id=" + id +
                ", departId=" + departId +
                ", functionId=" + functionId +
                ", sort=" + sort +
                ", level=" + level +
                '}';
    }
}
