package com.hongbang.pojo;

public class UserFunction {
    private int id;
    private int userId;
    private int functionId;
    private int open;

    public UserFunction(int id, int userId, int functionId, int open) {
        this.id = id;
        this.userId = userId;
        this.functionId = functionId;
        this.open = open;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFunctionId() {
        return functionId;
    }

    public void setFunctionId(int functionId) {
        this.functionId = functionId;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    @Override
    public String toString() {
        return "UserFunction{" +
                "id=" + id +
                ", userId=" + userId +
                ", functionId=" + functionId +
                ", open=" + open +
                '}';
    }
}
