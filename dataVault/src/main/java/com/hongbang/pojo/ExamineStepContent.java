package com.hongbang.pojo;

public class ExamineStepContent {
    private int id;
    private int appFormId;
    private int examineStepId;
    private int result;

    public ExamineStepContent(int id, int appFormId, int examineStepId, int result) {
        this.id = id;
        this.appFormId = appFormId;
        this.examineStepId = examineStepId;
        this.result = result;
    }

    public ExamineStepContent() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppFormId() {
        return appFormId;
    }

    public void setAppFormId(int appFormId) {
        this.appFormId = appFormId;
    }

    public int getExamineStepId() {
        return examineStepId;
    }

    public void setExamineStepId(int examineStepId) {
        this.examineStepId = examineStepId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ExamineStepContent{" +
                "id=" + id +
                ", appFormId=" + appFormId +
                ", examineStepId=" + examineStepId +
                ", result=" + result +
                '}';
    }
}
