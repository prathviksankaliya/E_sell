package com.itcraftsolution.esell.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.itcraftsolution.esell.Model.SliderModel;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.SliderSampleBinding;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    Context context;
    List<SliderModel> list;

    public SliderAdapter(Context context, List<SliderModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_sample, parent, false);
        return new SliderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, int position) {

        SliderModel model = list.get(position);
        Glide.with(context).load(model.getImgUrl())
                .into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return list.size();
    }


    public static class SliderAdapterViewHolder extends ViewHolder {
        ImageView imageView;
        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sliderImageSample);
        }
    }
}
