package com.example.tabibapp.Model;

public class reserve {
    private String Doctor_id;
    private String Doctor_name;
    private String Doctor_price;
    private String Doctor_date;

    public String getDoctor_id() {
        return Doctor_id;
    }

    public void setDoctor_id(String Doctor_id) {
        this.Doctor_id = Doctor_id;
    }

    public String getDoctor_name() {
        return Doctor_name;
    }

    public void setDoctor_name(String Doctor_name) {
        this.Doctor_name = Doctor_name;
    }

    public String getDoctor_price() {
        return Doctor_price;
    }

    public void setDoctor_price(String Doctor_price) {
        this.Doctor_price = Doctor_price;
    }

    public String getDoctor_date() {
        return Doctor_date;
    }

    public void setDoctor_date(String Doctor_date) {
        this.Doctor_date = Doctor_date;
    }

    public reserve(String Doctor_id, String Doctor_name, String Doctor_price, String Doctor_date) {
        this.Doctor_id = Doctor_id;
        this.Doctor_name = Doctor_name;
        this.Doctor_price = Doctor_price;
        this.Doctor_date = Doctor_date;
    }

    public reserve() {
    }
}
