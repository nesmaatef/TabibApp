package com.example.tabibapp.Model;

public class category {
   // private String name;
    private String Image;

   // public String getName() {
    //    return name;
    //}

   // public void setName(String name) {
     //   this.name = name;
    //}

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public category(String image) {
        //this.name = name;
        Image = image;
    }

    public category() {
    }
}
