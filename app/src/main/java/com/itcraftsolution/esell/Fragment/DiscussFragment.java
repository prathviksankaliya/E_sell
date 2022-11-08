package com.itcraftsolution.esell.Fragment;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.Extra.LoadingDialog;
import com.itcraftsolution.esell.Model.UserModel;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.FragmentDiscussBinding;
import com.itcraftsolution.esell.spf.SpfUserData;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Discuss Fragment Class
public class DiscussFragment extends Fragment {


    public DiscussFragment() {
        // Required empty public constructor
    }

    private FragmentDiscussBinding binding;
    private SpfUserData spf;
    private int UserId;
    private String UserPhone, UserEmail, UserImg, UserName, ItemPrice, ItemTitle, ItemLocation, ItemDesc, ItemImg;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDiscussBinding.inflate(getLayoutInflater());
        loadingDialog = new LoadingDialog(requireActivity());

        // Call LoadData Method
        LoadData();

        //Call Method
        binding.cvDiscussCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(requireContext()).withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + UserPhone));
                                try {
                                    startActivity(intent);
                                } catch (Exception e) {
                                    Toast.makeText(requireContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        //Email Method
        binding.cvDiscussEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setData(Uri.parse("mailto:"));
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{UserEmail});
                i.putExtra(Intent.EXTRA_SUBJECT, "Are you Sell this item?");
                i.putExtra(Intent.EXTRA_TEXT, "I've found this on #E-Sell. are You Sell this item? (playstore link)");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(requireContext(), "There are no email clients installed.", Toast.LENGTH_LONG).show();
                }
            }
        });
        return binding.getRoot();
    }

    // LoadData Method
    // Load User Data From Server
    private void LoadData() {
        spf = new SpfUserData(requireContext());
        if (spf.getItemDetails().getInt("Discuss", 0) == 1) {

            UserId = spf.getItemDetails().getInt("UserId", 0);
            loadingDialog.StartLoadingDialog();
            ApiUtilities.apiInterface().ReadUserId(UserId)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            UserModel model = response.body();
                            if (model != null) {
                                if (model.getMessage() == null) {
                                    loadingDialog.StopLoadingDialog();
                                    UserPhone = model.getPhone();
                                    UserEmail = model.getEmail();
                                    UserImg = model.getUser_img();
                                    UserName = model.getUser_name();

                                    Glide.with(requireContext()).load(ApiUtilities.UserImage + UserImg).into(binding.igProfileDp);
                                    binding.txDiscussUserName.setText(UserName);

                                } else {
                                    loadingDialog.StopLoadingDialog();
                                    Toast.makeText(requireContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            loadingDialog.StopLoadingDialog();
                            Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
                        }
                    });

            ItemPrice = spf.getItemDetails().getString("ItemPrice", null);
            ItemTitle = spf.getItemDetails().getString("ItemTitle", null);
            ItemLocation = spf.getItemDetails().getString("ItemLocation", null);
            ItemDesc = spf.getItemDetails().getString("ItemDesc", null);
            ItemImg = spf.getItemDetails().getString("ItemImg", null);
            binding.txDiscussItemLoc.setText(ItemLocation);
            binding.txDiscussItemName.setText(ItemTitle);
            binding.txDiscussItemPrice.setText(ItemPrice);
        } else {
            binding.MainDiscussLayout.setVisibility(View.GONE);
            binding.llNotFoundDiscuss.setVisibility(View.VISIBLE);
        }
    }
}