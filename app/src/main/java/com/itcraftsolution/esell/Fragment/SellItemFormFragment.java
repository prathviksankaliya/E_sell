package com.itcraftsolution.esell.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.Extra.LoadingDialog;
import com.itcraftsolution.esell.Model.ResponceModel;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.FragmentSellItemFormBinding;
import com.itcraftsolution.esell.spf.SpfUserData;
import com.itcraftsolution.esell.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SellItemFormFragment extends Fragment {


    public SellItemFormFragment() {
        // Required empty public constructor
    }

    private FragmentSellItemFormBinding binding;
    private String Title, Desc, Price, Sublocality, Locality, City, Category, OldTitle, OldDesc, OldPrice, ReceiverId;
    private int UserId, Insert, Update, Id, postCount;
    private FusedLocationProviderClient mFusedLocationClient;
    private List<MultipartBody.Part> images;
    private ArrayList<Uri> ImageUris;
    private LoadingDialog loadingDialog;
    private boolean CheckImage = false;
    private int PERMISSION_ID = 44;
    private SpfUserData spf;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSellItemFormBinding.inflate(getLayoutInflater());

        loadingDialog = new LoadingDialog(requireActivity());
        spf = new SpfUserData(requireContext());
        Category = spf.getItemDetails().getString("Category", null);
        Insert = spf.getItemDetails().getInt("Insert", 0);
        Update = spf.getItemDetails().getInt("Update", 0);
        if (Update == 1) {
            loadingDialog.StartLoadingDialog();
            LoadData();
            loadingDialog.StopLoadingDialog();
        }
        ImageUris = new ArrayList<>();

        binding.igSellItemBackCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                fragmentTransaction.replace(R.id.frMainContainer, new AdsFragment())
                        .addToBackStack(null).commit();
            }
        });


        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to get location
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
                getLastLocation();
            }
        });

        binding.btnSelectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mGetContent.launch("image/*");

                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                mGetContent.launch(Intent.createChooser(intent, "Select Image(s)"));

            }
        });


        binding.btnSellItemFormNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Objects.requireNonNull(binding.edSellItemFormTitle.getText()).toString().length() <= 4) {
                    binding.txFormTitleError.setText("* Title must be Minimum 5 characters");
                    binding.txFormTitleError.setTextColor(getResources().getColor(R.color.red));
                    binding.edSellItemFormTitle.requestFocus();
                } else if (Objects.requireNonNull(binding.edSellItemFormDesc.getText()).toString().length() <= 7) {
                    binding.txFormDescError.setText("* Description must be Minimum 8 characters");
                    binding.txFormDescError.setTextColor(getResources().getColor(R.color.red));
                    binding.txFormLocationError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txFormPriceError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txFormTitleError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.edSellItemFormDesc.requestFocus();

                } else if (Objects.requireNonNull(binding.edSellItemFormPrice.getText()).toString().isEmpty()) {
                    binding.txFormPriceError.setText("* Price Must be in Indian Currency");
                    binding.txFormPriceError.setTextColor(getResources().getColor(R.color.red));
                    binding.txFormTitleError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txFormDescError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txFormLocationError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.edSellItemFormPrice.requestFocus();
                } else if (Integer.parseInt(binding.edSellItemFormPrice.getText().toString()) <= 150) {
                    binding.txFormPriceError.setText("* Price must be Minimum 150 Rupees");
                    binding.txFormPriceError.setTextColor(getResources().getColor(R.color.red));
                    binding.txFormTitleError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txFormDescError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txFormLocationError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.edSellItemFormPrice.requestFocus();
                } else if (binding.txLocation.getText().toString().equals("CityName")) {
                    binding.txFormLocationError.setText("Please Confirm Your Location");
                    binding.txFormLocationError.setTextColor(getResources().getColor(R.color.red));
                    binding.txFormPriceError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txFormTitleError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txFormDescError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txFormLocation.requestFocus();
                } else if (!CheckImage) {
                    binding.txSelectImagesError.setText("Please Select Item Images");
                    binding.txSelectImagesError.setTextColor(getResources().getColor(R.color.red));
                    binding.txFormPriceError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txFormTitleError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txFormDescError.setTextColor(getResources().getColor(R.color.blue_grey));
                    binding.txFormLocationError.setTextColor(getResources().getColor(R.color.blue_grey));
                } else {

                    if (Update == 1) {
                        loadingDialog.StartLoadingDialog();
                        Update = 0;
                        Category = spf.getItemDetails().getString("Category", null);
                        Id = spf.getItemDetails().getInt("ItemId", 0);
                        Title = binding.edSellItemFormTitle.getText().toString();
                        Desc = binding.edSellItemFormDesc.getText().toString();
                        Price = binding.edSellItemFormPrice.getText().toString();

//                        ApiUtilities.apiInterface().UpdateSellItem(Id, Category, Title, Desc, Integer.parseInt(Price), Locality, Sublocality, encodeImageString, 1)
//                                .enqueue(new Callback<ResponceModel>() {
//                                    @Override
//                                    public void onResponse(Call<ResponceModel> call, Response<ResponceModel> response) {
//                                        ResponceModel responceModel = response.body();
//                                        if (responceModel != null) {
//                                            loadingDialog.StopLoadingDialog();
//                                            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
//                                            fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
//                                            fragmentTransaction.replace(R.id.frMainContainer, new CongressScreenFragment())
//                                                    .addToBackStack(null).commit();
//                                        } else {
//                                            Toast.makeText(requireActivity(), "Something went Wrong! model khali", Toast.LENGTH_SHORT).show();
//                                        }
//                                        loadingDialog.StopLoadingDialog();
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<ResponceModel> call, Throwable t) {
//                                        loadingDialog.StopLoadingDialog();
//                                        Toast.makeText(requireActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                });
                        images = new ArrayList<>();
                        for (int i = 0; i < ImageUris.size(); i++) {
                            images.add(prepareFilePart("file[" + i + "]", ImageUris.get(i)));
                        }
                        Log.e("mya123", images.toString());

                        RequestBody cat = createPartFromString(Category);
                        RequestBody title = createPartFromString(Title);
                        RequestBody desc = createPartFromString(Desc);
                        RequestBody locality = createPartFromString(Locality);
                        RequestBody sublocality = createPartFromString(Sublocality);

                        ApiUtilities.apiInterface().UpdateuploadImages(images, Id, cat, title, desc, Integer.parseInt(Price), locality, sublocality, 1)
                                .enqueue(new Callback<ResponceModel>() {
                                    @Override
                                    public void onResponse(Call<ResponceModel> call, Response<ResponceModel> response) {
                                        ResponceModel responceModel = response.body();
                                        if (responceModel != null) {
                                            loadingDialog.StopLoadingDialog();
                                            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                                            fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                                            fragmentTransaction.replace(R.id.frMainContainer, new CongressScreenFragment())
                                                    .addToBackStack(null).commit();
                                        } else {
                                            Toast.makeText(requireActivity(), "Something went Wrong!!", Toast.LENGTH_SHORT).show();
                                        }
                                        loadingDialog.StopLoadingDialog();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponceModel> call, Throwable t) {
                                        Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
                                        loadingDialog.StopLoadingDialog();
                                    }
                                });
                    } else {
                        loadingDialog.StartLoadingDialog();
                        //cat_name,title,description,price,location,city_area,item_img,status

                        SpfUserData spfUserData = new SpfUserData(requireContext());
                        Category = spfUserData.getItemDetails().getString("Category", null);
                        UserId = spfUserData.getSpf().getInt("UserId", 0);
                        Title = binding.edSellItemFormTitle.getText().toString();
                        Desc = binding.edSellItemFormDesc.getText().toString();
                        Price = binding.edSellItemFormPrice.getText().toString();

                        ApiUtilities.apiInterface().ReadPostSize(UserId, 1).enqueue(new Callback<ResponceModel>() {
                            @Override
                            public void onResponse(Call<ResponceModel> call, Response<ResponceModel> response) {
                                ResponceModel model = response.body();
                                if (model != null) {
                                    if (model.getMessage() == null) {
                                        postCount = 0;
                                        postCount = model.getPostCount();

                                        if (postCount <= 9 && postCount != 0) {
                                            images = new ArrayList<>();
                                            for (int i = 0; i < ImageUris.size(); i++) {
                                                images.add(prepareFilePart("file[" + i + "]", ImageUris.get(i)));
                                            }
                                            RequestBody cat = createPartFromString(Category);
                                            RequestBody title = createPartFromString(Title);
                                            RequestBody desc = createPartFromString(Desc);
                                            RequestBody locality = createPartFromString(Locality);
                                            RequestBody sublocality = createPartFromString(Sublocality);
                                            ReceiverId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                            RequestBody reciverid = createPartFromString(ReceiverId);

                                            ApiUtilities.apiInterface().uploadImages(images, UserId, cat, title, desc, Integer.parseInt(Price), locality, sublocality, 1, reciverid).enqueue(new Callback<ResponceModel>() {
                                                @Override
                                                public void onResponse(Call<ResponceModel> call, Response<ResponceModel> response) {
                                                    ResponceModel model = response.body();
                                                    if (model != null) {
                                                        if (!model.getMessage().equals("fail")) {
                                                            Toast.makeText(requireContext(), "" + model.getMessage(), Toast.LENGTH_SHORT).show();
                                                            loadingDialog.StopLoadingDialog();
                                                            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                                                            fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                                                            fragmentTransaction.replace(R.id.frMainContainer, new CongressScreenFragment())
                                                                    .addToBackStack(null).commit();
                                                        } else {
                                                            Toast.makeText(requireContext(), "Something went wrong!!" + model.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }

                                                    } else {
                                                        Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
                                                    }
                                                    loadingDialog.StopLoadingDialog();
                                                }

                                                @Override
                                                public void onFailure(Call<ResponceModel> call, Throwable t) {
                                                    loadingDialog.StopLoadingDialog();
                                                    Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            loadingDialog.StopLoadingDialog();
                                        } else {
                                            loadingDialog.StopLoadingDialog();
                                            Toast.makeText(requireContext(), "Sorry! Your 10 Post Already Uploaded", Toast.LENGTH_LONG).show();
                                        }
                                        loadingDialog.StopLoadingDialog();

                                    } else {
                                        loadingDialog.StopLoadingDialog();
                                        Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponceModel> call, Throwable t) {
                                loadingDialog.StopLoadingDialog();
                                Toast.makeText(requireContext(), "Something went wrong!! " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }
        });
        return binding.getRoot();
    }

    private void LoadData() {

        OldTitle = spf.getItemDetails().getString("ItemTitle", null);
        OldDesc = spf.getItemDetails().getString("ItemDesc", null);
        OldPrice = spf.getItemDetails().getString("ItemPrice", null);

        binding.edSellItemFormTitle.setText(OldTitle);
        binding.edSellItemFormDesc.setText(OldDesc);
        binding.edSellItemFormPrice.setText(OldPrice);
        if (Update == 1) {
            binding.btnSellItemFormNext.setText("Update Post");
        }
    }

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        if (data != null) {

                            if (data.getClipData() != null) {
                                ImageUris.clear();
                                int Count = data.getClipData().getItemCount();
                                for (int i = 0; i < Count; i++) {

                                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                    ImageUris.add(imageUri);

                                }
                                Log.e("mya123", ImageUris.toString());
                                CheckImage = true;
                                binding.txSelctedImg.setVisibility(View.VISIBLE);
                                binding.txSelctedImg.setText("You Have " + ImageUris.size() + " Image Selected");
                            } else {
                                ImageUris.clear();
                                Uri imageUri = data.getData();

                                ImageUris.add(imageUri);
                                CheckImage = true;
                                binding.txSelctedImg.setVisibility(View.VISIBLE);
                                binding.txSelctedImg.setText("You Have " + ImageUris.size() + " Image Selected");
//                                try {
//                                    InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
//                                    bitmap = BitmapFactory.decodeStream(inputStream);
//                                    encodeBitmapImage(bitmap);
//                                    CheckImage = true;
//                                } catch (Exception e) {
//                                    Toast.makeText(requireContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
                            }

                        }

                    }

                }
            });

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(requireContext(), fileUri);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(requireActivity().getContentResolver().getType(fileUri)),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

//    private void encodeBitmapImage(Bitmap bitmap) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 70, byteArrayOutputStream);
//        byte[] bytesofimage = byteArrayOutputStream.toByteArray();
//        encodeImageString = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
//    }

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
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
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
                                binding.txLocation.setVisibility(View.VISIBLE);
                                binding.btnVerify.setVisibility(View.INVISIBLE);
                                binding.txLocation.setText(City);


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
                binding.txLocation.setText(City);


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

}