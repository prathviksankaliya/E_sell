package com.itcraftsolution.esell.Model;

//Chat Model Class
public class ChatModel {
    private int id, status;
    private String user_name, user_img, item_location, item_title, receiver_id, date, message;

    // Chat Model Constructor
    public ChatModel(int id, int status, String user_name, String user_img, String item_location, String item_title, String receiver_id, String date, String message) {
        this.id = id;
        this.status = status;
        this.user_name = user_name;
        this.user_img = user_img;
        this.item_location = item_location;
        this.item_title = item_title;
        this.receiver_id = receiver_id;
        this.date = date;
        this.message = message;
    }

    // Chat Model Class Getter Setter Method
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getItem_location() {
        return item_location;
    }

    public void setItem_location(String item_location) {
        this.item_location = item_location;
    }

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
