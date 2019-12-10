package com.example.tabibapp.Model;

public class reserve {
    private String Doctor_phone;
    private String Clinic_name;
    private String Clinic_price;
    private String Clinic_date;
    private String User_phone;
    private String Comment, company , city;

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public reserve(String doctor_phone, String clinic_name, String clinic_price, String clinic_date, String user_phone, String comment, String company, String city) {
        Doctor_phone = doctor_phone;
        Clinic_name = clinic_name;
        Clinic_price = clinic_price;
        Clinic_date = clinic_date;
        User_phone = user_phone;
        Comment = comment;
        this.company = company;
       this.city =city;
    }

    public String getDoctor_phone() {
        return Doctor_phone;
    }

    public void setDoctor_phone(String doctor_phone) {
        Doctor_phone = doctor_phone;
    }

    public String getClinic_name() {
        return Clinic_name;
    }

    public void setClinic_name(String clinic_name) {
        Clinic_name = clinic_name;
    }

    public String getClinic_price() {
        return Clinic_price;
    }

    public void setClinic_price(String clinic_price) {
        Clinic_price = clinic_price;
    }

    public String getClinic_date() {
        return Clinic_date;
    }

    public void setClinic_date(String clinic_date) {
        Clinic_date = clinic_date;
    }

    public String getUser_phone() {
        return User_phone;
    }

    public void setUser_phone(String user_phone) {
        User_phone = user_phone;
    }

    public reserve() {
    }
}
