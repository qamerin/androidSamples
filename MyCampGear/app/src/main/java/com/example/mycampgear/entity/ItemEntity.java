package com.example.mycampgear.entity;

import android.graphics.Bitmap;

public class ItemEntity {
    private int itemId = 0;
    boolean checkd = false;
    private String category = null;
    private String description = null;
    private String brand = null;
    private String itemName = null;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    private Bitmap image = null;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public boolean isCheckd() {
        return checkd;
    }

    public void setCheckd(boolean checkd) {
        this.checkd = checkd;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
