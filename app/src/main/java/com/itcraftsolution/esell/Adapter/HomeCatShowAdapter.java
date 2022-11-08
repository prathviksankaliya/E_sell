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
import com.itcraftsolution.esell.Fragment.HomeCatShowFragment;
import com.itcraftsolution.esell.Fragment.ItemDetailsFragment;
import com.itcraftsolution.esell.Model.HomeCatShow;
import com.itcraftsolution.esell.Model.HomeFreshItem;
import com.itcraftsolution.esell.Model.MyAdsItem;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.HomeCatShowSampleBinding;
import com.itcraftsolution.esell.databinding.HomeFreshitemSampleBinding;
import com.itcraftsolution.esell.spf.SpfUserData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Home Category Product Recycler  View Adapter

public class HomeCatShowAdapter extends RecyclerView.Adapter<HomeCatShowAdapter.viewHolder> {

    Context context;
    ArrayList<MyAdsItem> homeCatShows;
    SpfUserData spf;
    public HomeCatShowAdapter(Context context, ArrayList<MyAdsItem> homeCatShows) {
        this.context = context;
        this.homeCatShows = homeCatShows;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_cat_show_sample , parent , false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        //Home Category Product Fetch From Server
        MyAdsItem model = homeCatShows.get(position);
        String img = model.getItem_img();
        List<String> list = new ArrayList<String>(Arrays.asList(img.split(",")));
        Glide.with(context).load(ApiUtilities.SellItemImage+list.get(0)).into(holder.binding.igHomeCatShowImage);
        holder.binding.txHomeCatDesc.setText(model.getDescription());
        holder.binding.txHomeCatPrice.setText(String.valueOf("₹ "+model.getPrice()));
        holder.binding.txHomeCatItemLocation.setText(String.valueOf(model.getCity_area()+" ,"+model.getLocation()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
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
        return homeCatShows.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        HomeCatShowSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = HomeCatShowSampleBinding.bind(itemView);
        }
    }
}
