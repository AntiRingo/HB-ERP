package com.hongbang.pojo;

import java.math.BigDecimal;

public class ApplicationReceiveBatch {
    private Integer id;
    private Integer contentId;          // 关联主表ID
    private String batchCode;           // 批次号
    private BigDecimal receiveNum;     // 本次数量（正数到货，负数退货）
    private Integer receiveType;        // 1=到货 2=退货 3=让步接收
    private String vault;              // 仓库
    private String operateUser;        // 操作人
    private String operateTime;        // 操作时间
    private String batchRemark;        // 备注

    public ApplicationReceiveBatch(Integer id, Integer contentId, String batchCode, BigDecimal receiveNum, Integer receiveType, String vault, String operateUser, String operateTime, String batchRemark) {
        this.id = id;
        this.contentId = contentId;
        this.batchCode = batchCode;
        this.receiveNum = receiveNum;
        this.receiveType = receiveType;
        this.vault = vault;
        this.operateUser = operateUser;
        this.operateTime = operateTime;
        this.batchRemark = batchRemark;
    }

    public ApplicationReceiveBatch() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public BigDecimal getReceiveNum() {
        return receiveNum;
    }

    public void setReceiveNum(BigDecimal receiveNum) {
        this.receiveNum = receiveNum;
    }

    public Integer getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(Integer receiveType) {
        this.receiveType = receiveType;
    }

    public String getVault() {
        return vault;
    }

    public void setVault(String vault) {
        this.vault = vault;
    }

    public String getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public String getBatchRemark() {
        return batchRemark;
    }

    public void setBatchRemark(String batchRemark) {
        this.batchRemark = batchRemark;
    }

    @Override
    public String toString() {
        return "ApplicationReceiveBatch{" +
                "id=" + id +
                ", contentId=" + contentId +
                ", batchCode='" + batchCode + '\'' +
                ", receiveNum=" + receiveNum +
                ", receiveType=" + receiveType +
                ", vault='" + vault + '\'' +
                ", operateUser='" + operateUser + '\'' +
                ", operateTime='" + operateTime + '\'' +
                ", batchRemark='" + batchRemark + '\'' +
                '}';
    }
}
