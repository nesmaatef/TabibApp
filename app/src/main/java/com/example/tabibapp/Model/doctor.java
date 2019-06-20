package com.example.tabibapp.Model;

public class doctor {
    private String name;
    private String desc;
    private String map;
    private String image;
    private String price;
    private String catid;

    public doctor(String name, String desc, String map, String image, String price, String catid) {
        this.name = name;
        this.desc = desc;
        this.map = map;
        this.image = image;
        this.price = price;
        this.catid = catid;
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

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
