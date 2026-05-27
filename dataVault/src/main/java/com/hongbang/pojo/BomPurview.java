package com.hongbang.pojo;

public class BomPurview {
    private int id;
    private int finBomTitleId;
    private int userPurview;//读
    private int canEdit;//写
    private int deleteSign;//删除标志
    private int reviewAppId;//权限变更申请单Id

    public BomPurview(int id, int finBomTitleId, int userPurview, int canEdit, int deleteSign, int reviewAppId) {
        this.id = id;
        this.finBomTitleId = finBomTitleId;
        this.userPurview = userPurview;
        this.canEdit = canEdit;
        this.deleteSign = deleteSign;
        this.reviewAppId = reviewAppId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFinBomTitleId() {
        return finBomTitleId;
    }

    public void setFinBomTitleId(int finBomTitleId) {
        this.finBomTitleId = finBomTitleId;
    }

    public int getUserPurview() {
        return userPurview;
    }

    public void setUserPurview(int userPurview) {
        this.userPurview = userPurview;
    }

    public int getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(int canEdit) {
        this.canEdit = canEdit;
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

    @Override
    public String toString() {
        return "BomPurview{" +
                "id=" + id +
                ", finBomTitleId=" + finBomTitleId +
                ", userPurview=" + userPurview +
                ", canEdit=" + canEdit +
                ", deleteSign=" + deleteSign +
                ", reviewAppId=" + reviewAppId +
                '}';
    }
}
