package com.hongbang.pojo;

public class User {
    private int id;
    private String userName;
    private String passWord;
    private int department;//所属部门
    private String name;
    private int age;
    private String sex;
    private int level;
    private int exitTime;
    private String secondaryPassword;//二级密码

    public User(int id, String userName, String passWord, int department, String name, int age, String sex, int level, int exitTime, String secondaryPassword) {
        this.id = id;
        this.userName = userName;
        this.passWord = passWord;
        this.department = department;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.level = level;
        this.exitTime = exitTime;
        this.secondaryPassword = secondaryPassword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExitTime() {
        return exitTime;
    }

    public void setExitTime(int exitTime) {
        this.exitTime = exitTime;
    }

    public String getSecondaryPassword() {
        return secondaryPassword;
    }

    public void setSecondaryPassword(String secondaryPassword) {
        this.secondaryPassword = secondaryPassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", department=" + department +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", level=" + level +
                ", exitTime=" + exitTime +
                ", secondaryPassword='" + secondaryPassword + '\'' +
                '}';
    }
}
