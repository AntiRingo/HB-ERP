package com.hongbang.pojo;

public class AuthorUser {
    private int id;//作者管理的人员
    private int authorId;//作者id
    private int userId;//作者管理的人的id
    private int deleteSign;//删除标志（0最新(正在使用的)，1删除，2未审核）
    private int reviewAppId;//审核单id
    private int type;//申请更改管理员用户类型：0删除；1增加
    private int canEdit;//修改权限:0没有权限；1有权限

    public AuthorUser(int id, int authorId, int userId, int deleteSign, int reviewAppId, int type, int canEdit) {
        this.id = id;
        this.authorId = authorId;
        this.userId = userId;
        this.deleteSign = deleteSign;
        this.reviewAppId = reviewAppId;
        this.type = type;
        this.canEdit = canEdit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDeleteSign() {
        return deleteSign;
    }

    public void setDeleteSign(int deleteSign) {
        this.deleteSign = deleteSign;
    }

    public int getReviewAppId() {
        return reviewAppId;
    }

    public void setReviewAppId(int reviewAppId) {
        this.reviewAppId = reviewAppId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(int canEdit) {
        this.canEdit = canEdit;
    }

    @Override
    public String toString() {
        return "AuthorUser{" +
                "id=" + id +
                ", authorId=" + authorId +
                ", userId=" + userId +
                ", deleteSign=" + deleteSign +
                ", reviewAppId=" + reviewAppId +
                ", type=" + type +
                ", canEdit=" + canEdit +
                '}';
    }
}
