package com.itcraftsolution.esell.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.Fragment.SellItemFormFragment;
import com.itcraftsolution.esell.Model.HomeCategory;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.SellCategorySampleBinding;
import com.itcraftsolution.esell.spf.SpfUserData;

import java.util.ArrayList;
import java.util.List;

//Category Recycler View Adapter

public class SellCategoryRecyclerAdapter extends RecyclerView.Adapter<SellCategoryRecyclerAdapter.viewHolder> {

    Context context;
    List<HomeCategory> sellCategories;

    public SellCategoryRecyclerAdapter(Context context, List<HomeCategory> sellCategories) {
        this.context = context;
        this.sellCategories = sellCategories;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sell_category_sample, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        HomeCategory homeCategory = sellCategories.get(position);

        Glide.with(context).load(ApiUtilities.HomeCategory + homeCategory.getCat_img()).into(holder.binding.igSellCategorySample);
        holder.binding.txSellCategoryName.setText(homeCategory.getCat_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpfUserData spfUserData = new SpfUserData(context);
                spfUserData.setItemDetail(null, null, null, null, null, 0, 0, 1, 0, homeCategory.getCat_name(), null, 0);
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth)
                        .replace(R.id.frMainContainer, new SellItemFormFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return sellCategories.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        SellCategorySampleBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SellCategorySampleBinding.bind(itemView);
        }
    }
}
