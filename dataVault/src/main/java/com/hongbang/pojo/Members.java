package com.hongbang.pojo;

public class Members {
    private int id;
    private String name;
    private boolean selected;
    private boolean canEdit;
    private int type;

    public Members(int id, String name, boolean selected, boolean canEdit, int type) {
        this.id = id;
        this.name = name;
        this.selected = selected;
        this.canEdit = canEdit;
        this.type = type;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Members{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", selected=" + selected +
                ", canEdit=" + canEdit +
                ", type=" + type +
                '}';
    }
}
