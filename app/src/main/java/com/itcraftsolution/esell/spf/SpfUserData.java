package com.itcraftsolution.esell.spf;

import android.content.Context;
import android.content.SharedPreferences;

//Shared Preference Class
public class SpfUserData {
    Context context;

    public SpfUserData(Context context) {
        this.context = context;
    }

    public void setSpf(int UserId, String UserPhone, String UserEmail, String UserImage, String UserName, String UserBio, String UserCity, String CityArea, int UserStatus, String authid) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginUserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserPhone", UserPhone);
        editor.putString("UserEmail", UserEmail);
        editor.putString("UserImage", UserImage);
        editor.putString("UserName", UserName);
        editor.putString("UserBio", UserBio);
        editor.putString("UserCity", UserCity);
        editor.putString("CityArea", CityArea);
        editor.putInt("UserStatus", UserStatus);
        editor.putInt("UserId", UserId);
        editor.putString("AuthId", authid);
        editor.apply();

    }

    public SharedPreferences getSpf() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginUserDetails", Context.MODE_PRIVATE);

        return sharedPreferences;
    }

    public void setItemDetail(String Img, String Price, String Title, String Location, String Desc, int Id, int UserId, int Insert, int Update, String Category, String auth_id, int Discuss) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ItemDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("ItemId", Id);
        editor.putInt("UserId", UserId);
        editor.putString("ItemImg", Img);
        editor.putString("ItemPrice", Price);
        editor.putString("ItemTitle", Title);
        editor.putString("ItemLocation", Location);
        editor.putString("ItemDesc", Desc);
        editor.putString("Category", Category);
        editor.putString("Auth_Id", auth_id);
        editor.putInt("Insert", Insert);
        editor.putInt("Update", Update);
        editor.putInt("Discuss", Discuss);
        editor.apply();
    }


    public SharedPreferences getItemDetails() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ItemDetails", Context.MODE_PRIVATE);

        return sharedPreferences;
    }

    public void setCreateChat(String ReceiverId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ChatDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ReceiverId", ReceiverId);
//        editor.putString("UserName", UserName);
//        editor.putString("UserImg", UserImg);
//        editor.putString("ItemLocation", ItemLocation);
//        editor.putString("ItemName", ItemName);
//        editor.putInt("Chat", Chated);
        editor.apply();
    }


    public SharedPreferences getCreateChat() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ChatDetails", Context.MODE_PRIVATE);

        return sharedPreferences;
    }


    public boolean RemoveAllSpf(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginUserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        SharedPreferences sp = context.getSharedPreferences("ItemDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor speditor = sp.edit();
        speditor.clear();
        speditor.apply();

        return true;
    }


}
