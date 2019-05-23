package com.example.tabibapp.Model;

public class users {

    private String password ;
    private String name;

    public users(String name, String password, String age) {
        this.password = password;
        this.name = name;
        this.age = age;
        this.isstaff="false";

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String phone;
    private String isstaff;
    private String age;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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


    public users() {
    }
}
