package com.hongbang.pojo;

public class PdfExcel {
    private int id;//上传的图纸和坐标
    private int bomTitleId;
    private String name;
    private String type;
    private String time;
    private int user;//添加人
    private String url;//文件储存路径
    private double scale;//高亮的缩放大小
    private double originCanvasX;//原点x坐标
    private double originCanvasY;//原点Y坐标
    private double pdfScale;//pdf放大缩小倍数
    private int rotation;//旋转角度
    private boolean flippedX;//水平翻转
    private boolean flippedY;//垂直翻转
    private int paired;//是否已经配对
    private int fileId;//已经配对的id

    public PdfExcel(int id, int bomTitleId, String name, String type, String time, int user, String url, double scale, double originCanvasX, double originCanvasY, double pdfScale, int rotation, boolean flippedX, boolean flippedY, int paired, int fileId) {
        this.id = id;
        this.bomTitleId = bomTitleId;
        this.name = name;
        this.type = type;
        this.time = time;
        this.user = user;
        this.url = url;
        this.scale = scale;
        this.originCanvasX = originCanvasX;
        this.originCanvasY = originCanvasY;
        this.pdfScale = pdfScale;
        this.rotation = rotation;
        this.flippedX = flippedX;
        this.flippedY = flippedY;
        this.paired = paired;
        this.fileId = fileId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBomTitleId() {
        return bomTitleId;
    }

    public void setBomTitleId(int bomTitleId) {
        this.bomTitleId = bomTitleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getOriginCanvasX() {
        return originCanvasX;
    }

    public void setOriginCanvasX(double originCanvasX) {
        this.originCanvasX = originCanvasX;
    }

    public double getOriginCanvasY() {
        return originCanvasY;
    }

    public void setOriginCanvasY(double originCanvasY) {
        this.originCanvasY = originCanvasY;
    }

    public double getPdfScale() {
        return pdfScale;
    }

    public void setPdfScale(double pdfScale) {
        this.pdfScale = pdfScale;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public boolean isFlippedX() {
        return flippedX;
    }

    public void setFlippedX(boolean flippedX) {
        this.flippedX = flippedX;
    }

    public boolean isFlippedY() {
        return flippedY;
    }

    public void setFlippedY(boolean flippedY) {
        this.flippedY = flippedY;
    }

    public int getPaired() {
        return paired;
    }

    public void setPaired(int paired) {
        this.paired = paired;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    @Override
    public String toString() {
        return "PdfExcel{" +
                "id=" + id +
                ", bomTitleId=" + bomTitleId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", time='" + time + '\'' +
                ", user=" + user +
                ", url='" + url + '\'' +
                ", scale=" + scale +
                ", originCanvasX=" + originCanvasX +
                ", originCanvasY=" + originCanvasY +
                ", pdfScale=" + pdfScale +
                ", rotation=" + rotation +
                ", flippedX=" + flippedX +
                ", flippedY=" + flippedY +
                ", paired=" + paired +
                ", fileId=" + fileId +
                '}';
    }
}
