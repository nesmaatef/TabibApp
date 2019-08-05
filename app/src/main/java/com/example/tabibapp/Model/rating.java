package com.example.tabibapp.Model;

public class rating {
    private String userphone;
    private String doctorid;
    private String ratevalue;
    private String comment;

    public rating(String userphone, String doctorid, String ratevalue, String comment) {
        this.userphone = userphone;
        this.doctorid = doctorid;
        this.ratevalue = ratevalue;
        this.comment = comment;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getDoctorid() {
        return doctorid;
    }

    public void setDoctorid(String doctorid) {
        this.doctorid = doctorid;
    }

    public String getRatevalue() {
        return ratevalue;
    }

    public void setRatevalue(String ratevalue) {
        this.ratevalue = ratevalue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public rating() {
    }
}
