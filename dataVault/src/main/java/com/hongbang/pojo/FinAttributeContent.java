package com.hongbang.pojo;

public class FinAttributeContent {
    private int id;//属性内容表
    private int finAttNameId;//属于哪个属性
    private int finProductId;//属于哪个物料
    private String finAttContent;//属性内容

    public FinAttributeContent(int id, int finAttNameId, int finProductId, String finAttContent) {
        this.id = id;
        this.finAttNameId = finAttNameId;
        this.finProductId = finProductId;
        this.finAttContent = finAttContent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFinAttNameId() {
        return finAttNameId;
    }

    public void setFinAttNameId(int finAttNameId) {
        this.finAttNameId = finAttNameId;
    }

    public int getFinProductId() {
        return finProductId;
    }

    public void setFinProductId(int finProductId) {
        this.finProductId = finProductId;
    }

    public String getFinAttContent() {
        return finAttContent;
    }

    public void setFinAttContent(String finAttContent) {
        this.finAttContent = finAttContent;
    }

    @Override
    public String toString() {
        return "FinAttributeContent{" +
                "id=" + id +
                ", finAttNameId=" + finAttNameId +
                ", finProductId=" + finProductId +
                ", finAttContent='" + finAttContent + '\'' +
                '}';
    }
}
