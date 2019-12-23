package com.example.tabibapp.Model;

public class reserve_doctor {

    private String hospital_phone;
    private String doctor_phone;
    private String service ;
    private String room;
    private String date;

    public String getHospital_phone() {
        return hospital_phone;
    }

    public void setHospital_phone(String hospital_phone) {
        this.hospital_phone = hospital_phone;
    }

    public String getDoctor_phone() {
        return doctor_phone;
    }

    public void setDoctor_phone(String doctor_phone) {
        this.doctor_phone = doctor_phone;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public reserve_doctor(String hospital_phone, String doctor_phone, String service, String room, String date) {
        this.hospital_phone = hospital_phone;
        this.doctor_phone = doctor_phone;
        this.service = service;
        this.room = room;
        this.date = date;
    }

    public reserve_doctor() {
    }
}
