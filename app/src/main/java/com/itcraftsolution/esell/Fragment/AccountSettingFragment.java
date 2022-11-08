package com.itcraftsolution.esell.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.Extra.LoadingDialog;
import com.itcraftsolution.esell.Model.ResponceModel;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.UserLogin;
import com.itcraftsolution.esell.databinding.FragmentAccountSettingFargmentBinding;
import com.itcraftsolution.esell.spf.SpfUserData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Account Setting Fragment
public class AccountSettingFragment extends Fragment {

    public AccountSettingFragment() {
        // Required empty public constructor
    }

    private FragmentAccountSettingFargmentBinding binding;
    private FirebaseAuth auth;
    private boolean isDelete = false;
    private int UserId;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentAccountSettingFargmentBinding.inflate(getLayoutInflater());

        //Firebase User Authentication Check
        auth = FirebaseAuth.getInstance();

        // Logout Method
        binding.txLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Logout Account")
                        .setMessage("Are Sure To Logout This Account ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SpfUserData spfUserData = new SpfUserData(requireContext());
                                UserId = spfUserData.getSpf().getInt("UserId", 0);
                                loadingDialog = new LoadingDialog(requireActivity());
                                loadingDialog.StartLoadingDialog();
                                ApiUtilities.apiInterface().LastUpdateUser(UserId, 0)
                                        .enqueue(new Callback<ResponceModel>() {
                                            @Override
                                            public void onResponse(Call<ResponceModel> call, Response<ResponceModel> response) {
                                                ResponceModel responceModel = response.body();
                                                if (responceModel != null) {
                                                    if (responceModel.getMessage().equals("fail")) {
                                                        loadingDialog.StopLoadingDialog();
                                                        Toast.makeText(requireActivity(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        loadingDialog.StopLoadingDialog();
                                                        isDelete = spfUserData.RemoveAllSpf(requireContext());
                                                        if (isDelete) {
                                                            auth.signOut();
                                                            Intent intent = new Intent(getContext(), UserLogin.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(intent);
                                                            requireActivity().finish();
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponceModel> call, Throwable t) {
                                                loadingDialog.StopLoadingDialog();
                                                Toast.makeText(requireActivity(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        })
                        .setNegativeButton("No", null)
                        .setIcon(R.drawable.ic_baseline_outlet_24)
                        .show();
            }
        });

        return binding.getRoot();
    }

    // Theme Shared Preference Method
    private void ThemeMode(boolean Mode) {
        SharedPreferences spf = requireContext().getSharedPreferences("ThemeMode", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putBoolean("ThemeMode", Mode);
        editor.apply();

    }
}