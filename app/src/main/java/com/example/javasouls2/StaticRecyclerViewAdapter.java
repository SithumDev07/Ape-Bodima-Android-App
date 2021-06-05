package com.example.javasouls2;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StaticRecyclerViewAdapter extends RecyclerView.Adapter<StaticRecyclerViewAdapter.StaticRVViewHolder>{

    private ArrayList<StaticRecyclerViewModel> items;

    private static RecyclerViewClickInterface recyclerViewClickInterface;

    int selected_position = 0;

    private ArrayList<StaticRecyclerViewModel> Source;
    private Timer timer;

    public StaticRecyclerViewAdapter(ArrayList<StaticRecyclerViewModel> items, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.items = items;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        Source = items;
    }

    @NonNull
    @Override
    public StaticRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_card_design,parent,false);
        StaticRVViewHolder staticRVViewHolder = new StaticRVViewHolder(view);
        return staticRVViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StaticRVViewHolder holder, int position) {

        StaticRecyclerViewModel currentItem = items.get(position);
        holder.image.setImageResource(currentItem.getImage());
        holder.Title.setText(currentItem.getText());
        holder.rental.setText(currentItem.getPayment());
        holder.rooms.setText(currentItem.getRooms());
        holder.bathrooms.setText(currentItem.getBathrooms());
        holder.rating.setRating(currentItem.getRating());

        //holder.itemView.setBackgroundColor(selected_position == position ? Color.rgb(199,236,238) : Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class StaticRVViewHolder extends RecyclerView.ViewHolder{

        private TextView Title;
        private ImageView image;
        private TextView rooms;
        private TextView bathrooms;
        private TextView rental;
        private RatingBar rating;

        public StaticRVViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //notifyItemChanged(selected_position);
                    //selected_position = getAdapterPosition();
                    //notifyItemChanged(selected_position);

                    recyclerViewClickInterface.onItemClick(getAdapterPosition());

                }
            });

            Title = itemView.findViewById(R.id.AnnexAd1TextTitle);
            image =  itemView.findViewById(R.id.annexView1);
            rooms = itemView.findViewById(R.id.NoOfRooms1);
            bathrooms = itemView.findViewById(R.id.NoOfBathrooms1);
            rental = itemView.findViewById(R.id.RentalPerMonthAd1);
            rating = itemView.findViewById(R.id.Ratingad1);
        }
    }


    public void searchAds(final String searchKeyword){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if ((searchKeyword.trim().isEmpty())){
                    items = Source;
                }
                else{
                    ArrayList<StaticRecyclerViewModel> temp = new ArrayList<>();
                    for(StaticRecyclerViewModel ad : Source){
                        if (ad.getText().toLowerCase().contains(searchKeyword.toLowerCase())){
                            temp.add(ad);
                        }
                    }

                    items = temp;

                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }

        },100);
    }

    public void CancelTimer(){
        if (timer != null){
            timer.cancel();
        }
    }

}
