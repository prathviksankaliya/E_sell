package com.itcraftsolution.esell.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.itcraftsolution.esell.Adapter.MyAdsItemAdapter;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.Extra.LoadingDialog;
import com.itcraftsolution.esell.Model.MyAdsItem;
import com.itcraftsolution.esell.databinding.FragmentAdsBinding;
import com.itcraftsolution.esell.spf.SpfUserData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//User Add Item Fragment
public class AdsFragment extends Fragment {

    public AdsFragment() {
        // Required empty public constructor
    }

    private FragmentAdsBinding binding;
    private SpfUserData spf;
    private MyAdsItemAdapter adapter;
    private int UserId;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdsBinding.inflate(getLayoutInflater());

        // Loading Dialog Show
        loadingDialog = new LoadingDialog(requireActivity());
        loadingDialog.StartLoadingDialog();

        // Fetch Data Method
        // Fetch Data From Server
        FetchData();

        return binding.getRoot();
    }

    // Fetch Data Method
    //Fetch User Added Product From Server
    private void FetchData() {
        // Shared Preference
        spf = new SpfUserData(requireContext());
        UserId = spf.getSpf().getInt("UserId", 0);

        //Fetch User Added Product From Server
        ApiUtilities.apiInterface().MyadSellItem(UserId).enqueue(new Callback<List<MyAdsItem>>() {
            @Override
            public void onResponse(Call<List<MyAdsItem>> call, Response<List<MyAdsItem>> response) {
                List<MyAdsItem> list = response.body();
                if (list != null) {
                    if (list.get(0).getMessage() == null) {
                        adapter = new MyAdsItemAdapter(requireContext(), list);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
                        binding.rvMyAdsItem.setLayoutManager(gridLayoutManager);
                        loadingDialog.StopLoadingDialog();
                        binding.rvMyAdsItem.setAdapter(adapter);
                    } else {
                        loadingDialog.StopLoadingDialog();
                        binding.rvMyAdsItem.setVisibility(View.GONE);
                        binding.llNoDataFound.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MyAdsItem>> call, Throwable t) {
                loadingDialog.StopLoadingDialog();
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}