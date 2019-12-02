package com.example.tabibapp.Model;

public class doctor {
    private String name;
    private String desc;
    private String image;
    private String catid;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public doctor(String name, String desc, String image, String catid, String phone) {
        this.name = name;
        this.desc = desc;
        this.image = image;
        this.catid = catid;
        this.phone=phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public doctor() {
    }
}
