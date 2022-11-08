package com.itcraftsolution.esell.Model;

//Home Category Show Model
public class HomeCatShow {
    private int ItemImage;
    private String ItemName, ItemPrice, ItemLocation;

    //Home Category Show Constructor
    public HomeCatShow(int itemImage, String itemName, String itemPrice, String itemLocation) {
        ItemImage = itemImage;
        ItemName = itemName;
        ItemPrice = itemPrice;
        ItemLocation = itemLocation;
    }

    public HomeCatShow() {

    }

    // Home Category Getter And Setter Method
    public int getItemImage() {
        return ItemImage;
    }

    public void setItemImage(int itemImage) {
        ItemImage = itemImage;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getItemLocation() {
        return ItemLocation;
    }

    public void setItemLocation(String itemLocation) {
        ItemLocation = itemLocation;
    }

}
