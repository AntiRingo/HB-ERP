package com.hongbang.pojo;
//机型
public class Model {
    private int id;
    private String modelName;

    public Model(int id, String modelName) {
        this.id = id;
        this.modelName = modelName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Override
    public String toString() {
        return "Model{" +
                "id=" + id +
                ", modelName='" + modelName + '\'' +
                '}';
    }
}
