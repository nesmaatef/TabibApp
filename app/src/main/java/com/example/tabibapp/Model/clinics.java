package com.example.tabibapp.Model;

public class clinics {
    private String docid;
    private String image;
    private String map;
    private String name;
    private String price;

    public String getTimeswait() {
        return timeswait;
    }

    public void setTimeswait(String timeswait) {
        this.timeswait = timeswait;
    }

    private String timeswait;

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }




    public clinics(String docid, String image, String map, String name, String price, String timeswait) {
        this.docid = docid;
        this.image = image;
        this.map = map;
        this.name = name;
        this.price = price;
        this.timeswait = timeswait;
    }

    public clinics() {
    }
}
