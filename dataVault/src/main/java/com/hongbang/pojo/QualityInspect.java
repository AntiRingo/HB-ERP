package com.hongbang.pojo;

import java.util.Date;

public class QualityInspect {
    private Integer id;
    private Integer applicationContentId;
    private Integer applicationId;
    private Integer productId;
    private Integer vault;
    private Integer inspectResult;
    private String inspectRemark;
    private Date inspectTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getApplicationContentId() { return applicationContentId; }
    public void setApplicationContentId(Integer applicationContentId) { this.applicationContentId = applicationContentId; }
    public Integer getApplicationId() { return applicationId; }
    public void setApplicationId(Integer applicationId) { this.applicationId = applicationId; }
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public Integer getVault() { return vault; }
    public void setVault(Integer vault) { this.vault = vault; }
    public Integer getInspectResult() { return inspectResult; }
    public void setInspectResult(Integer inspectResult) { this.inspectResult = inspectResult; }
    public String getInspectRemark() { return inspectRemark; }
    public void setInspectRemark(String inspectRemark) { this.inspectRemark = inspectRemark; }
    public Date getInspectTime() { return inspectTime; }
    public void setInspectTime(Date inspectTime) { this.inspectTime = inspectTime; }

    @Override
    public String toString() {
        return "QualityInspect{" +
                "id=" + id +
                ", applicationContentId=" + applicationContentId +
                ", applicationId=" + applicationId +
                ", productId=" + productId +
                ", vault=" + vault +
                ", inspectResult=" + inspectResult +
                ", inspectRemark='" + inspectRemark + '\'' +
                ", inspectTime=" + inspectTime +
                '}';
    }
}