package com.itcraftsolution.esell.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.models.SlideModel;
import com.itcraftsolution.esell.Adapter.SliderAdapter;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.Model.SliderModel;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.FragmentProductImageBinding;
import com.itcraftsolution.esell.spf.SpfUserData;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProductImageFragment extends Fragment {


    public ProductImageFragment() {
        // Required empty public constructor
    }

    private FragmentProductImageBinding binding;
    private SpfUserData spf;
    private String ItemImg;
    private List<SliderModel> slideModels;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProductImageBinding.inflate(getLayoutInflater());


        // Call Load Method
        LoadData();

        // Back Arrow
        //Go To Product Detail Fragment
        binding.igItemDetailsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.remove(ProductImageFragment.this);
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                fragmentTransaction.replace(R.id.frMainContainer, new ItemDetailsFragment())
                        .addToBackStack(null).commit();
            }
        });


        return binding.getRoot();
    }

    // LoadData Method
    private void LoadData() {
        spf = new SpfUserData(requireContext());
        ItemImg = spf.getItemDetails().getString("ItemImg", null);
        List<String> list = new ArrayList<String>(Arrays.asList(ItemImg.split(",")));
        slideModels = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {

//            slideModels.add(new SlideModel(ApiUtilities.SellItemImage + list.get(i)));
            slideModels.add(new SliderModel(ApiUtilities.SellItemImage + list.get(i)));

        }
        SliderAdapter adapter = new SliderAdapter(requireContext(), slideModels);
        binding.isProductImagesSlider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        binding.isProductImagesSlider.setSliderAdapter(adapter);

        binding.isProductImagesSlider.setScrollTimeInSec(3);

        binding.isProductImagesSlider.setAutoCycle(true);

        binding.isProductImagesSlider.startAutoCycle();
//        binding.isProductImagesSlider.setImageList(slideModels, true);
    }

}