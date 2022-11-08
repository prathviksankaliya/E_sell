package com.itcraftsolution.esell.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.Fragment.AdsFragment;
import com.itcraftsolution.esell.Fragment.SellItemFormFragment;
import com.itcraftsolution.esell.Model.MyAdsItem;
import com.itcraftsolution.esell.Model.ResponceModel;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.MyadsItemSampleBinding;
import com.itcraftsolution.esell.spf.SpfUserData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//User Added Product Recycler View Adapter

public class MyAdsItemAdapter extends RecyclerView.Adapter<MyAdsItemAdapter.viewHolder>{
    Context context;
    List<MyAdsItem> myAdsItems;
    int ItemId;

    public MyAdsItemAdapter(Context context, List<MyAdsItem> myAdsItems) {
        this.context = context;
        this.myAdsItems = myAdsItems;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.myads_item_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        MyAdsItem myAdsItem = myAdsItems.get(position);
        String img = myAdsItem.getItem_img();

        // Fetch User Added Product From Server
        List<String> list = new ArrayList<String>(Arrays.asList(img.split(",")));
        Glide.with(context).load(ApiUtilities.SellItemImage+list.get(0)).into(holder.binding.igItemImage);
        holder.binding.txItemName.setText(myAdsItem.getTitle());
        holder.binding.txItemPrice.setText(String.valueOf("₹ "+myAdsItem.getPrice()));
        holder.binding.txDesc.setText(myAdsItem.getDescription());
        holder.binding.txItemLocation.setText(String.valueOf(myAdsItem.getCity_area()+" ,"+myAdsItem.getLocation()));
        holder.binding.txDate.setText(myAdsItem.getDate());
        SpfUserData spf = new SpfUserData(context);
        spf.setItemDetail(myAdsItem.getItem_img(),String.valueOf(myAdsItem.getPrice()),myAdsItem.getTitle(),String.valueOf(myAdsItem.getCity_area()+" ,"+myAdsItem.getLocation()),
                myAdsItem.getDescription(),myAdsItem.getId(),myAdsItem.getUser_id(),0,1,myAdsItem.getCat_name(), myAdsItem.getAuth_id(),0);
        //Edit Added Product
        holder.binding.txEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_rigth,R.anim.enter_from_rigth)
                        .replace(R.id.frMainContainer , new SellItemFormFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        //Delete Added Product
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Post")
                        .setMessage("Are Sure To Delete This Post ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SpfUserData spf = new SpfUserData(context);
                                ItemId = spf.getItemDetails().getInt("ItemId", 0);

                                ApiUtilities.apiInterface().DeleteSellItem(ItemId,0)
                                        .enqueue(new Callback<ResponceModel>() {
                                            @Override
                                            public void onResponse(Call<ResponceModel> call, Response<ResponceModel> response) {
                                                ResponceModel responceModel = response.body();
                                                if (responceModel != null) {
                                                    Toast.makeText(context, "" + responceModel.getMessage(), Toast.LENGTH_SHORT).show();
                                                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                                                            .setCustomAnimations(R.anim.enter_from_rigth,R.anim.enter_from_rigth)
                                                            .replace(R.id.frMainContainer , new AdsFragment())
                                                            .addToBackStack(null)
                                                            .commit();
                                                } else {
                                                    Toast.makeText(context, "Something went Wrong!", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponceModel> call, Throwable t) {
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", null)
                        .setIcon(R.drawable .ic_baseline_outlet_24)
                        .show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return myAdsItems.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        MyadsItemSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = MyadsItemSampleBinding.bind(itemView);
        }
    }
}
