package com.hongbang.pojo;

import java.util.Date;

public class ApplicationForm {
    private int id;
    private String orderNumber;
    private String date;
    private int sort;
    private int userId;
    private int completeStatus;
    private int sortTwo;
    private String reason;
    private int circulation;
    private int circulationBoss;
    private String refuse;
    private String notes;
    private int robot;
    private int invoiceSign;

    public ApplicationForm(int id, String orderNumber, String date, int sort, int userId, int completeStatus, int sortTwo, String reason, int circulation, int circulationBoss, String refuse, String notes, int robot, int invoiceSign) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.date = date;
        this.sort = sort;
        this.userId = userId;
        this.completeStatus = completeStatus;
        this.sortTwo = sortTwo;
        this.reason = reason;
        this.circulation = circulation;
        this.circulationBoss = circulationBoss;
        this.refuse = refuse;
        this.notes = notes;
        this.robot = robot;
        this.invoiceSign = invoiceSign;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(int completeStatus) {
        this.completeStatus = completeStatus;
    }

    public int getSortTwo() {
        return sortTwo;
    }

    public void setSortTwo(int sortTwo) {
        this.sortTwo = sortTwo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getCirculation() {
        return circulation;
    }

    public void setCirculation(int circulation) {
        this.circulation = circulation;
    }

    public int getCirculationBoss() {
        return circulationBoss;
    }

    public void setCirculationBoss(int circulationBoss) {
        this.circulationBoss = circulationBoss;
    }

    public String getRefuse() {
        return refuse;
    }

    public void setRefuse(String refuse) {
        this.refuse = refuse;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getRobot() {
        return robot;
    }

    public void setRobot(int robot) {
        this.robot = robot;
    }

    public int getInvoiceSign() {
        return invoiceSign;
    }

    public void setInvoiceSign(int invoiceSign) {
        this.invoiceSign = invoiceSign;
    }

    @Override
    public String toString() {
        return "ApplicationForm{" +
                "id=" + id +
                ", orderNumber='" + orderNumber + '\'' +
                ", date='" + date + '\'' +
                ", sort=" + sort +
                ", userId=" + userId +
                ", completeStatus=" + completeStatus +
                ", sortTwo=" + sortTwo +
                ", reason='" + reason + '\'' +
                ", circulation=" + circulation +
                ", circulationBoss=" + circulationBoss +
                ", refuse='" + refuse + '\'' +
                ", notes='" + notes + '\'' +
                ", robot=" + robot +
                ", invoiceSign=" + invoiceSign +
                '}';
    }
}
