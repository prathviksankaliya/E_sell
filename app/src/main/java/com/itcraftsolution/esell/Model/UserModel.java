package com.itcraftsolution.esell.Model;

public class UserModel {
    private int id, status;
    private String phone, email, user_img, user_name, user_bio, date, location, city_area, message, auth_id;

    public UserModel(int id, String phone, String email, String user_img, String user_name, String user_bio, String date, String location, String city_area, String message, int status, String auth_id) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.user_img = user_img;
        this.user_name = user_name;
        this.user_bio = user_bio;
        this.date = date;
        this.location = location;
        this.city_area = city_area;
        this.message = message;
        this.status = status;
        this.auth_id = auth_id;
    }

    public UserModel(int id, int status, String phone, String email, String user_img, String user_name, String user_bio, String date, String location, String city_area) {
        this.id = id;
        this.status = status;
        this.phone = phone;
        this.email = email;
        this.user_img = user_img;
        this.user_name = user_name;
        this.user_bio = user_bio;
        this.date = date;
        this.location = location;
        this.city_area = city_area;
    }

    public UserModel() {
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_bio() {
        return user_bio;
    }

    public void setUser_bio(String user_bio) {
        this.user_bio = user_bio;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
