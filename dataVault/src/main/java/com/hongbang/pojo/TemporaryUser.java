package com.hongbang.pojo;

//临时用户
public class TemporaryUser {
    private int id;
    private String createTime;
    private String overTime;
    private int userId;
    private String userName;
    private String passWord;
    private int department;

    public TemporaryUser(int id, String createTime, String overTime, int userId, String userName, String passWord, int department) {
        this.id = id;
        this.createTime = createTime;
        this.overTime = overTime;
        this.userId = userId;
        this.userName = userName;
        this.passWord = passWord;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "TemporaryUser{" +
                "id=" + id +
                ", createTime='" + createTime + '\'' +
                ", overTime='" + overTime + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", department=" + department +
                '}';
    }
}
