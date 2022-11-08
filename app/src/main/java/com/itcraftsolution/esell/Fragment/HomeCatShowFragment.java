package com.itcraftsolution.esell.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import com.itcraftsolution.esell.Adapter.HomeCatShowAdapter;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.Extra.LoadingDialog;
import com.itcraftsolution.esell.Model.HomeCatShow;
import com.itcraftsolution.esell.Model.MyAdsItem;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.FragmentHomeCatShowBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//HomeCatShow Fragment Class
public class HomeCatShowFragment extends Fragment {


    public HomeCatShowFragment() {
        // Required empty public constructor
    }

    private ArrayList<MyAdsItem> list;
    private FragmentHomeCatShowBinding binding;
    private SharedPreferences spf;
    private String categoryName;
    private LoadingDialog loadingDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeCatShowBinding.inflate(getLayoutInflater());

        //Call FetchData Method
        FetchData();

        //Back Arrow
        //Go To HomeFragment
        binding.igHomeCatShowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.remove(HomeCatShowFragment.this);
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                fragmentTransaction.replace(R.id.frMainContainer, new HomeFragment());
                fragmentTransaction.addToBackStack(null).commit();
            }
        });


        return binding.getRoot();
    }

    //FetchData Method
    //Fetch Data From Server
    private void FetchData() {
        loadingDialog = new LoadingDialog(requireActivity());
        loadingDialog.StartLoadingDialog();
        spf = requireContext().getSharedPreferences("SendCategory", Context.MODE_PRIVATE);
        categoryName = spf.getString("Category", null);
        ApiUtilities.apiInterface().SellItemByCategory(categoryName).enqueue(new Callback<List<MyAdsItem>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<MyAdsItem>> call, Response<List<MyAdsItem>> response) {
                assert response.body() != null;
                list = new ArrayList<>();
                list.addAll(response.body());
                if (list != null) {
                    if (list.get(0).getMessage() == null) {
                        HomeCatShowAdapter catShowAdapter = new HomeCatShowAdapter(getContext(), list);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
                        binding.rvHomeCatShow.setLayoutManager(gridLayoutManager);
                        loadingDialog.StopLoadingDialog();
                        binding.rvHomeCatShow.setAdapter(catShowAdapter);
                    } else {
                        loadingDialog.StopLoadingDialog();
                        binding.rvHomeCatShow.setVisibility(View.GONE);
                        binding.llNoDataFound.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MyAdsItem>> call, Throwable t) {
                Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}