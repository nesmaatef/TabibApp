package com.example.tabibapp.Model;

public class category {
   private String name;
  private String Image;
  private String hospital_id;

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getName() {
      return name;
  }

   public void setName(String name) {
     this.name = name;
  }

  public String getImage() {
    return Image;
  }

  public void setImage(String image) {
    this.Image = image;
  }

  public category(String image, String name, String hospital_id) {
    this.name = name;
    Image = image;
    this.hospital_id =hospital_id;
  }

  public category() {
  }
}
