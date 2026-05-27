package com.hongbang.pojo;

public class FinBomTitle {
    private  int id;
    private  int finProductId;
    private String title;
    private String description;
    private Integer author;

    public FinBomTitle(int id, int finProductId, String title, String description, Integer author) {
        this.id = id;
        this.finProductId = finProductId;
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFinProductId() {
        return finProductId;
    }

    public void setFinProductId(int finProductId) {
        this.finProductId = finProductId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "FinBomTitle{" +
                "id=" + id +
                ", finProductId=" + finProductId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", author=" + author +
                '}';
    }
}
