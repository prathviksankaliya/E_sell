package com.itcraftsolution.esell.Model;


// User Model Class
public class User {

    private String bio, email, id, imageURL, locality, phone, search, status, sublocality, username;

    public User() {
    }

    // User Model Constructor
    public User(String bio, String email, String id, String imageURL, String locality, String phone, String search, String status, String sublocality, String username) {
        this.bio = bio;
        this.email = email;
        this.id = id;
        this.imageURL = imageURL;
        this.locality = locality;
        this.phone = phone;
        this.search = search;
        this.status = status;
        this.sublocality = sublocality;
        this.username = username;
    }

    // User Model Class Getter And Setter Method
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSublocality() {
        return sublocality;
    }

    public void setSublocality(String sublocality) {
        this.sublocality = sublocality;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
