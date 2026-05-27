package com.hongbang.pojo;

public class UserFunctionTwo {
    private int id;
    private int functionTwoId;
    private int userId;
    private int openStatus;

    public UserFunctionTwo(int id, int functionTwoId, int userId, int openStatus) {
        this.id = id;
        this.functionTwoId = functionTwoId;
        this.userId = userId;
        this.openStatus = openStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFunctionTwoId() {
        return functionTwoId;
    }

    public void setFunctionTwoId(int functionTwoId) {
        this.functionTwoId = functionTwoId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(int openStatus) {
        this.openStatus = openStatus;
    }

    @Override
    public String toString() {
        return "UserFunctionTwo{" +
                "id=" + id +
                ", functionTwoId=" + functionTwoId +
                ", userId=" + userId +
                ", openStatus=" + openStatus +
                '}';
    }
}
