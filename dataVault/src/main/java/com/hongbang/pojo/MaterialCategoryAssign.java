package com.hongbang.pojo;

public class MaterialCategoryAssign {
    private int id;//选择分类物料审核人表
    private int categoryId;//分类ID
    private int userId;//用户ID
    private String createTime;//创建时间

    public MaterialCategoryAssign(int id, int categoryId, int userId, String createTime) {
        this.id = id;
        this.categoryId = categoryId;
        this.userId = userId;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "MaterialCategoryAssign{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", userId=" + userId +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
