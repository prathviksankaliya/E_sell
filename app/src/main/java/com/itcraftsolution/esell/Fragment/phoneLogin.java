package com.itcraftsolution.esell.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.spf.SpfUserData;

//phoneLogin Fragment Class
public class phoneLogin extends Fragment {


    public phoneLogin() {
        // Required empty public constructor
    }

    private EditText etPhone;
    private Button btnNext;
    private ImageView ivBack;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_phone_login, container, false);


        etPhone = view.findViewById(R.id.etEnterPhoneNumber);
        btnNext = view.findViewById(R.id.btnPhoneNumLogin);
        ivBack = view.findViewById(R.id.ivphoneDetailsBackArrow);

        //Button Get OTP
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPhoneNumber()) {
                    StoreUserDetails(etPhone.getText().toString());
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.remove(phoneLogin.this);
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                    fragmentTransaction.replace(R.id.frUserDetailsContainer, new confirmationCode())
                            .addToBackStack(null).commit();
                }
            }
        });

        // Back Arrow
        //Go to Login Fragment
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.remove(phoneLogin.this);
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                fragmentTransaction.replace(R.id.frUserDetailsContainer, new login())
                        .addToBackStack(null).commit();
            }
        });
        return view;

    }

    // checkPhoneNumber Method
    private boolean checkPhoneNumber() {
        boolean condition = true;
        if (etPhone.getText().toString().length() != 10) {
            etPhone.setError("Phone number must be 10 digits");
            condition = false;
        }
        return condition;
    }

    //Shared Preference
    //Store Phone Number
    private void StoreUserDetails(String PhoneNumber) {
        SpfUserData spfUserData = new SpfUserData(requireContext());
        spfUserData.setSpf(0, PhoneNumber, null, null, null, null, null, null, 0, null);
    }

}


