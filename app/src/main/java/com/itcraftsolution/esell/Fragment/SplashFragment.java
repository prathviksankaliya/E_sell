package com.itcraftsolution.esell.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.itcraftsolution.esell.MainActivity;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.FragmentSplashBinding;
import com.itcraftsolution.esell.spf.SpfUserData;

//Splash Fragment Class
public class SplashFragment extends Fragment {


    public SplashFragment() {
        // Required empty public constructor
    }

    private FragmentSplashBinding binding;
    private Animation topAnim, bottomAnim;
    private static final int SPLASH_SCREEN = 1600;
    private FirebaseAuth auth;
    private int UserStatus;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(getLayoutInflater());

        //Firebase Authentication
        auth = FirebaseAuth.getInstance();

        //Set Animation
        topAnim = AnimationUtils.loadAnimation(getContext(), R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_animation);

        // Call Method
        // Splash screen
        initViewsAndListener();


        return binding.getRoot();
    }

    //initViewsAndListener Method
    private void initViewsAndListener() {
        binding.igSplashLogo.setAnimation(topAnim);
        binding.tvSlogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SpfUserData spfUserData = new SpfUserData(requireContext());
                UserStatus = spfUserData.getSpf().getInt("UserStatus", 0);
                if (auth.getCurrentUser() != null && UserStatus == 1) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finishAffinity();
                } else {
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.remove(SplashFragment.this);
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                    fragmentTransaction.replace(R.id.frUserDetailsContainer, new login())
                            .addToBackStack(null).commit();
                }

            }
        }, SPLASH_SCREEN);
    }
}
