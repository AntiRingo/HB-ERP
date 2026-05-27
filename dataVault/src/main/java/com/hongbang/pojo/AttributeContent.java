package com.hongbang.pojo;

public class AttributeContent {
    private int id;
    private int parentId;
    private int productId;
    private String content;

    public AttributeContent(int id, int parentId, int productId, String content) {
        this.id = id;
        this.parentId = parentId;
        this.productId = productId;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "AttributeContent{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", productId=" + productId +
                ", content='" + content + '\'' +
                '}';
    }
}
