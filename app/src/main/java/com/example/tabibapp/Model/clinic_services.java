package com.example.tabibapp.Model;

public class clinic_services {
    private String name;
    private String price;

    public clinic_services(String name, String price) {
        this.name = name;
        this.price = price;
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

    public clinic_services() {
    }
}
