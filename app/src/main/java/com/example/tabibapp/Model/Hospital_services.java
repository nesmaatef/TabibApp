package com.example.tabibapp.Model;

public class Hospital_services {
    private String name;
    private String hospitalid ;

    public String getHospitalid() {
        return hospitalid;
    }

    public void setHospitalid(String hospitalid) {
        this.hospitalid = hospitalid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Hospital_services(String name, String hospitalid) {
        this.name = name;
        this.hospitalid = hospitalid;
    }

    public Hospital_services() {
    }
}
