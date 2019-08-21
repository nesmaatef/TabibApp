package com.example.tabibapp.Model;

public class users {







    private String phone;
    private String isstaff;
    private String ispatient;

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
    }
}
