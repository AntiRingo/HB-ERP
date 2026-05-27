package com.hongbang.pojo;

import java.util.Date;
/**
 * 到货记录表 实体类
 * 对应表字段：你给的表结构
 */
public class ProcurementOperations {
    private Long id;                // 操作ID
    private Integer contentId;       // 申请单明细ID（关联application_content.id）
    private Integer operatorId;      // 操作人ID
    private String operationType;   // 操作类型：procurement_start,arrival,complete
    private Date operationTime;     // 操作时间
    private Double batchQuantity;   // 本次到货数量（你要求用double）
    private String notes;           // 备注
    private Date createdAt;         // 创建时间

    // 无参构造
    public void ArrivalRecord() {}

    // getter & setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public Double getBatchQuantity() {
        return batchQuantity;
    }

    public void setBatchQuantity(Double batchQuantity) {
        this.batchQuantity = batchQuantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ProcurementOperations{" +
                "id=" + id +
                ", contentId=" + contentId +
                ", operatorId=" + operatorId +
                ", operationType='" + operationType + '\'' +
                ", operationTime=" + operationTime +
                ", batchQuantity=" + batchQuantity +
                ", notes='" + notes + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
