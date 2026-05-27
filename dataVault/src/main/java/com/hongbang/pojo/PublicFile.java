package com.hongbang.pojo;

public class PublicFile {
    private int id;
    private int pId;
    private String name;
    private String size;
    private String type;
    public String url;

    public PublicFile(int id, int pId, String name, String size, String type, String url) {
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.size = size;
        this.type = type;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PublicFile{" +
                "id=" + id +
                ", pId=" + pId +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
