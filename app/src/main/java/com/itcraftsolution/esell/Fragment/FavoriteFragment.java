package com.itcraftsolution.esell.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.itcraftsolution.esell.Adapter.FavRecyclerAdapter;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.Extra.LoadingDialog;
import com.itcraftsolution.esell.Model.MyAdsItem;
import com.itcraftsolution.esell.databinding.FragmentFavoriteBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Favorite Fragment Class
public class FavoriteFragment extends Fragment {


    public FavoriteFragment() {
        // Required empty public constructor
    }

    private FragmentFavoriteBinding binding;
    private FavRecyclerAdapter adapter;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(getLayoutInflater());

        //Call FetchData Method
        FetchData();


        return binding.getRoot();
    }

    //FetchData Method
    //Fetch Data From Server
    private void FetchData() {
        loadingDialog = new LoadingDialog(requireActivity());
        loadingDialog.StartLoadingDialog();
        ApiUtilities.apiInterface().ReadLike(1).enqueue(new Callback<List<MyAdsItem>>() {
            @Override
            public void onResponse(Call<List<MyAdsItem>> call, Response<List<MyAdsItem>> response) {
                List<MyAdsItem> list = response.body();
                if (list != null) {
                    if (list.get(0).getMessage() == null) {
                        adapter = new FavRecyclerAdapter(requireContext(), list);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
                        binding.rvFavoriteItem.setLayoutManager(gridLayoutManager);
                        loadingDialog.StopLoadingDialog();
                        binding.rvFavoriteItem.setAdapter(adapter);
                    } else {
                        loadingDialog.StopLoadingDialog();
                        Toast.makeText(requireContext(), "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MyAdsItem>> call, Throwable t) {
                loadingDialog.StopLoadingDialog();
                Toast.makeText(requireContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}