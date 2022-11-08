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
import com.itcraftsolution.esell.Fragment.ChatScreenFragment;
import com.itcraftsolution.esell.Model.ChatModel;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.ChatBuyingSampleBinding;
import com.itcraftsolution.esell.spf.SpfUserData;

import java.util.List;

//Chat Buying User Adapter

public class ChatBuyingAdapter extends  RecyclerView.Adapter<ChatBuyingAdapter.viewHolder>{
    List<ChatModel> list;
    Context context;

    public ChatBuyingAdapter(List<ChatModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_buying_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ChatModel model = list.get(position);

        holder.binding.txUserName.setText(model.getUser_name());
        holder.binding.txItemName.setText(model.getItem_title());
        holder.binding.txUserLocation.setText(model.getItem_location());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_rigth,R.anim.enter_from_rigth)
                        .replace(R.id.frMainContainer , new ChatScreenFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
            ChatBuyingSampleBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ChatBuyingSampleBinding.bind(itemView);
        }
    }
}
