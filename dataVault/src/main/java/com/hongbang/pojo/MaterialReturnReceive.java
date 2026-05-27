package com.hongbang.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class MaterialReturnReceive {
    private Integer id;
    private Integer applicationContentId;
    private Integer applicationId;
    private Integer productId;
    private Integer vault;
    private Integer handleType;
    private BigDecimal returnQty;
    private Integer receiveUser;
    private Integer continuePurchase;
    private String handleRemark;
    private Date handleTime;
    private BigDecimal returnQtyed;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplicationContentId() {
        return applicationContentId;
    }

    public void setApplicationContentId(Integer applicationContentId) {
        this.applicationContentId = applicationContentId;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getVault() {
        return vault;
    }

    public void setVault(Integer vault) {
        this.vault = vault;
    }

    public Integer getHandleType() {
        return handleType;
    }

    public void setHandleType(Integer handleType) {
        this.handleType = handleType;
    }

    public BigDecimal getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(BigDecimal returnQty) {
        this.returnQty = returnQty;
    }

    public Integer getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(Integer receiveUser) {
        this.receiveUser = receiveUser;
    }

    public Integer getContinuePurchase() {
        return continuePurchase;
    }

    public void setContinuePurchase(Integer continuePurchase) {
        this.continuePurchase = continuePurchase;
    }

    public String getHandleRemark() {
        return handleRemark;
    }

    public void setHandleRemark(String handleRemark) {
        this.handleRemark = handleRemark;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public BigDecimal getReturnQtyed() {
        return returnQtyed;
    }

    public void setReturnQtyed(BigDecimal returnQtyed) {
        this.returnQtyed = returnQtyed;
    }


    @Override
    public String toString() {
        return "MaterialReturnReceive{" +
                "id=" + id +
                ", applicationContentId=" + applicationContentId +
                ", applicationId=" + applicationId +
                ", productId=" + productId +
                ", vault=" + vault +
                ", handleType=" + handleType +
                ", returnQty=" + returnQty +
                ", receiveUser=" + receiveUser +
                ", continuePurchase=" + continuePurchase +
                ", handleRemark='" + handleRemark + '\'' +
                ", handleTime=" + handleTime +
                ", returnQtyed=" + returnQtyed +
                '}';
    }
}
