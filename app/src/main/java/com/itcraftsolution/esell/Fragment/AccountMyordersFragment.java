package com.itcraftsolution.esell.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.itcraftsolution.esell.Adapter.AccountMyordersRecyclerAdapter;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.Extra.LoadingDialog;
import com.itcraftsolution.esell.Model.MyAdsItem;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.FragmentAccountMyordersBinding;

import com.itcraftsolution.esell.spf.SpfUserData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Account My Order Fragment
public class AccountMyordersFragment extends Fragment {


    public AccountMyordersFragment() {
        // Required empty public constructor
    }

    private FragmentAccountMyordersBinding binding;
    private SpfUserData spfdata;
    private int UserId;
    private AccountMyordersRecyclerAdapter adapter;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountMyordersBinding.inflate(getLayoutInflater());

        //Loading Dialog
        loadingDialog = new LoadingDialog(requireActivity());
        loadingDialog.StartLoadingDialog();


        // Fetch My Order Data From Server
        FetchData();

        //Back Arrow
        // Go To Account Fragment
        binding.igMyOrdersToAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.remove(AccountMyordersFragment.this);
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                fragmentTransaction.replace(R.id.frMainContainer, new AccountFragment());
                fragmentTransaction.addToBackStack(null).commit();
            }
        });
        return binding.getRoot();
    }

    // Fetch Order Data From Server
    //Fetch Data Method
    private void FetchData() {
        spfdata = new SpfUserData(requireContext());
        UserId = spfdata.getSpf().getInt("UserId", 0);

        ApiUtilities.apiInterface().MyadSellItem(UserId).enqueue(new Callback<List<MyAdsItem>>() {
            @Override
            public void onResponse(Call<List<MyAdsItem>> call, Response<List<MyAdsItem>> response) {
                List<MyAdsItem> list = response.body();
                if (list != null) {
                    if (list.get(0).getMessage() == null) {
                        adapter = new AccountMyordersRecyclerAdapter(requireContext(), list);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
                        binding.rvActiveOrdersItem.setLayoutManager(gridLayoutManager);
                        loadingDialog.StopLoadingDialog();
                        binding.rvActiveOrdersItem.setAdapter(adapter);
                    } else {
                        loadingDialog.StopLoadingDialog();
                        binding.rvActiveOrdersItem.setVisibility(View.GONE);
                        binding.llNoDataFound.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MyAdsItem>> call, Throwable t) {
            }
        });
    }
}