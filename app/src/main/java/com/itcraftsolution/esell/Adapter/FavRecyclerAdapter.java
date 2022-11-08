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
import com.itcraftsolution.esell.databinding.FavoriteSampleBinding;

import java.util.ArrayList;
import java.util.List;

    //Favorite Item Recycler View Adapter

public class FavRecyclerAdapter extends RecyclerView.Adapter<FavRecyclerAdapter.viewHolder> {
    Context context;
    List<MyAdsItem> list;

    public FavRecyclerAdapter(Context context, List<MyAdsItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_sample, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        MyAdsItem model = list.get(position);

        //Load Favorite Data From Server
        Glide.with(context).load(ApiUtilities.SellItemImage+model.getItem_img()).into(holder.binding.igHomeCatShowImage);
        holder.binding.txHomeCatPrice.setText(String.valueOf("₹ "+model.getPrice()));
        holder.binding.txHomeCatDesc.setText(model.getDescription());
        holder.binding.txHomeCatItemLocation.setText(String.valueOf(model.getCity_area()+" ,"+model.getLocation()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        FavoriteSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = FavoriteSampleBinding.bind(itemView);
        }
    }
}
