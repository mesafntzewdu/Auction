package com.mesi.auction.dao;

public class userDAO {

    private  String user_name;
    private String father_name;
    private String email;
    private String phone;

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    private String imgPath;

    public userDAO(String user_name, String father_name, String email, String phone, String imgPath) {
        this.user_name = user_name;
        this.father_name = father_name;
        this.email = email;
        this.phone = phone;
        this.imgPath = imgPath;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
