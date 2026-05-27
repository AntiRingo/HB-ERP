package com.hongbang.pojo;

public class FinApplicationForm {
    private int id;//产品仓库申请表id
    private String finOrderNumber;//申请单单号
    private String finAppDate;//申请时间
    private int userId;//申请人
    private String finReason;//申请原因（说明）
    private int finSort;//申请类型（出库入库）

    public FinApplicationForm(int id, String finOrderNumber, String finAppDate, int userId, String finReason, int finSort) {
        this.id = id;
        this.finOrderNumber = finOrderNumber;
        this.finAppDate = finAppDate;
        this.userId = userId;
        this.finReason = finReason;
        this.finSort = finSort;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFinOrderNumber() {
        return finOrderNumber;
    }

    public void setFinOrderNumber(String finOrderNumber) {
        this.finOrderNumber = finOrderNumber;
    }

    public String getFinAppDate() {
        return finAppDate;
    }

    public void setFinAppDate(String finAppDate) {
        this.finAppDate = finAppDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFinReason() {
        return finReason;
    }

    public void setFinReason(String finReason) {
        this.finReason = finReason;
    }

    public int getFinSort() {
        return finSort;
    }

    public void setFinSort(int finSort) {
        this.finSort = finSort;
    }

    @Override
    public String toString() {
        return "FinApplicationForm{" +
                "id=" + id +
                ", finOrderNumber='" + finOrderNumber + '\'' +
                ", finAppDate='" + finAppDate + '\'' +
                ", userId=" + userId +
                ", finReason='" + finReason + '\'' +
                ", finSort=" + finSort +
                '}';
    }
}
