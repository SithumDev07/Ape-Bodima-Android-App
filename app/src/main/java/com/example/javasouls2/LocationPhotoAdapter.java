package com.example.javasouls2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LocationPhotoAdapter extends RecyclerView.Adapter<LocationPhotoAdapter.SliderHolder>{

    private ArrayList<LocationPhotoAd> locations;
    private ViewPager2 view;


    public LocationPhotoAdapter(ArrayList<LocationPhotoAd> locations, ViewPager2 view) {
        this.locations = locations;
        this.view = view;
    }

    @NonNull
    @Override
    public SliderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item_container,parent,false);

        SliderHolder sliderHolder = new SliderHolder(view);
        return sliderHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SliderHolder holder, int position) {
        //holder.setImage(locations.get(position));

        LocationPhotoAd currentItems = locations.get(position);
        holder.image.setImageBitmap(currentItems.getBitmap());

        if (position == locations.size() - 2){
            view.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    class SliderHolder extends RecyclerView.ViewHolder{

        private RoundedImageView image;

        public SliderHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.ImageSlide);


        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            locations.addAll(locations);
            notifyDataSetChanged();

        }
    };

    

}
