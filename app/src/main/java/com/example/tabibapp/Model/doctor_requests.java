package com.example.tabibapp.Model;

public class doctor_requests {

    private String name, desc,phone,image;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public doctor_requests(String name, String desc, String phone, String image) {
        this.name = name;
        this.desc = desc;
        this.phone = phone;
        this.image = image;
    }

    public doctor_requests() {
    }
}
