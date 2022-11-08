package com.itcraftsolution.esell.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.MainActivity;
import com.itcraftsolution.esell.Model.ResponceModel;
import com.itcraftsolution.esell.Model.UserModel;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.FragmentConfirmationCodeBinding;
import com.itcraftsolution.esell.spf.SpfUserData;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Confirmation Code Class Fragment
public class confirmationCode extends Fragment {


    private FragmentConfirmationCodeBinding binding;
    private String UserNumber, VerifyId, PhoneNumber;
    private FirebaseAuth auth;
    private SharedPreferences spf;
    private ProgressDialog dialog, CheckOtpDialog;

    public confirmationCode() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentConfirmationCodeBinding.inflate(getLayoutInflater());

        //Call Method
        dialog = new ProgressDialog(getContext());
        CheckOtpDialog = new ProgressDialog(getContext());
        dialog.setTitle("OTP Verification");
        dialog.setMessage("Sending OTP ...");
        CheckOtpDialog.setTitle("OTP Verification");
        CheckOtpDialog.setMessage("Verify PhoneNumber & OTP ...");

        // Firebase Authentication
        auth = FirebaseAuth.getInstance();

        UserNumber = getspfData().getSpf().getString("UserPhone", null);
        //TODO change it to  indian number +91
        PhoneNumber = "+91" + UserNumber;
        binding.tvDisplayPhoneNumber.setText(PhoneNumber);

        //Send Verification Code Method
        sendVerificationCode(PhoneNumber);

        //Dialog Method call
        dialog.show();

        //Button To Send Otp
        binding.btnContinuePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckOtp()) {
                    verifyCode(binding.otpView.getOTP(), PhoneNumber);
                    CheckOtpDialog.show();
                }

            }
        });

        return binding.getRoot();
    }

    // Send Verification Code Method
    private void sendVerificationCode(String phoneNumber) {

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setActivity(requireActivity())
                .setPhoneNumber(phoneNumber)
                .setCallbacks(callbacks)
                .setTimeout(30L, TimeUnit.SECONDS)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override

        //onVerificationCompleted Method
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            final String Code = phoneAuthCredential.getSmsCode();
            if (Code != null) {
                binding.otpView.setOTP(Code);
                verifyCode(Code, PhoneNumber);
            }
        }

        //onVerificationFailed Method
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
        }

        //onCodeSent Method
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            dialog.dismiss();
            VerifyId = s;
        }
    };

    //Verify Code Method
    private void verifyCode(String code, String phone) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerifyId, code);
//        checkUserExist(phone);
        SignInWithCredential(credential);
    }

    public void checkUserExist (String phone) {
        SpfUserData data = new SpfUserData(requireContext());
        ApiUtilities.apiInterface().ReadUserPhone(phone).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel model = response.body();
                if (model != null) {
                    if (model.getMessage() == null) {
                        data.setSpf(model.getId(), model.getPhone(), model.getEmail(), model.getUser_img(),
                                model.getUser_name(), model.getUser_bio(), model.getLocation(), model.getCity_area(), model.getStatus(), model.getAuth_id());

                        Intent i = new Intent(requireContext(), MainActivity.class);
                        startActivity(i);
                        requireActivity().finish();

                    } else {
                        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                        fragmentTransaction.remove(confirmationCode.this);
                        fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                        fragmentTransaction.replace(R.id.frUserDetailsContainer, new UserProfileFragment());
                        fragmentTransaction.addToBackStack(null).commit();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(requireActivity(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //SignInWithCredential Method
    private void SignInWithCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    CheckOtpDialog.dismiss();
                    checkUserExist(PhoneNumber);

                } else {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Please Try Again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Check Otp Method
    // Check Automatic OTP
    private boolean CheckOtp() {
        boolean condition = true;
        if (binding.otpView.getOTP().length() != 6) {
            Toast.makeText(getContext(), "Fill The OTP", Toast.LENGTH_SHORT).show();
            condition = false;
        }
        return condition;
    }

    // Shared Preference
    // SpfData Method
    private SpfUserData getspfData() {
        SpfUserData spfUserData = new SpfUserData(requireContext());
        spfUserData.getSpf();
        return spfUserData;
    }

}