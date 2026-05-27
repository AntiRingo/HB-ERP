package com.hongbang.pojo;

public class FunctionTwo {
    private int id;
    private int functionId;
    private String name;

    public FunctionTwo(int id, int functionId, String name) {
        this.id = id;
        this.functionId = functionId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFunctionId() {
        return functionId;
    }

    public void setFunctionId(int functionId) {
        this.functionId = functionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FunctionTwo{" +
                "id=" + id +
                ", functionId=" + functionId +
                ", name='" + name + '\'' +
                '}';
    }
}
