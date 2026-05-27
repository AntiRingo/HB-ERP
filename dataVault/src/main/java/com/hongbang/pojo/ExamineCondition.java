package com.hongbang.pojo;

public class ExamineCondition {
    private int id;
    private String name;

    public ExamineCondition(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ExamineCondition{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
