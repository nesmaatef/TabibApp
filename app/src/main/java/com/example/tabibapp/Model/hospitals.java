package com.example.tabibapp.Model;

public class hospitals {
    private String name;
    private String map;
    private String phone ;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    private String desc;
    private String times;
    private String image;
    private String catid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public hospitals(String name, String map, String desc, String times, String image, String catid, String phone) {
        this.name = name;
        this.map = map;
        this.desc = desc;
        this.times = times;
        this.image = image;
        this.catid= catid;
        this.phone=phone;
    }

    public hospitals() {
    }
}
