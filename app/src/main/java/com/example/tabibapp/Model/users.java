package com.example.tabibapp.Model;

public class users {
    private String phone;
    private String isstaff;
    private String ispatient;
    private String city,area,company;

    public users() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getIshospital() {
        return ishospital;
    }

    public void setIshospital(String ishospital) {
        this.ishospital = ishospital;
    }

    private String ishospital;
    private String ishospital1;

    public String getIshospital1() {
        return ishospital1;
    }

    public void setIshospital1(String ishospital1) {
        this.ishospital1 = ishospital1;
    }

    public String getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(String isadmin) {
        this.isadmin = isadmin;
    }

    private String isadmin;






    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIsstaff() {
        return isstaff;
    }

    public users(String phone, String isstaff, String ispatient, String city, String area, String company, String ishospital, String ishospital1, String isadmin) {
        this.phone = phone;
        this.isstaff = isstaff;
        this.ispatient = ispatient;
        this.city = city;
        this.area = area;
        this.company = company;
        this.ishospital = ishospital;
        this.ishospital1 = ishospital1;
        this.isadmin = isadmin;
    }

    public void setIsstaff(String isstaff) {
        this.isstaff = isstaff;
    }


    public String getIspatient() {
        return ispatient;
    }

    public void setIspatient(String ispatient) {
        this.ispatient = ispatient;
    }


}
