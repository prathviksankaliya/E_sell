package com.itcraftsolution.esell.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.MainActivity;
import com.itcraftsolution.esell.Model.UserModel;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.FragmentLoginBinding;
import com.itcraftsolution.esell.spf.SpfUserData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Login Fragment
public class login extends Fragment {


    private GoogleSignInClient GoogleSignInClient;
    private FragmentLoginBinding binding;
    private ActivityResultLauncher<Intent> SignInActivityResultLauncher;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    public login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(getLayoutInflater());

        //Firebase Authentication
        auth = FirebaseAuth.getInstance();

        //Process Dialog
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Login Account");
        progressDialog.setMessage("SignIn Account With Google");

        binding.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                fragmentTransaction.replace(R.id.frUserDetailsContainer, new EsellPolicyFragment())
                        .addToBackStack(null).commit();
            }
        });

        //Button SignIn

        binding.btnSignIn.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
            fragmentTransaction.replace(R.id.frUserDetailsContainer, new signInFragment())
                    .addToBackStack(null).commit();
        });

        //sign up button
        binding.btnSignUp.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
            fragmentTransaction.replace(R.id.frUserDetailsContainer, new signUpFragment())
                    .addToBackStack(null).commit();
        });

        // Button Continue With Phone
        binding.btnContinueWithPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                fragmentTransaction.replace(R.id.frUserDetailsContainer, new phoneLogin())
                        .addToBackStack(null).commit();
            }
        });

        //Call CreateRequest Method
        CreateRequest();

        //Button Continue With Google
        binding.btnContinueWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Call SignIn Method
                SignIn();

                //Process Dialog Show
                progressDialog.show();
            }
        });

        //SignIn Process
        SignInActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {

                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                            try {
                                GoogleSignInAccount account = task.getResult(ApiException.class);

                                SignInWithGoogle(account);

                            } catch (ApiException e) {
                                e.printStackTrace();
                            }
                        } else {
                            progressDialog.dismiss();
                        }
                    }
                });

        return binding.getRoot();
    }

    public void checkUserExist (String email){
        SpfUserData data = new SpfUserData(requireContext());
        ApiUtilities.apiInterface().ReadUserEmail(email).enqueue(new Callback<UserModel>() {
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
                        //TODO: send to user fragment
                        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                        fragmentTransaction.remove(login.this);
                        fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                        fragmentTransaction.replace(R.id.frUserDetailsContainer, new UserProfileFragment())
                                .addToBackStack(null).commit();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(requireActivity(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //CreateRequest Method
    private void CreateRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.FirebaseClientid))
                .requestEmail()
                .build();

        GoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
    }

    //SignIn Method
    private void SignIn() {
        Intent intent = GoogleSignInClient.getSignInIntent();
        SignInActivityResultLauncher.launch(intent);

    }

    //SignWithGoogle Method
    private void SignInWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();

                    checkUserExist(account.getEmail());


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Somthing went wrong!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}