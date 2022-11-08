package com.itcraftsolution.esell.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.itcraftsolution.esell.Adapter.HomeCatRecyclerAdapter;
import com.itcraftsolution.esell.Adapter.HomeFreshItemRecyclerAdapter;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.Extra.LoadingDialog;
import com.itcraftsolution.esell.Model.HomeCategory;
import com.itcraftsolution.esell.Model.MyAdsItem;
import com.itcraftsolution.esell.Model.UserModel;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.FragmentHomeBinding;
import com.itcraftsolution.esell.spf.SpfUserData;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Home Fragment Class
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }


    private String Sublocality, Locality, City, searchtext;
    private FragmentHomeBinding binding;
    private HomeFreshItemRecyclerAdapter homeFreshitem;
    private FusedLocationProviderClient mFusedLocationClient;
    private int PERMISSION_ID = 44;
    private SpfUserData spfdata;
    private List<MyAdsItem> list;
    private int UserId, searchUserId;
    private LoadingDialog loadingDialog;
    private SharedPreferences spf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        //LoadDialog show
        loadingDialog = new LoadingDialog(requireActivity());

        //Search Bar Condition
        if (!binding.edHomeSearch.getText().toString().isEmpty()) {
            //Search Number
            //Call SearchData Method
            searchtext = binding.edHomeSearch.getText().toString();
            searchtext = "+91" + searchtext;
            SearchData(searchtext);
        } else {
            // Call FetchData Methood
            FetchData();
        }


        //Get Current Location
        binding.tvCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to get location
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
                getLastLocation();
            }
        });

        //get User Current Location & Print it.
        spf = requireContext().getSharedPreferences("UserLocation", Context.MODE_PRIVATE);
        binding.tvCityName.setText(spf.getString("UserLocation", null));

        //Search Bar
        binding.edHomeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.edHomeSearch.getText().toString().isEmpty()) {
                    searchtext = binding.edHomeSearch.getText().toString();
                    searchtext = "+91" + searchtext;
                    SearchData(searchtext);
                } else {
                    //Call FetchData Method
                    FetchData();
                }

            }
        });

        //Home Category
        // Go TO  SellFragment
        binding.igHomeCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.remove(HomeFragment.this);
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                fragmentTransaction.replace(R.id.frMainContainer, new SellFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });


        return binding.getRoot();
    }

    //SearchData Method
    private void SearchData(String searchtext) {
        ApiUtilities.apiInterface().ReadUserPhone(searchtext).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel model = response.body();
                if (model != null) {
                    if (model.getMessage() == null) {
                        searchUserId = model.getId();
                        ApiUtilities.apiInterface().MyadSellItem(searchUserId).enqueue(new Callback<List<MyAdsItem>>() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onResponse(Call<List<MyAdsItem>> call, Response<List<MyAdsItem>> response) {
                                assert response.body() != null;
                                list.clear();
                                list.addAll(response.body());
                                if (list != null) {
                                    if (list.get(0).getMessage() == null) {
                                        homeFreshitem.notifyDataSetChanged();
                                    } else {
                                        loadingDialog.StopLoadingDialog();
                                        binding.llNoDataFound.setVisibility(View.VISIBLE);
                                        binding.rvHomeFreshItems.setVisibility(View.GONE);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<List<MyAdsItem>> call, Throwable t) {
                                Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        binding.llNoDataFound.setVisibility(View.VISIBLE);
                        binding.rvHomeFreshItems.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //FetchData Method
    //Fetch Data From Server
    private void FetchData() {

        loadingDialog.StartLoadingDialog();
        spfdata = new SpfUserData(requireContext());
        UserId = spfdata.getSpf().getInt("UserId", 0);

        ApiUtilities.apiInterface().ReadCategory(1).enqueue(new Callback<List<HomeCategory>>() {
            @Override
            public void onResponse(Call<List<HomeCategory>> call, Response<List<HomeCategory>> response) {
                List<HomeCategory> list = response.body();
                if (list != null) {
                    if (list.get(0).getMessage() == null) {
                        HomeCatRecyclerAdapter adapter = new HomeCatRecyclerAdapter(getContext(), list);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                        binding.rvHomeCategory.setLayoutManager(linearLayoutManager);
                        loadingDialog.StopLoadingDialog();
                        binding.rvHomeCategory.setAdapter(adapter);
                    } else {
                        loadingDialog.StopLoadingDialog();
                        Toast.makeText(requireContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<HomeCategory>> call, Throwable t) {
                loadingDialog.StopLoadingDialog();
                Toast.makeText(requireContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
            }
        });

        //Fetch Home Fresh Item From Server
        ApiUtilities.apiInterface().ReadSellItem(1).enqueue(new Callback<List<MyAdsItem>>() {
            @Override
            public void onResponse(Call<List<MyAdsItem>> call, Response<List<MyAdsItem>> response) {
                list = response.body();
                if (list != null) {
                    if (list.get(0).getMessage() == null) {
                        homeFreshitem = new HomeFreshItemRecyclerAdapter(requireContext(), list);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                        binding.rvHomeFreshItems.setLayoutManager(gridLayoutManager);
                        loadingDialog.StopLoadingDialog();
                        binding.rvHomeFreshItems.setAdapter(homeFreshitem);
                    } else {
                        loadingDialog.StopLoadingDialog();
                        binding.llNoDataFound.setVisibility(View.VISIBLE);
                        binding.rvHomeFreshItems.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), "Data Not Found", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MyAdsItem>> call, Throwable t) {
                loadingDialog.StopLoadingDialog();
                Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        });
        loadingDialog.StopLoadingDialog();
    }


    //Get Location Method
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {
            // check if location is enabled
            if (isLocationEnabled()) {

                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {

                            try {
                                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                // binding.tvCityName.setText(String.valueOf(addresses.get(0).getSubLocality()));

                                Locality = addresses.get(0).getLocality();
                                Sublocality = addresses.get(0).getSubLocality();
                                City = Sublocality + ", " + Locality;
                                binding.tvCityName.setText(City);
                                StoreUserLocation(City);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            try {
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);


                Locality = addresses.get(0).getLocality();
                Sublocality = addresses.get(0).getSubLocality();
                City = Sublocality + "," + Locality;
                binding.tvCityName.setText(City);


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    // If everything is alright then
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }


    // Shared Preference
    //StoreUserLocation Method
    private void StoreUserLocation(String UserLocation) {
        SharedPreferences spf = requireContext().getSharedPreferences("UserLocation", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("UserLocation", UserLocation);
        editor.apply();
    }
}