package com.example.javasouls2.HelperClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javasouls2.R;

import java.util.ArrayList;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder> {

    private ArrayList<AnnexHelperClass> items;

    public FeaturedAdapter(ArrayList<AnnexHelperClass> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_card_design,parent,false);
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view);

        return featuredViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {

        AnnexHelperClass CurrentItem = items.get(position);
        holder.image.setImageResource(CurrentItem.getImage());
        holder.title.setText(CurrentItem.getTitle());
        holder.rooms.setText(CurrentItem.getRooms());
        holder.bathrooms.setText(CurrentItem.getBathrooms());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, rooms, bathrooms;


        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);

            //Hooks
            image = itemView.findViewById(R.id.BoardingAdImage);
            title = itemView.findViewById(R.id.AnnexAd1TextTitle);
            rooms = itemView.findViewById(R.id.NoOfRooms1);
            bathrooms = itemView.findViewById(R.id.NoOfBathrooms1);

        }
    }
}
