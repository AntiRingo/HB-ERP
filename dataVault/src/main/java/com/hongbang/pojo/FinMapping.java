package com.hongbang.pojo;

public class FinMapping {
    private int id;//产品仓库映射
    private int finAttNameId;//属性名id
    private int finSortId;//属于哪个分类下的映射
    private int beginLocation;
    private int endLocation;
    private int length;

    public FinMapping(int id, int finAttNameId, int finSortId, int beginLocation, int endLocation, int length) {
        this.id = id;
        this.finAttNameId = finAttNameId;
        this.finSortId = finSortId;
        this.beginLocation = beginLocation;
        this.endLocation = endLocation;
        this.length = length;
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

    public int getFinSortId() {
        return finSortId;
    }

    public void setFinSortId(int finSortId) {
        this.finSortId = finSortId;
    }

    public int getBeginLocation() {
        return beginLocation;
    }

    public void setBeginLocation(int beginLocation) {
        this.beginLocation = beginLocation;
    }

    public int getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(int endLocation) {
        this.endLocation = endLocation;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }


    @Override
    public String toString() {
        return "FinMapping{" +
                "id=" + id +
                ", finAttNameId=" + finAttNameId +
                ", finSortId=" + finSortId +
                ", beginLocation=" + beginLocation +
                ", endLocation=" + endLocation +
                ", length=" + length +
                '}';
    }
}
