package com.hongbang.pojo;

public class ExamineLog {
    private int id;
    private String date;
    private int appFormId;
    private int userId;
    private String examineStatus;

    public ExamineLog(int id, String date, int appFormId, int userId, String examineStatus) {
        this.id = id;
        this.date = date;
        this.appFormId = appFormId;
        this.userId = userId;
        this.examineStatus = examineStatus;
    }

    public ExamineLog() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAppFormId() {
        return appFormId;
    }

    public void setAppFormId(int appFormId) {
        this.appFormId = appFormId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getExamineStatus() {
        return examineStatus;
    }

    public void setExamineStatus(String examineStatus) {
        this.examineStatus = examineStatus;
    }

    @Override
    public String toString() {
        return "ExamineLog{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", appFormId=" + appFormId +
                ", userId=" + userId +
                ", examineStatus='" + examineStatus + '\'' +
                '}';
    }
}
