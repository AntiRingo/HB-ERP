package com.hongbang.pojo;

public class ApplicationDetail {
//    采购申请单的属性信息
    private Integer id;
    private Integer applicationContentId;

    private Double taxPrice;
    private Double goodsAmount;
    private Double freight;

    private String usePurpose;
    private String orderNo;
    private String expectInfo;

    private String payDate;
    private String arriveDate;
    private String invoiceDate;
    private String warrantyDate;

    private String supplierName;
    private String supplierAddress;
    private String contactPerson;
    private String contactTel;
    private String fax;
    private String taxNo;
    private String email;

    public ApplicationDetail(Integer id, Integer applicationContentId, Double taxPrice, Double goodsAmount, Double freight, String usePurpose, String orderNo, String expectInfo, String payDate, String arriveDate, String invoiceDate, String warrantyDate, String supplierName, String supplierAddress, String contactPerson, String contactTel, String fax, String taxNo, String email) {
        this.id = id;
        this.applicationContentId = applicationContentId;
        this.taxPrice = taxPrice;
        this.goodsAmount = goodsAmount;
        this.freight = freight;
        this.usePurpose = usePurpose;
        this.orderNo = orderNo;
        this.expectInfo = expectInfo;
        this.payDate = payDate;
        this.arriveDate = arriveDate;
        this.invoiceDate = invoiceDate;
        this.warrantyDate = warrantyDate;
        this.supplierName = supplierName;
        this.supplierAddress = supplierAddress;
        this.contactPerson = contactPerson;
        this.contactTel = contactTel;
        this.fax = fax;
        this.taxNo = taxNo;
        this.email = email;
    }

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

    public Double getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(Double taxPrice) {
        this.taxPrice = taxPrice;
    }

    public Double getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(Double goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public Double getFreight() {
        return freight;
    }

    public void setFreight(Double freight) {
        this.freight = freight;
    }

    public String getUsePurpose() {
        return usePurpose;
    }

    public void setUsePurpose(String usePurpose) {
        this.usePurpose = usePurpose;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getExpectInfo() {
        return expectInfo;
    }

    public void setExpectInfo(String expectInfo) {
        this.expectInfo = expectInfo;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(String arriveDate) {
        this.arriveDate = arriveDate;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getWarrantyDate() {
        return warrantyDate;
    }

    public void setWarrantyDate(String warrantyDate) {
        this.warrantyDate = warrantyDate;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ApplicationDetail{" +
                "id=" + id +
                ", applicationContentId=" + applicationContentId +
                ", taxPrice=" + taxPrice +
                ", goodsAmount=" + goodsAmount +
                ", freight=" + freight +
                ", usePurpose='" + usePurpose + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", expectInfo='" + expectInfo + '\'' +
                ", payDate='" + payDate + '\'' +
                ", arriveDate='" + arriveDate + '\'' +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", warrantyDate='" + warrantyDate + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", supplierAddress='" + supplierAddress + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", contactTel='" + contactTel + '\'' +
                ", fax='" + fax + '\'' +
                ", taxNo='" + taxNo + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
