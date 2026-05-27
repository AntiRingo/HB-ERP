package com.hongbang.pojo;

public class PdfFourPointsRegions {
    private int id;
    private int excelId;
    private Double point1X;
    private Double point1Y;
    private Double point2X;
    private Double point2Y;
    private Double point3X;
    private Double point3Y;
    private Double point4X;
    private Double point4Y;

    public PdfFourPointsRegions(int id, int excelId, Double point1X, Double point1Y, Double point2X, Double point2Y, Double point3X, Double point3Y, Double point4X, Double point4Y) {
        this.id = id;
        this.excelId = excelId;
        this.point1X = point1X;
        this.point1Y = point1Y;
        this.point2X = point2X;
        this.point2Y = point2Y;
        this.point3X = point3X;
        this.point3Y = point3Y;
        this.point4X = point4X;
        this.point4Y = point4Y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExcelId() {
        return excelId;
    }

    public void setExcelId(int excelId) {
        this.excelId = excelId;
    }

    public Double getPoint1X() {
        return point1X;
    }

    public void setPoint1X(Double point1X) {
        this.point1X = point1X;
    }

    public Double getPoint1Y() {
        return point1Y;
    }

    public void setPoint1Y(Double point1Y) {
        this.point1Y = point1Y;
    }

    public Double getPoint2X() {
        return point2X;
    }

    public void setPoint2X(Double point2X) {
        this.point2X = point2X;
    }

    public Double getPoint2Y() {
        return point2Y;
    }

    public void setPoint2Y(Double point2Y) {
        this.point2Y = point2Y;
    }

    public Double getPoint3X() {
        return point3X;
    }

    public void setPoint3X(Double point3X) {
        this.point3X = point3X;
    }

    public Double getPoint3Y() {
        return point3Y;
    }

    public void setPoint3Y(Double point3Y) {
        this.point3Y = point3Y;
    }

    public Double getPoint4X() {
        return point4X;
    }

    public void setPoint4X(Double point4X) {
        this.point4X = point4X;
    }

    public Double getPoint4Y() {
        return point4Y;
    }

    public void setPoint4Y(Double point4Y) {
        this.point4Y = point4Y;
    }

    @Override
    public String toString() {
        return "pdfFourPointsRegions{" +
                "id=" + id +
                ", excelId=" + excelId +
                ", point1X=" + point1X +
                ", point1Y=" + point1Y +
                ", point2X=" + point2X +
                ", point2Y=" + point2Y +
                ", point3X=" + point3X +
                ", point3Y=" + point3Y +
                ", point4X=" + point4X +
                ", point4Y=" + point4Y +
                '}';
    }
}
