package com.example.tabibapp.Model;

public class reserve {
    private String Doctor_phone;
    private String Clinic_name;
    private String Clinic_price;
    private String Clinic_date;
    private String Clinic_date1;

    public String getClinic_date1() {
        return Clinic_date1;
    }

    public void setClinic_date1(String clinic_date1) {
        Clinic_date1 = clinic_date1;
    }

    private String User_phone;
    private String Comment;
    private String company;
    private String city;
    private String service;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    private String section;

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

    public reserve(String doctor_phone, String clinic_name, String clinic_price, String clinic_date, String clinic_date1, String user_phone, String comment, String company, String city, String service, String section) {
        Doctor_phone = doctor_phone;
        Clinic_name = clinic_name;
        Clinic_price = clinic_price;
        Clinic_date = clinic_date;
        Clinic_date1 = clinic_date1;
        User_phone = user_phone;
        Comment = comment;
        this.company = company;
        this.city = city;
        this.service = service;
        this.section = section;
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
