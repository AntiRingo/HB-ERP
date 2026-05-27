package com.hongbang.pojo;

public class BomPurviewReview {

//    权限变更申请单
    private int id;
    private String times;
    private int applicant;//申请人id
    private int bomId;//变更的bom表id
    private int status;

    public BomPurviewReview(int id, String times, int applicant, int bomId, int status) {
        this.id = id;
        this.times = times;
        this.applicant = applicant;
        this.bomId = bomId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String gettimes() {
        return times;
    }

    public void settimes(String times) {
        this.times = times;
    }

    public int getApplicant() {
        return applicant;
    }

    public void setApplicant(int applicant) {
        this.applicant = applicant;
    }

    public int getBomId() {
        return bomId;
    }

    public void setBomId(int bomId) {
        this.bomId = bomId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BomPurviewReview{" +
                "id=" + id +
                ", times='" + times + '\'' +
                ", applicant=" + applicant +
                ", bomId=" + bomId +
                ", status=" + status +
                '}';
    }
}
