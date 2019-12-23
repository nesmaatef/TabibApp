package com.example.tabibapp.Model;

public class reserve_hospital {
    private String hospital_phone;
    private String user_phone;
    private String category;
    private String doctor;
    private String date;

    public String getHospital_phone() {
        return hospital_phone;
    }

    public void setHospital_phone(String hospital_phone) {
        this.hospital_phone = hospital_phone;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public reserve_hospital(String hospital_phone, String user_phone, String category, String doctor, String date) {
        this.hospital_phone = hospital_phone;
        this.user_phone = user_phone;
        this.category = category;
        this.doctor = doctor;
        this.date = date;
    }

    public reserve_hospital() {
    }
}
