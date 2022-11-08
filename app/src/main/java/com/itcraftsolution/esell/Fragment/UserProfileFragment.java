package com.itcraftsolution.esell.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.Extra.LoadingDialog;
import com.itcraftsolution.esell.MainActivity;
import com.itcraftsolution.esell.Model.ResponceModel;
import com.itcraftsolution.esell.Model.UserModel;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.FragmentUserProfileBinding;
import com.itcraftsolution.esell.spf.SpfUserData;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//User Profile Fragment
public class UserProfileFragment extends Fragment {

    public UserProfileFragment() {
        // Required empty public constructor
    }

    private FragmentUserProfileBinding binding;
    private String Sublocality, Locality, City, Name, Email, Phone, About, Location, encodeImageString, destpath;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int PERMISSION_ID = 44;
    private Bitmap bitmap;
    private ActivityResultLauncher<String> mGetContent;
    private int Status;
    private DatabaseReference reference;
    private LoadingDialog dialog;
    private Uri PhotoUri;
    private SpfUserData spfUserData;
    private LoadingDialog loadingDialog;
    private GoogleSignInAccount account;
    boolean CheckImage = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserProfileBinding.inflate(getLayoutInflater());

        //Loading Dialog Show
        loadingDialog = new LoadingDialog(requireActivity());

        //Call LoadData Methode
        LoadData();

