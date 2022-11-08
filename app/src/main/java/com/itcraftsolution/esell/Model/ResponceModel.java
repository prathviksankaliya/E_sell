package com.itcraftsolution.esell.Model;

//Retrofit Responce Model Class
public class ResponceModel {
    private String message;
    private int postCount;

    // Responce Class Constructor


    public ResponceModel(String message, int postCount) {
        this.message = message;
        this.postCount = postCount;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    //Responce Getter And Setter Method
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
