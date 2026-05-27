package com.hongbang.pojo;

import java.util.List;

public class BomUserPurview {
    private int id;
    private String name;
    private List<Members>members;

    public BomUserPurview(int id, String name, List<Members> members) {
        this.id = id;
        this.name = name;
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

    public List<Members> getMembers() {
        return members;
    }

    public void setMembers(List<Members> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "BomUserPurview{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", members=" + members +
                '}';
    }
}
