package com.hongbang.pojo;

public class Mapping {
    private int id;
    private int attNameId;
    private int sortId;
    private int beginLocation;
    private int endLocation;
    private int length;

    public Mapping(int id, int attNameId, int sortId, int beginLocation, int endLocation, int length) {
        this.id = id;
        this.attNameId = attNameId;
        this.sortId = sortId;
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

    public int getAttNameId() {
        return attNameId;
    }

    public void setAttNameId(int attNameId) {
        this.attNameId = attNameId;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
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
        return "Mapping{" +
                "id=" + id +
                ", attNameId=" + attNameId +
                ", sortId=" + sortId +
                ", beginLocation=" + beginLocation +
                ", endLocation=" + endLocation +
                ", length=" + length +
                '}';
    }
}
