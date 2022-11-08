package com.itcraftsolution.esell.Api;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtilities {
    // Server Base Url
    private static final String BASE_URL= "https://esell.co.in/esell_api/";
    // Fetch User Images in Server
    public static final String UserImage ="https://esell.co.in/esell_api/user/images/";
    // Fetch Post Images in Server
    public static final String SellItemImage ="https://esell.co.in/esell_api/post/images/";
    // Fetch Admin Images in Server
    public static final String HomeCategory ="https://esell.co.in/admin/assets/images/";
    // Fetch User Policy in Server
    public static final String EsellPolicy = "https://esell.co.in/esell_policy/selling_on_esell.pdf";
    // Fetch User Policy in Server
    public static final String EsellServicePolicy = "https://esell.co.in/esell_policy/esell_services.pdf";

    public static Retrofit retrofit = null;

    public static ApiInterface apiInterface()
    {

        if(retrofit == null)
        {
            Gson gson =new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }

}
