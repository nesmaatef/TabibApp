package com.example.tabibapp.Model;

public class fav {
    private String doctorid, doctorname, doctordescription,doctorimage,userphone, doctorcatid;

    public String getDoctorid() {
        return doctorid;
    }

    public void setDoctorid(String doctorid) {
        this.doctorid = doctorid;
    }

    public String getDoctorname() {
        return doctorname;
    }

    public void setDoctorname(String doctorname) {
        this.doctorname = doctorname;
    }

    public String getDoctordescription() {
        return doctordescription;
    }

    public void setDoctordescription(String doctordescription) {
        this.doctordescription = doctordescription;
    }

    public String getDoctorimage() {
        return doctorimage;
    }

    public void setDoctorimage(String doctorimage) {
        this.doctorimage = doctorimage;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getDoctorcatid() {
        return doctorcatid;
    }

    public void setDoctorcatid(String doctorcatid) {
        this.doctorcatid = doctorcatid;
    }

    public fav(String doctorid, String doctorname, String doctordescription, String doctorimage, String userphone, String doctorcatid) {
        this.doctorid = doctorid;
        this.doctorname = doctorname;
        this.doctordescription = doctordescription;
        this.doctorimage = doctorimage;
        this.userphone = userphone;
        this.doctorcatid = doctorcatid;
    }

    public fav() {
    }
}
