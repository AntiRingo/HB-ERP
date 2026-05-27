package com.hongbang.pojo;

import java.util.List;

public class UserPickerData {
    private int id;//部门id
    private String name;//部门名称
    private boolean expanded;//展开或者折叠
    private List<Members> members;

    public UserPickerData(int id, String name, boolean expanded, List<Members> members) {
        this.id = id;
        this.name = name;
        this.expanded = expanded;
        this.members = members;
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

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public List<Members> getMembers() {
        return members;
    }

    public void setMembers(List<Members> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "UserPickerData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", expanded=" + expanded +
                ", members=" + members +
                '}';
    }
}
