package com.itcraftsolution.esell.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.Fragment.HomeCatShowFragment;
import com.itcraftsolution.esell.Fragment.login;
import com.itcraftsolution.esell.Model.HomeCategory;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.HomeCategorySampleBinding;

import java.util.ArrayList;
import java.util.List;

//Home Category Recycler View
public class HomeCatRecyclerAdapter extends RecyclerView.Adapter<HomeCatRecyclerAdapter.viewHolder> {

    Context context;
    List<HomeCategory> homeCategories;
    SharedPreferences spf;

    public HomeCatRecyclerAdapter(Context context, List<HomeCategory> homeCategories) {
        this.context = context;
        this.homeCategories = homeCategories;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_category_sample , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        HomeCategory homeCategory = homeCategories.get(position);

        //Fetch Category From Server
        Glide.with(context).load(ApiUtilities.HomeCategory+homeCategory.getCat_img()).into(holder.binding.igSampleHomeCat);
        holder.binding.txSampleHomeCat.setText(homeCategory.getCat_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spf = context.getSharedPreferences("SendCategory", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = spf.edit();
                editor.putString("Category", homeCategory.getCat_name());
                editor.apply();
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_rigth,R.anim.enter_from_rigth)
                        .replace(R.id.frMainContainer , new HomeCatShowFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return homeCategories.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        HomeCategorySampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = HomeCategorySampleBinding.bind(itemView);

        }
    }
}
