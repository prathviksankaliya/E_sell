package com.itcraftsolution.esell.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.itcraftsolution.esell.databinding.FragmentEsellPolicyBinding;


public class EsellPolicyFragment extends Fragment {


    public EsellPolicyFragment() {
        // Required empty public constructor
    }

    private FragmentEsellPolicyBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEsellPolicyBinding.inflate(getLayoutInflater());

        binding.pdfViewPager.fromAsset("esell_services.pdf").load();
        return binding.getRoot();
    }
}