        //Set Image From Gallery
        binding.igProfileDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStoragePermission()) {
                    //Intent For Image Select
                    mGetContent.launch("image/*");

                } else {
                    //Check Permission
                    requestStoragePermission();
                }
            }
        });

        //From Validation
        // If All Right Profile Save
        binding.btnNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Objects.requireNonNull(binding.edUserName.getText()).toString().length() < 4) {
                    binding.txProfileNameError.setText("Name must be Minimum 4 characters");
                    binding.txProfileNameError.setTextColor(getResources().getColor(R.color.red));
                    binding.edUserName.requestFocus();
                } else if (!CheckImage || binding.igProfileDp.getDrawable() == null) {
                    Toast.makeText(getContext(), "Please Set Your Profile Picture ", Toast.LENGTH_SHORT).show();
                } else if (Objects.requireNonNull(binding.edUserAboutUs.getText()).toString().length() < 8) {
                    binding.txProfileAboutError.setText("About Us must be Minimum 8 characters");
                    binding.txProfileAboutError.setTextColor(getResources().getColor(R.color.red));
                    binding.edUserAboutUs.requestFocus();
                } else if (Objects.requireNonNull(binding.edUserPhoneNumber.getText()).toString().length() != 10) {
                    binding.txProfilePhoneError.setText("PhoneNumber must be Minimum 10 Digits");
                    binding.txProfilePhoneError.setTextColor(getResources().getColor(R.color.red));
                    binding.edUserPhoneNumber.requestFocus();
                } else if (!ValidEmail(Objects.requireNonNull(binding.edUserEmail.getText()).toString())) {
                    binding.txProfileEmailError.setText("Please Enter Email In Valid Format");
                    binding.txProfileNameError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txProfileAboutError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txProfilePhoneError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txProfileEmailError.setTextColor(getResources().getColor(R.color.red));
                    binding.edUserEmail.requestFocus();
                } else if (binding.txLocationn.getText().toString().equals("CityName")) {
                    binding.txProfileLocationError.setText("Please Confirm Your Location");
                    binding.txProfileEmailError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txProfileNameError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txProfileAboutError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txProfilePhoneError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txProfileLocationError.setTextColor(getResources().getColor(R.color.red));
                    binding.txUserLocation.requestFocus();
                } else if (!binding.cbPolicy.isChecked()) {
                    binding.txPolicyError.setText("Please agreed Policy");
                    binding.txPolicyError.setTextColor(getResources().getColor(R.color.red));
                } else {
                    loadingDialog.StartLoadingDialog();
                    binding.txProfileNameError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txProfileAboutError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txProfilePhoneError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txProfileEmailError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txProfileLocationError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txPolicyError.setText("");
                    binding.txPolicyError.setTextColor(getResources().getColor(R.color.blue_grey));

                    binding.igVerify.setVisibility(View.VISIBLE);

                    Name = binding.edUserName.getText().toString();

                    About = binding.edUserAboutUs.getText().toString();

                    //TODO: check data
                    Phone = "+91" + binding.edUserPhoneNumber.getText().toString();

                    Email = binding.edUserEmail.getText().toString();
                    Location = binding.txLocationn.getText().toString();

                    //Loading Dialog Show
                    loadingDialog.StartLoadingDialog();

                    //store User Data To Server
                    ApiUtilities.apiInterface().ReadUser(Phone, Email)
                            .enqueue(new Callback<UserModel>() {
                                @Override
                                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                    UserModel model = response.body();
                                    if (model != null) {
                                        if (model.getMessage() == null) {
                                            Status = model.getStatus();
                                            if (Status == 0) {
                                                ApiUtilities.apiInterface().UpdateUser(model.getId(), encodeImageString, Name, About, Locality, Sublocality, 1)
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
                                                                        Toast.makeText(requireContext(), "" + responceModel.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        spfUserData = new SpfUserData(requireContext());
                                                                        spfUserData.setSpf(model.getId(), Phone, Email, encodeImageString, Name, About, Locality, Sublocality, 1, FirebaseAuth.getInstance().getUid());
                                                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                                                        startActivity(intent);
                                                                        requireActivity().finishAffinity();
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<ResponceModel> call, Throwable t) {
                                                                loadingDialog.StopLoadingDialog();
                                                                Toast.makeText(requireActivity(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            } else {
                                                Toast.makeText(requireContext(), "Try To Login other Email", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            //Shared Preference
                                            //Store User Data
                                            spfUserData = new SpfUserData(requireContext());
                                            spfUserData.setSpf(0, Phone, Email, encodeImageString, Name, About, Locality, Sublocality, 1, FirebaseAuth.getInstance().getUid());
                                            String Auth_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                            ApiUtilities.apiInterface().InsertUser(Phone, Email, encodeImageString, Name, About, Locality, Sublocality, 1, Auth_id)
                                                    .enqueue(new Callback<ResponceModel>() {
                                                        @Override
                                                        public void onResponse(Call<ResponceModel> call, Response<ResponceModel> response) {
                                                            ResponceModel responceModel = response.body();
                                                            if (responceModel != null) {
                                                                if (responceModel.getMessage().equals("fail")) {
                                                                    Toast.makeText(requireActivity(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    ApiUtilities.apiInterface().ReadUser(Phone, Email).enqueue(new Callback<UserModel>() {
                                                                        @Override
                                                                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                                                            UserModel model = response.body();
                                                                            if (model != null) {
                                                                                if (model.getMessage() == null) {
                                                                                    loadingDialog.StopLoadingDialog();
                                                                                    HashMap<String, String> hashMap = new HashMap<>();
                                                                                    hashMap.put("id", FirebaseAuth.getInstance().getUid());
                                                                                    hashMap.put("username", model.getUser_name());
                                                                                    hashMap.put("bio", model.getUser_bio());
                                                                                    hashMap.put("phone", model.getPhone());
                                                                                    hashMap.put("email", model.getEmail());
                                                                                    hashMap.put("locality", model.getLocation());
                                                                                    hashMap.put("sublocality", model.getCity_area());
                                                                                    hashMap.put("imageURL", model.getUser_img());
                                                                                    hashMap.put("status", "offline");
                                                                                    hashMap.put("search", model.getUser_name().toLowerCase());
                                                                                    reference = FirebaseDatabase.getInstance().getReference("Users").child(model.getAuth_id());
                                                                                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                Intent intent = new Intent(getContext(), MainActivity.class);
                                                                                                startActivity(intent);
                                                                                                requireActivity().finishAffinity();
                                                                                                Toast.makeText(requireContext(), "" + responceModel.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        }
                                                                                    });
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


                                                                }
                                                            } else {
                                                                Toast.makeText(requireActivity(), "Something went Wrong!", Toast.LENGTH_SHORT).show();
                                                            }
                                                            loadingDialog.StopLoadingDialog();
                                                        }

                                                        @Override
                                                        public void onFailure(Call<ResponceModel> call, Throwable t) {
                                                            loadingDialog.StopLoadingDialog();
                                                            Toast.makeText(requireActivity(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<UserModel> call, Throwable t) {
                                    loadingDialog.StopLoadingDialog();
                                    Toast.makeText(requireActivity(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        //Get Current Location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        binding.txUserLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to get location
                getLastLocation();
            }
        });

        //Load Image In Array
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result.getPath() != null) {
                    PhotoUri = result;
                    destpath = UUID.randomUUID().toString() + ".png";

                    UCrop.Options options = new UCrop.Options();
                    options.setCompressionQuality(70);
                    options.setCircleDimmedLayer(true);
                    options.setCompressionFormat(Bitmap.CompressFormat.PNG);


                    UCrop.of(PhotoUri, Uri.fromFile(new File(requireContext().getCacheDir(), destpath)))
                            .withOptions(options)
                            .withAspectRatio(0, 0)
                            .useSourceImageAspectRatio()
                            .withMaxResultSize(1080, 720)
                            .start(requireContext(), UserProfileFragment.this);

                }

            }
        });
        return binding.getRoot();
    }

    //ValidEmail Method
    private boolean ValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    //requestStoragePermission Method
    //Request For Permission
    private void requestStoragePermission() {

        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_ID);
    }

    //checkStoragePermission Method
    // Check Permission
    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    //LoadData Method
    //Fetch LoadData Method
    private void LoadData() {
        SpfUserData spfUserData = new SpfUserData(requireContext());
        GoogleSignIn.getLastSignedInAccount(requireContext());
        account = GoogleSignIn.getLastSignedInAccount(requireContext());
        if (spfUserData.getSpf().getString("UserPhone", null) != null) {
            binding.edUserPhoneNumber.setText(spfUserData.getSpf().getString("UserPhone", null));
            binding.edUserPhoneNumber.setInputType(InputType.TYPE_NULL);
        } else if (account != null) {
            assert getArguments() != null;
            if (getArguments().containsKey("mail")){
                binding.edUserEmail.setText(getArguments().getString("mail"));
            }else {
                binding.edUserEmail.setText(account.getEmail());
            }
            binding.edUserEmail.setInputType(InputType.TYPE_NULL);
            binding.edUserAboutUs.setText(account.getGivenName());
            binding.edUserName.setText(account.getDisplayName());

        } else {
//            Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
        }
    }

    //Get Multiple Image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            assert data != null;
            final Uri resulturi = UCrop.getOutput(data);
            try {
                InputStream inputStream = requireContext().getContentResolver().openInputStream(resulturi);
                bitmap = BitmapFactory.decodeStream(inputStream);
                encodeBitmapImage(bitmap);
                CheckImage = true;
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Get Current Location
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {
            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
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
                                binding.txLocationn.setVisibility(View.VISIBLE);
                                binding.txUserLocation.setVisibility(View.INVISIBLE);
                                binding.txLocationn.setText(City);

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

    // Get Last Location
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
                binding.txLocationn.setText(City);


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
//         ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
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
            } else {
                Toast.makeText(requireContext(), "Oops! you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }

    }


    //encodeBitmapImage Method
    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, byteArrayOutputStream);
//        Toast.makeText(requireContext(), ""+bitmap.getByteCount(), Toast.LENGTH_SHORT).show();
        binding.igProfileDp.setImageBitmap(bitmap);
        byte[] bytesofimage = byteArrayOutputStream.toByteArray();
        encodeImageString = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }

}