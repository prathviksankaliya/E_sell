package com.itcraftsolution.esell.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itcraftsolution.esell.databinding.FragmentSellinEsellBinding;


public class SellinEsellFragment extends Fragment {


    public SellinEsellFragment() {
        // Required empty public constructor
    }

    private FragmentSellinEsellBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSellinEsellBinding.inflate(getLayoutInflater());

        binding.pdfViewPager.fromAsset("selling_on_esell.pdf").load();
        return binding.getRoot();
    }

}