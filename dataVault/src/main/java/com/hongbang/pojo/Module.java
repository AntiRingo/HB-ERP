package com.hongbang.pojo;

public class Module {
    private int id;
    private String name;
    private int reorder;

    public Module(int id, String name, int reorder) {
        this.id = id;
        this.name = name;
        this.reorder = reorder;
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

    public int getReorder() {
        return reorder;
    }

    public void setReorder(int reorder) {
        this.reorder = reorder;
    }

    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", reorder=" + reorder +
                '}';
    }
}
