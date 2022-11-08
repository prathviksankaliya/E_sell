package com.itcraftsolution.esell.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.Model.MyAdsItem;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.ActiveorderSampleBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Active Order Recycler View Adapter

public class AccountMyordersRecyclerAdapter extends RecyclerView.Adapter<AccountMyordersRecyclerAdapter.viewHolder> {

    Context context;
    List<MyAdsItem> list;

    public AccountMyordersRecyclerAdapter(Context context, List<MyAdsItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activeorder_sample, parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        MyAdsItem model = list.get(position);
        String img = model.getItem_img();
        List<String> list = new ArrayList<String>(Arrays.asList(img.split(",")));
        Glide.with(context).load(ApiUtilities.SellItemImage+list.get(0)).into(holder.binding.igSampleImage);
        holder.binding.txSampleName.setText(model.getTitle());
        holder.binding.txSampleDesc.setText(model.getDescription());
        holder.binding.txSamplePrice.setText(String.valueOf("₹ "+model.getPrice()));
        holder.binding.txSampleLocation.setText(String.valueOf(model.getCity_area()+" ,"+model.getLocation()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        ActiveorderSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ActiveorderSampleBinding.bind(itemView);
        }
    }
}
