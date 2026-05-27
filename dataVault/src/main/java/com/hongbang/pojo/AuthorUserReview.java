package com.hongbang.pojo;

public class AuthorUserReview {
    private int id;
    private String times;
    private int applicant;
    private int status;//审核状态

    public AuthorUserReview(int id, String times, int applicant, int status) {
        this.id = id;
        this.times = times;
        this.applicant = applicant;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public int getApplicant() {
        return applicant;
    }

    public void setApplicant(int applicant) {
        this.applicant = applicant;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AuthorUserReview{" +
                "id=" + id +
                ", times='" + times + '\'' +
                ", applicant=" + applicant +
                ", status=" + status +
                '}';
    }
}


