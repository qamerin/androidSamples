package com.example.mycampgear;

import android.widget.CheckBox;

public class AddItemList {

    public boolean isCheckd() {
        return checkd;
    }

    public void setCheckd(boolean checkd) {
        this.checkd = checkd;
    }

    boolean checkd = false;

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

    private String category = null;
    private String description = null;

}
