package com.itcraftsolution.esell;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.Extra.LoadingDialog;
import com.itcraftsolution.esell.Fragment.AccountFragment;
import com.itcraftsolution.esell.Fragment.AdsFragment;
import com.itcraftsolution.esell.Fragment.ChatFragment;
import com.itcraftsolution.esell.Fragment.HomeFragment;
import com.itcraftsolution.esell.Fragment.SellFragment;
import com.itcraftsolution.esell.Model.UserModel;
import com.itcraftsolution.esell.databinding.ActivityMainBinding;
import com.itcraftsolution.esell.spf.SpfUserData;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    private SharedPreferences spf;
    private String Phone, Email, message;
    private int Status, color;
    private DatabaseReference reference;
    private LoadingDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(binding.getRoot());


        dialog = new LoadingDialog(this);

        LoadData();
        defView();
        binding.mainBottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.bNavHome:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.bNavChat:
                        selectedFragment = new ChatFragment();
                        break;
                    case R.id.bNavSell:
                        selectedFragment = new SellFragment();
                        break;
                    case R.id.bNavMyAds:
                        selectedFragment = new AdsFragment();
                        break;
                    case R.id.bNavAccount:
                        selectedFragment = new AccountFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, selectedFragment).addToBackStack(null).commit();
                return true;
            }
        });
//        }

    }

    private void defView() {
        FragmentTransaction firstFragment = getSupportFragmentManager().beginTransaction();
        firstFragment.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
        firstFragment.replace(R.id.frMainContainer, new HomeFragment());
        firstFragment.commit();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void LoadData() {
        dialog.StartLoadingDialog();
        SpfUserData data = new SpfUserData(this);
        spf = data.getSpf();
        Phone = spf.getString("UserPhone", null);
        Email = spf.getString("UserEmail", null);
        Status = spf.getInt("UserStatus", 0);

        ApiUtilities.apiInterface().ReadUserEmail( Email).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel model = response.body();
                if (model != null) {
                    if (model.getMessage() == null) {
                        dialog.StopLoadingDialog();
                        data.setSpf(model.getId(), model.getPhone(), model.getEmail(), model.getUser_img(),
                                model.getUser_name(), model.getUser_bio(), model.getLocation(), model.getCity_area(), model.getStatus(), model.getAuth_id());

                    } else {
                        dialog.StopLoadingDialog();
                        Toast.makeText(MainActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                dialog.StopLoadingDialog();
                Toast.makeText(MainActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (binding.mainBottomNav.getSelectedItemId() == R.id.bNavHome) {
            super.onBackPressed();
        } else if (binding.mainBottomNav.getSelectedItemId() == R.id.bNavSell) {
            super.onBackPressed();
            binding.mainBottomNav.setSelectedItemId(R.id.bNavHome);
        } else if (binding.mainBottomNav.getSelectedItemId() == R.id.bNavChat) {
            if (binding.mainBottomNav.getSelectedItemId() == R.id.bNavChat) {
                super.onBackPressed();
                binding.mainBottomNav.setSelectedItemId(R.id.bNavHome);
            } else {
                binding.mainBottomNav.setSelectedItemId(R.id.bNavChat);
            }
        } else if (binding.mainBottomNav.getSelectedItemId() == R.id.bNavMyAds) {
            if (binding.mainBottomNav.getSelectedItemId() == R.id.bNavMyAds) {
                binding.mainBottomNav.setSelectedItemId(R.id.bNavHome);
            } else {
                binding.mainBottomNav.setSelectedItemId(R.id.bNavMyAds);
            }
        } else if (binding.mainBottomNav.getSelectedItemId() == R.id.bNavAccount) {
            if (binding.mainBottomNav.getSelectedItemId() == R.id.bNavAccount) {
                binding.mainBottomNav.setSelectedItemId(R.id.bNavHome);
            } else {
                binding.mainBottomNav.setSelectedItemId(R.id.bNavAccount);
            }
        } else {
            binding.mainBottomNav.setSelectedItemId(R.id.bNavHome);
        }
    }
}