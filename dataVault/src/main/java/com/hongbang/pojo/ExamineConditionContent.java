package com.hongbang.pojo;

public class ExamineConditionContent {
    private int id;
    private int examineStepId;
    private int examineConditionId;
    private String content;

    public ExamineConditionContent(int id, int examineStepId, int examineConditionId, String content) {
        this.id = id;
        this.examineStepId = examineStepId;
        this.examineConditionId = examineConditionId;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExamineStepId() {
        return examineStepId;
    }

    public void setExamineStepId(int examineStepId) {
        this.examineStepId = examineStepId;
    }

    public int getExamineConditionId() {
        return examineConditionId;
    }

    public void setExamineConditionId(int examineConditionId) {
        this.examineConditionId = examineConditionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ExamineConditionContent{" +
                "id=" + id +
                ", examineStepId=" + examineStepId +
                ", examineConditionId=" + examineConditionId +
                ", content='" + content + '\'' +
                '}';
    }
}
