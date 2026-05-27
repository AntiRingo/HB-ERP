package com.hongbang.pojo;

public class FinAttributeName {
    private int id;//产品仓库分类的属性名称
    private int finSortId;//属于哪个分类
    private String finAttName;//属性名
    private String finAttUnit;//属性单位
    private Integer finAttLength;
    private int finAttPublic;//记录是否为公共属性

    public FinAttributeName(int id, int finSortId, String finAttName, String finAttUnit, Integer finAttLength, int finAttPublic) {
        this.id = id;
        this.finSortId = finSortId;
        this.finAttName = finAttName;
        this.finAttUnit = finAttUnit;
        this.finAttLength = finAttLength;
        this.finAttPublic = finAttPublic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFinSortId() {
        return finSortId;
    }

    public void setFinSortId(int finSortId) {
        this.finSortId = finSortId;
    }

    public String getFinAttName() {
        return finAttName;
    }

    public void setFinAttName(String finAttName) {
        this.finAttName = finAttName;
    }

    public String getFinAttUnit() {
        return finAttUnit;
    }

    public void setFinAttUnit(String finAttUnit) {
        this.finAttUnit = finAttUnit;
    }

    public Integer getFinAttLength() {
        return finAttLength;
    }

    public void setFinAttLength(Integer finAttLength) {
        this.finAttLength = finAttLength;
    }

    public int getFinAttPublic() {
        return finAttPublic;
    }

    public void setFinAttPublic(int finAttPublic) {
        this.finAttPublic = finAttPublic;
    }

    @Override
    public String toString() {
        return "FinAttributeName{" +
                "id=" + id +
                ", finSortId=" + finSortId +
                ", finAttName='" + finAttName + '\'' +
                ", finAttUnit='" + finAttUnit + '\'' +
                ", finAttLength=" + finAttLength +
                ", finAttPublic=" + finAttPublic +
                '}';
    }
}
