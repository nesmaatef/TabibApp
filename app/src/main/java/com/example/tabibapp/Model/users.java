package com.example.tabibapp.Model;

public class users {
    private String phone;
    private String isstaff;
    private String ispatient;
    private String city,area,company;

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



    public void setIsstaff(String isstaff) {
        this.isstaff = isstaff;
    }


    public String getIspatient() {
        return ispatient;
    }

    public void setIspatient(String ispatient) {
        this.ispatient = ispatient;
    }

    public users() {
        this.isstaff="false";
        this.ispatient="false";
        this.city="false";
        this.area="false";
        this.company="false";
    }
}
