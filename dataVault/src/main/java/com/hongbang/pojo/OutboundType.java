package com.hongbang.pojo;
//出入库类型
public class OutboundType {
    private int id;
    private String type;
    private int sort;

    public OutboundType(int id, String type, int sort) {
        this.id = id;
        this.type = type;
        this.sort = sort;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "OutboundType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", sort=" + sort +
                '}';
    }
}
