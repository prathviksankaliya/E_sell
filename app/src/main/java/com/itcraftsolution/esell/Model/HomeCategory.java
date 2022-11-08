package com.itcraftsolution.esell.Model;

// Home Category Model
public class HomeCategory {
    private int id, status;
    private String cat_name, cat_img, date, message;

    //Home Category Constructor
    public HomeCategory(int id, int status, String cat_name, String cat_img, String date, String message) {
        this.id = id;
        this.status = status;
        this.cat_name = cat_name;
        this.cat_img = cat_img;
        this.date = date;
        this.message = message;
    }

    // Home Category Getter and Setter Method
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_img() {
        return cat_img;
    }

    public void setCat_img(String cat_img) {
        this.cat_img = cat_img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
