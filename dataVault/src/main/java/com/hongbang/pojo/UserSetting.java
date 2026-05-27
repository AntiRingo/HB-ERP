package com.hongbang.pojo;

public class UserSetting {
    private  int id;
    private String sort;
    private String user;
    private String infoHead;
    private String info;

    public UserSetting(int id, String sort, String user, String infoHead, String info) {
        this.id = id;
        this.sort = sort;
        this.user = user;
        this.infoHead = infoHead;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getInfoHead() {
        return infoHead;
    }

    public void setInfoHead(String infoHead) {
        this.infoHead = infoHead;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "UserSetting{" +
                "id=" + id +
                ", sort='" + sort + '\'' +
                ", user='" + user + '\'' +
                ", infoHead='" + infoHead + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}
