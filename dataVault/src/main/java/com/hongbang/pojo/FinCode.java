package com.hongbang.pojo;

public class FinCode {
    private int id;
    private  int length;
    private  String description;
    private  String remove;
    private  int level;

    public FinCode(int id, int length, String description, String remove, int level) {
        this.id = id;
        this.length = length;
        this.description = description;
        this.remove = remove;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemove() {
        return remove;
    }

    public void setRemove(String remove) {
        this.remove = remove;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "FinCode{" +
                "id=" + id +
                ", length=" + length +
                ", description='" + description + '\'' +
                ", remove='" + remove + '\'' +
                ", level=" + level +
                '}';
    }
}
