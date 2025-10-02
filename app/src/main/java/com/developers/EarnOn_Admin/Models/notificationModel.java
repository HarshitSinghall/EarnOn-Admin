package com.developers.EarnOn_Admin.Models;

public class notificationModel {

    String Description;
    String Image;
    String Title;
    String large_Image;
    String id;

    public notificationModel(String description, String image, String title, String large_Image, String id) {
        Description = description;
        Image = image;
        Title = title;
        this.large_Image = large_Image;
        this.id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getLarge_Image() {
        return large_Image;
    }

    public void setLarge_Image(String large_Image) {
        this.large_Image = large_Image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
