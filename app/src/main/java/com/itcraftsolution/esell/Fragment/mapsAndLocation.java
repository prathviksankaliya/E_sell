package com.itcraftsolution.esell.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.itcraftsolution.esell.databinding.FragmentMapsAndLocationBinding;
import com.itcraftsolution.esell.spf.SpfUserData;


public class mapsAndLocation extends Fragment {


//    UnUsed Fragment


    public mapsAndLocation() {
        // Required empty public constructor
    }

    private FragmentMapsAndLocationBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String UserEmail, UserImg, UserName, UserBio, UserLocation, UserArea, UserPhone;
    private int UserStatus;
    private GoogleSignInAccount account;
    private SpfUserData spfUserData;
    Uri uri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMapsAndLocationBinding.inflate(getLayoutInflater());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        binding.ivNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                mGetContent.launch(intent);
            }
        });
        binding.btnLocationNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "http://192.168.0.102:80/poetryapi/readpoetry.php";
                RequestQueue requestQueue;
                requestQueue = Volley.newRequestQueue(requireContext());

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("changeapp", response);
                        binding.ivNavigation.setImageURI(Uri.parse("content://media/external_primary/images/media/1032218"));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("changeapp", error.getMessage());
                    }
                });
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://192.168.0.102:80/poetryapi/readpoetry.php", new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("changeapp" , "responce is : "+response);
//                Toast.makeText(requireContext(), ""+response, Toast.LENGTH_SHORT).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Log.d("changeapp" , "Somthing went wrong");
//                Log.d("changeapp", error.getMessage());
//                Toast.makeText(requireContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

                requestQueue.add(stringRequest);


//                if(GetUserData())
//                {
//                    Toast.makeText(requireContext(), "User Created", Toast.LENGTH_SHORT).show();
////                    Intent intent = new Intent(getContext(),MainActivity.class);
////                    startActivity(intent);
////                    requireActivity().finishAffinity();
//                }
//                else {
//                    Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
//                }

            }
        });


        return binding.getRoot();
    }

    //Format Of DataBase Keys: phone,user_email,user_img,user_name,user_bio,user_location,user_area,user_status.
    private boolean GetUserData() {
        account = GoogleSignIn.getLastSignedInAccount(requireContext());
        if (account != null) {
            SharedPreferences preferences = requireActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);

            UserPhone = preferences.getString("UserPhoneNumber", null);
            UserEmail = account.getEmail();
            UserImg = String.valueOf(account.getPhotoUrl());
            UserName = account.getDisplayName();
            UserBio = account.getGivenName();
            UserLocation = "";
            UserArea = "";
            UserStatus = 0;

            Log.d("myapp", UserPhone + " " + UserEmail + " " + UserImg + " " + UserName + " " + UserBio + " " + UserLocation + " " + UserArea + " " + UserStatus);

            spfUserData = new SpfUserData(requireContext());
            spfUserData.setSpf(0, UserPhone, UserEmail, UserImg, UserName, UserBio, UserLocation, UserArea, UserStatus, FirebaseAuth.getInstance().getUid());
        } else {
            Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            uri = result.getData().getData();
                            binding.ivNavigation.setImageURI(uri);
                            Toast.makeText(requireContext(), "" + uri, Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });


}