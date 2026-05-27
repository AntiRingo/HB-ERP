package com.hongbang.pojo;

public class PublicNotice {
    private int id;
    private String title;
    private String date;
    private String department;
    private String priority;
    private String content;

    public PublicNotice(int id, String title, String date, String department, String priority, String content) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.department = department;
        this.priority = priority;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PublicNotice{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", department='" + department + '\'' +
                ", priority='" + priority + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
