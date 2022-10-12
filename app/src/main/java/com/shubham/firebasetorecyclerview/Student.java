package com.shubham.firebasetorecyclerview;

public class Student {
    String Branch,Name,USN,email,picUrl;

    public Student() {
    }

    public Student(String branch, String name, String USN, String email, String picUrl) {
        Branch = branch;
        Name = name;
        this.USN = USN;
        this.email = email;
        this.picUrl = picUrl;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUSN() {
        return USN;
    }

    public void setUSN(String USN) {
        this.USN = USN;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
