package com.itcraftsolution.esell.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.FragmentCongressScreenBinding;

//Congerss Screen Fragment
public class CongressScreenFragment extends Fragment {

    public CongressScreenFragment() {
        // Required empty public constructor
    }

    private FragmentCongressScreenBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCongressScreenBinding.inflate(getLayoutInflater());


        //Button PreView Add
        //Go to AdsFragment
        binding.btnPreviewAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.remove(CongressScreenFragment.this);
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                fragmentTransaction.replace(R.id.frMainContainer, new AdsFragment())
                        .addToBackStack(null).commit();
            }
        });

        return binding.getRoot();
    }
}