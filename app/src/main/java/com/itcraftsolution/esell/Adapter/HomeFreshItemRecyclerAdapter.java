package com.itcraftsolution.esell.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.Fragment.ItemDetailsFragment;
import com.itcraftsolution.esell.Model.HomeFreshItem;
import com.itcraftsolution.esell.Model.MyAdsItem;
import com.itcraftsolution.esell.Model.ResponceModel;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.HomeFreshitemSampleBinding;
import com.itcraftsolution.esell.spf.SpfUserData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Home  Fresh Item Product Recycler View

public class HomeFreshItemRecyclerAdapter extends RecyclerView.Adapter<HomeFreshItemRecyclerAdapter.viewHolder> {

    Context context;
    List<MyAdsItem> homeFreshItems;
    SpfUserData spf;
    Boolean clicked = true;
    int like = 0;

    public HomeFreshItemRecyclerAdapter(Context context, List<MyAdsItem> homeFreshItems) {
        this.context = context;
        this.homeFreshItems = homeFreshItems;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.home_freshitem_sample , parent , false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        MyAdsItem model = homeFreshItems.get(position);

        //Home Fresh Product Fetch From Server
        String img = model.getItem_img();
        List<String> list = new ArrayList<String>(Arrays.asList(img.split(",")));
    Glide.with(context).load(ApiUtilities.SellItemImage+list.get(0)).into(holder.binding.igSampleHomeFreshItem);
    holder.binding.txSampleHomeFreshItemName.setText(model.getTitle());
    holder.binding.txSampleHomeFreshItemPrice.setText(String.valueOf("₹ "+model.getPrice()));
    if(model.getFav() == 1)
    {
        holder.binding.igHomeLike.setImageResource(R.drawable.liked);
        clicked = false;
    }
    holder.binding.txSampleHomeFreshItemLocation.setText(String.valueOf(model.getCity_area()+" ,"+model.getLocation()));

        holder.binding.igHomeLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clicked)
                {
                    clicked = false;
                    holder.binding.igHomeLike.setImageResource(R.drawable.liked);
                    like = 1;
                    ApiUtilities.apiInterface().UpdateLike(model.getId(),1)
                            .enqueue(new Callback<ResponceModel>() {
                                @Override
                                public void onResponse(Call<ResponceModel> call, @NonNull Response<ResponceModel> response) {
                                    ResponceModel responceModel = response.body();
                                    if(responceModel != null)
                                    {
                                        Toast.makeText(context, ""+responceModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<ResponceModel> call, Throwable t) {
                                    Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }else {
                    clicked = true;
                    like = 0;
                    holder.binding.igHomeLike.setImageResource(R.drawable.like);
                    ApiUtilities.apiInterface().UpdateLike(model.getId(),0)
                            .enqueue(new Callback<ResponceModel>() {
                                @Override
                                public void onResponse(Call<ResponceModel> call, @NonNull Response<ResponceModel> response) {
                                    ResponceModel responceModel = response.body();
                                    if(responceModel != null)
                                    {
                                        Toast.makeText(context, "Remove To Favorite", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<ResponceModel> call, Throwable t) {
                                }
                            });
                }
            }
        });

    holder.binding.igSampleHomeFreshItem.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            spf = new SpfUserData(context);
            spf.setItemDetail(model.getItem_img(), String.valueOf("₹ "+model.getPrice()),model.getTitle(),String.valueOf(model.getCity_area()+" ,"+model.getLocation())
                    ,model.getDescription(),model.getId(), model.getUser_id(),0,0,model.getCat_name(),model.getAuth_id(),0);

            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_rigth,R.anim.enter_from_rigth)
                    .replace(R.id.frMainContainer , new ItemDetailsFragment())
                    .addToBackStack(null)
                    .commit();
        }
    });
    }

    @Override
    public int getItemCount() {
        return homeFreshItems.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        HomeFreshitemSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = HomeFreshitemSampleBinding.bind(itemView);
        }
    }
}
