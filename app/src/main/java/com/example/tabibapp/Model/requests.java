package com.example.tabibapp.Model;

public class requests {
    private String Clinic_name;
    private String Clinic_price;
    private String Clinic_date;
    private String User_phone;
    private String Comment;
    private String status;

    public String getDoctor_phone() {
        return doctor_phone;
    }

    public void setDoctor_phone(String doctor_phone) {
        this.doctor_phone = doctor_phone;
    }

    private String doctor_phone;


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

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public requests(String clinic_name, String clinic_price, String clinic_date, String user_phone, String comment, String status, String doctor_phone) {
        Clinic_name = clinic_name;
        Clinic_price = clinic_price;
        Clinic_date = clinic_date;
        User_phone = user_phone;
        Comment = comment;
        this.status = status;
        this.doctor_phone = doctor_phone;
    }

    public requests() {
    }
}
