package com.hongbang.pojo;

public class Invoice {
    private int id;
    private int appId;
    private String invoiceStr;
    private int status;
    private String time;

    public Invoice(int id, int appId, String invoiceStr, int status, String time) {
        this.id = id;
        this.appId = appId;
        this.invoiceStr = invoiceStr;
        this.status = status;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getInvoiceStr() {
        return invoiceStr;
    }

    public void setInvoiceStr(String invoiceStr) {
        this.invoiceStr = invoiceStr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", appId=" + appId +
                ", invoiceStr='" + invoiceStr + '\'' +
                ", status=" + status +
                ", time='" + time + '\'' +
                '}';
    }
}
