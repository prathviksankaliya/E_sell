package com.itcraftsolution.esell.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import com.itcraftsolution.esell.Adapter.SellCategoryRecyclerAdapter;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.Extra.LoadingDialog;
import com.itcraftsolution.esell.Model.HomeCategory;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.FragmentSellBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Sell Fragment Class
public class SellFragment extends Fragment {

    public SellFragment() {
        // Required empty public constructor
    }

    private FragmentSellBinding binding;
    private LoadingDialog loadingDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSellBinding.inflate(getLayoutInflater());

        // Loading Dialog Show
        //Call Loading Dialog
        loadingDialog = new LoadingDialog(requireActivity());
        loadingDialog.StartLoadingDialog();

        //Back Arrow
        // Go To HomeFragment
        binding.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.remove(SellFragment.this);
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                fragmentTransaction.replace(R.id.frMainContainer, new HomeFragment())
                        .addToBackStack(null).commit();
            }
        });

        //Load Category From Server
        ApiUtilities.apiInterface().ReadCategory(1).enqueue(new Callback<List<HomeCategory>>() {
            @Override
            public void onResponse(Call<List<HomeCategory>> call, Response<List<HomeCategory>> response) {
                List<HomeCategory> list = response.body();
                if (list != null) {
                    if (list.get(0).getMessage() == null) {
                        SellCategoryRecyclerAdapter adapter = new SellCategoryRecyclerAdapter(getContext(), list);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
                        binding.rvSellCategory.setLayoutManager(gridLayoutManager);
                        loadingDialog.StopLoadingDialog();
                        binding.rvSellCategory.setAdapter(adapter);
                    } else {
                        loadingDialog.StopLoadingDialog();
                        Toast.makeText(requireContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<HomeCategory>> call, Throwable t) {
                loadingDialog.StopLoadingDialog();
                Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        });


        return binding.getRoot();
    }
}