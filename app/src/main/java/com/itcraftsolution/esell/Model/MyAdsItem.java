package com.itcraftsolution.esell.Model;

import java.util.List;

//User Added Item Model
public class MyAdsItem {
    private int id, user_id, price, status, fav;
    private String cat_name, title, description, location, city_area, date, item_img, message, auth_id;


    //User Item Constructor
    public MyAdsItem(int id, int user_id, int price, int status, int fav, String cat_name, String title, String description, String location, String city_area, String item_img, String date, String message, String auth_id) {
        this.id = id;
        this.user_id = user_id;
        this.price = price;
        this.status = status;
        this.fav = fav;
        this.cat_name = cat_name;
        this.title = title;
        this.description = description;
        this.location = location;
        this.city_area = city_area;
        this.item_img = item_img;
        this.date = date;
        this.message = message;
        this.auth_id = auth_id;
    }

    //User Item Getter And Setter Method
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuth_id() {
        return auth_id;
    }

    public void setAuth_id(String auth_id) {
        this.auth_id = auth_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity_area() {
        return city_area;
    }

    public void setCity_area(String city_area) {
        this.city_area = city_area;
    }

    public String getItem_img() {
        return item_img;
    }

    public void setItem_img(String item_img) {
        this.item_img = item_img;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}