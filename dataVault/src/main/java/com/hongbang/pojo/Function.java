package com.hongbang.pojo;

public class Function {
    private int id;
    private int departId;
    private String functionName;
    private int moduleId;
    private String url;
    private int reorder;

    public Function(int id, int departId, String functionName, int moduleId, String url, int reorder) {
        this.id = id;
        this.departId = departId;
        this.functionName = functionName;
        this.moduleId = moduleId;
        this.url = url;
        this.reorder = reorder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDepartId() {
        return departId;
    }

    public void setDepartId(int departId) {
        this.departId = departId;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getReorder() {
        return reorder;
    }

    public void setReorder(int reorder) {
        this.reorder = reorder;
    }

    @Override
    public String toString() {
        return "Function{" +
                "id=" + id +
                ", departId=" + departId +
                ", functionName='" + functionName + '\'' +
                ", moduleId=" + moduleId +
                ", url='" + url + '\'' +
                ", reorder=" + reorder +
                '}';
    }
}
