
package com.example.javasouls2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class StaticViewAdapterOccupier extends RecyclerView.Adapter<StaticViewAdapterOccupier.StaticViewHolder>{


    private ArrayList<RecyclerViewModelOccupier> items;

    private static RecyclerViewClickInterface recyclerViewClickInterface;

    public StaticViewAdapterOccupier(ArrayList<RecyclerViewModelOccupier> items,RecyclerViewClickInterface recyclerViewClickInterface) {
        this.items = items;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public StaticViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_card_design_occupiers,parent,false);
        StaticViewHolder staticRVViewHolder = new StaticViewHolder(view);
        return staticRVViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StaticViewHolder holder, int position) {

        RecyclerViewModelOccupier currentItem = items.get(position);
        holder.image.setImageResource(currentItem.getImage());
        holder.Name.setText(currentItem.getName());
        holder.Age.setText(currentItem.getAge());
        holder.Phone.setText(currentItem.getPhoneNumber());
        holder.Since.setText(currentItem.getSince());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class StaticViewHolder extends RecyclerView.ViewHolder{

        private TextView Name;
        private de.hdodenhof.circleimageview.CircleImageView image;
        private TextView Age;
        private TextView Phone;
        private TextView Since;

        public StaticViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });

            Name = itemView.findViewById(R.id.OccupierName);
            image =  itemView.findViewById(R.id.OccupierProfile);
            Age = itemView.findViewById(R.id.AgeOfOccupier);
            Phone = itemView.findViewById(R.id.OccupierPhoneNumber);
            Since = itemView.findViewById(R.id.OccupiedSince);
        }
    }
}
