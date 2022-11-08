package com.itcraftsolution.esell.Api;

import com.itcraftsolution.esell.Model.HomeCategory;
import com.itcraftsolution.esell.Model.MyAdsItem;
import com.itcraftsolution.esell.Model.ResponceModel;
import com.itcraftsolution.esell.Model.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    // Insert User
    @FormUrlEncoded
    @POST("user/create_user.php")
    Call<ResponceModel> InsertUser(
            @Field("phone") String phone,
            @Field("user_email") String email, @Field("user_img") String user_img,
            @Field("user_name") String user_name, @Field("user_bio") String user_bio,
            @Field("user_location") String location, @Field("user_area") String city_area,
            @Field("user_status") int status, @Field("auth_id") String auth_id
    );

    // Read User By Phone , Email
    @FormUrlEncoded
    @POST("user/read_user.php")
    Call<UserModel> ReadUser(
            @Field("phone") String phone,
            @Field("email") String email
    );


    // Read User By Phone , Email
    @FormUrlEncoded
    @POST("user/read_user_email.php")
    Call<UserModel> ReadUserEmail(
            @Field("email") String phone
    );


    // Read User by Phone
    @FormUrlEncoded
    @POST("user/read_user_phone.php")
    Call<UserModel> ReadUserPhone(
            @Field("phone") String phone
    );

    // Read User by User id
    @FormUrlEncoded
    @POST("user/read_user_id.php")
    Call<UserModel> ReadUserId(
            @Field("id") int id

    );

    // Update User
    @FormUrlEncoded
    @POST("user/update_user.php")
    Call<ResponceModel> UpdateUser(
            @Field("id") int id, @Field("user_img") String user_img,
            @Field("user_name")String username,@Field("user_bio")String userbio,
            @Field("location")String location,@Field("city_area")String cite_area,
            @Field("status") int status
    );

    // Logout Time Update Status 0.
    @FormUrlEncoded
    @POST("user/lastupdate_user.php")
    Call<ResponceModel> LastUpdateUser(
            @Field("id") int id, @Field("status") int status
    );


//    @FormUrlEncoded
//    @POST("post/create_post.php")
//    Call<ResponceModel> InsertSellItem(
//            @Field("user_id") int user_id, @Field("cat_name") String cat_name,
//            @Field("title") String title, @Field("description") String description,
//            @Field("price") int price, @Field("location") String location,
//            @Field("city_area") String city_area, @Field("item_img") String item_img,
//            @Field("status") int status, @Field("auth_id") String auth_id
//    );

    // Read User Sell Item
    @FormUrlEncoded
    @POST("post/myad_post.php")
    Call<List<MyAdsItem>> MyadSellItem(
            @Field("user_id") int user_id
    );

    // Search Data by Category
    @FormUrlEncoded
    @POST("post/search_post_cat.php")
    Call<List<MyAdsItem>> SellItemByCategory(
            @Field("cat_name") String category
    );

    // Read User Post by Status 1.
    @FormUrlEncoded
    @POST("post/read_post.php")
    Call<List<MyAdsItem>> ReadSellItem(
            @Field("status") int status
    );

    // Read Category by Status 1.
    @FormUrlEncoded
    @POST("category/read_category.php")
    Call<List<HomeCategory>> ReadCategory(
            @Field("status") int status
    );

//
//    @FormUrlEncoded
//    @POST("post/update_post.php")
//    Call<ResponceModel> UpdateSellItem(
//            @Field("id") int id, @Field("cat_name") String cat_name,
//            @Field("title") String title, @Field("description") String description,
//            @Field("price") int price, @Field("location") String location,
//            @Field("city_area") String city_area, @Field("item_img") String item_img,
//            @Field("status") int status
//    );

    // Update Post Like
    @FormUrlEncoded
    @POST("post/fav_post.php")
    Call<ResponceModel> UpdateLike(
            @Field("id") int id, @Field("fav") int fav
    );

    // Read User Like
    @FormUrlEncoded
    @POST("post/fav_read_post.php")
    Call<List<MyAdsItem>> ReadLike(
            @Field("fav") int fav
    );

    // Update Sell Item Status 0 When Delete
    @FormUrlEncoded
    @POST("post/delete_post.php")
    Call<ResponceModel> DeleteSellItem(
            @Field("id") int id,@Field("status") int status
    );

    // Insert Sell Item with Multipart
    @Multipart
    @POST("post/upload_img.php")
    Call<ResponceModel> uploadImages( @Part List<MultipartBody.Part> images,
                                      @Part("user_id") int user_id, @Part("cat_name") RequestBody cat_name,
                                      @Part("title") RequestBody title, @Part("description") RequestBody description,
                                      @Part("price") int price, @Part("location") RequestBody location,
                                      @Part("city_area") RequestBody city_area,
                                      @Part("status") int status, @Part("auth_id") RequestBody auth_id
    );

    // Update Sell Item with Multipart
    @Multipart
    @POST("post/update_post.php")
    Call<ResponceModel> UpdateuploadImages( @Part List<MultipartBody.Part> images,
                                      @Part("id") int id, @Part("cat_name") RequestBody cat_name,
                                      @Part("title") RequestBody title, @Part("description") RequestBody description,
                                      @Part("price") int price, @Part("location") RequestBody location,
                                      @Part("city_area") RequestBody city_area,
                                      @Part("status") int status
    );

    @FormUrlEncoded
    @POST("post/post_size.php")
    Call<ResponceModel> ReadPostSize(
            @Field("user_id") int user_id,
            @Field("status") int status
    );


    //TODO test uddokta pay

    @POST("post/create_post.php")
    Call<ResponceModel> InsertSellItem(
            @Field("user_id") int user_id, @Field("cat_name") String cat_name,
            @Field("title") String title, @Field("description") String description,
            @Field("price") int price, @Field("location") String location,
            @Field("city_area") String city_area, @Field("item_img") String item_img,
            @Field("status") int status, @Field("auth_id") String auth_id
    );

}

