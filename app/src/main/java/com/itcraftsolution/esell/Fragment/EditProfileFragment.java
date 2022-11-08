package com.itcraftsolution.esell.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.Model.UserModel;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.FragmentEditProfileBinding;
import com.itcraftsolution.esell.spf.SpfUserData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// EditProfile Fragment Class
public class EditProfileFragment extends Fragment {

    public EditProfileFragment() {
        // Required empty public constructor
    }

    private SharedPreferences spf;
    private FragmentEditProfileBinding binding;
    private String Phone, Email;
    private int Status;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(getLayoutInflater());


        //Call LoadData Method
        LoadData();

        //Back Arrow
        // Go TO Account Fragment
        binding.igEditToAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.remove(EditProfileFragment.this);
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                fragmentTransaction.replace(R.id.frMainContainer, new AccountFragment()).addToBackStack(null).commit();
            }
        });

        // Update The Profile all code commented.

//        binding.btnEditImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mGetContent.launch("image/*");
//            }
//        });
//
//        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
//            @Override
//            public void onActivityResult(Uri result) {
//                if (result.getPath() != null) {
//                    PhotoUri = result;
//                    destpath = UUID.randomUUID().toString() + ".png";
//
//                    UCrop.Options options = new UCrop.Options();
//                    options.setCompressionQuality(70);
//                    options.setCircleDimmedLayer(true);
//                    options.setCompressionFormat(Bitmap.CompressFormat.PNG);
//
//
//                    UCrop.of(PhotoUri, Uri.fromFile(new File(requireContext().getCacheDir(), destpath)))
//                            .withOptions(options)
//                            .withAspectRatio(0, 0)
//                            .useSourceImageAspectRatio()
//                            .withMaxResultSize(1080, 720)
//                            .start(requireContext(), EditProfileFragment.this);
//
//                }
//
//            }
//        });

//        binding.txSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (binding.edProfileName.getText().toString().length() < 4) {
//                    binding.txerrorName.setText("Name must be Minimum 4 characters");
//                    binding.txerrorName.setTextColor(getResources().getColor(R.color.red));
//                    binding.edProfileName.requestFocus();
//                } else if (!CheckImage || binding.igProfileDp.getDrawable() == null) {
//                    Toast.makeText(getContext(), "Please Change Your Profile Picture ", Toast.LENGTH_SHORT).show();
//                } else if (binding.edProfileAbout.getText().toString().length() < 7) {
//                    binding.txerrorAbout.setText("About must be Minimum 7 characters");
//                    binding.txerrorAbout.setTextColor(getResources().getColor(R.color.red));
//                    binding.edProfileAbout.requestFocus();
//                } else {
//                    LoadingDialog loadingDialog = new LoadingDialog(requireActivity());
//                    loadingDialog.StartLoadingDialog();
//                    binding.igVerify.setVisibility(View.VISIBLE);
//                    binding.txerrorAbout.setTextColor(getResources().getColor(R.color.blue_grey));
//                    binding.txerrorName.setTextColor(getResources().getColor(R.color.blue_grey));
//                    SpfUserData spfUserData= new SpfUserData(requireContext());
//                    UserId = spfUserData.getSpf().getInt("UserId", 0);
//                    Locality = spfUserData.getSpf().getString("UserCity", null);
//                    Sublocality = spfUserData.getSpf().getString("CityArea", null);
//                    Name = binding.edProfileName.getText().toString();
//                    About = binding.edProfileAbout.getText().toString();
//
//                    ApiUtilities.apiInterface().UpdateUser(UserId,encodeImageString,Name,About,Locality,Sublocality,1)
//                            .enqueue(new Callback<ResponceModel>() {
//                                @Override
//                                public void onResponse(Call<ResponceModel> call, Response<ResponceModel> response) {
//                                    ResponceModel responceModel = response.body();
//                                    if(responceModel != null)
//                                    {
//                                        if(responceModel.getMessage().equals("fail"))
//                                        {
//                                            loadingDialog.StopLoadingDialog();
//                                            Toast.makeText(requireActivity(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
//                                        }
//                                        else {
//                                            loadingDialog.StopLoadingDialog();
//                                            Toast.makeText(requireContext(), ""+responceModel.getMessage(), Toast.LENGTH_SHORT).show();
//                                            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
//                                            fragmentTransaction.remove(EditProfileFragment.this);
//                                            fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
//                                            fragmentTransaction.replace(R.id.frMainContainer, new AccountFragment()).addToBackStack(null).commit();
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<ResponceModel> call, Throwable t) {
//                                    loadingDialog.StopLoadingDialog();
//                                    Toast.makeText(requireActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//
//                }
//
//            }
//        });

        return binding.getRoot();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
//
//            assert data != null;
//            final Uri resulturi = UCrop.getOutput(data);
//            try {
//                InputStream inputStream = requireContext().getContentResolver().openInputStream(resulturi);
//                bitmap = BitmapFactory.decodeStream(inputStream);
//                encodeBitmapImage(bitmap);
//                CheckImage = true;
//            } catch (Exception e) {
//                Toast.makeText(requireContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void encodeBitmapImage(Bitmap bitmap) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 70, byteArrayOutputStream);
//        binding.igProfileDp.setImageBitmap(bitmap);
//        byte[] bytesofimage = byteArrayOutputStream.toByteArray();
//        encodeImageString = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
//    }

    //LoadData Method
    //Load Data From Server
    private void LoadData() {
        SpfUserData data = new SpfUserData(requireContext());
        spf = data.getSpf();
        Phone = spf.getString("UserPhone", null);
        Email = spf.getString("UserEmail", null);
        Status = spf.getInt("UserStatus", 0);

        ApiUtilities.apiInterface().ReadUser(Phone, Email).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel model = response.body();
                if (model != null) {
                    if (model.getMessage() == null) {

                        binding.edProfileName.setText(model.getUser_name());
                        binding.edProfileEmail.setText(model.getEmail());
                        binding.edProfileAbout.setText(model.getUser_bio());
                        binding.edProfilePhoneNumber.setText(model.getPhone());
                        binding.edProfileEmail.setInputType(InputType.TYPE_NULL);
                        binding.edProfilePhoneNumber.setInputType(InputType.TYPE_NULL);
                        binding.edProfileAbout.setInputType(InputType.TYPE_NULL);
                        binding.edProfileName.setInputType(InputType.TYPE_NULL);
                        Glide.with(requireContext()).load(ApiUtilities.UserImage + model.getUser_img())
                                .into(binding.igProfileDp);
                        data.setSpf(model.getId(), model.getPhone(), model.getEmail(), model.getUser_img(),
                                model.getUser_name(), model.getUser_bio(), model.getLocation(), model.getCity_area(), model.getStatus(), model.getAuth_id());
                    } else {
                        Toast.makeText(requireContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}