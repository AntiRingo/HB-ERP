package com.hongbang.pojo;

import java.util.List;

public class ApprovalRequests {
    private int id;
    private String timestamp;
    private String applicant;
    private String department;
    private int status;
    private List<Members> members;

    public ApprovalRequests(int id, String timestamp, String applicant, String department, int status, List<Members> members) {
        this.id = id;
        this.timestamp = timestamp;
        this.applicant = applicant;
        this.department = department;
        this.status = status;
        this.members = members;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Members> getMembers() {
        return members;
    }

    public void setMembers(List<Members> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "ApprovalRequests{" +
                "id=" + id +
                ", timestamp='" + timestamp + '\'' +
                ", applicant='" + applicant + '\'' +
                ", department='" + department + '\'' +
                ", status=" + status +
                ", members=" + members +
                '}';
    }
}
