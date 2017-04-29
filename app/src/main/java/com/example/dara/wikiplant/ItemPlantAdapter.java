package com.example.dara.wikiplant;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Dara on 29/04/2017.
 */

public class ItemPlantAdapter extends RecyclerView.Adapter<ItemPlantAdapter.PlantViewHolder> {

   private List<Plant> listItems;
    private Context context;

    public ItemPlantAdapter(List<Plant> list, Context context) {
        this.listItems = list;
        this.context = context;
    }

    @Override
    public PlantViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plant, parent, false);
        return new PlantViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PlantViewHolder holder, int position){
       Plant plant = listItems.get(position);
        holder.name.setText(plant.getName());
        holder.description.setText(plant.getDescription());
        Picasso.with(holder.itemView.getContext())
                .load(plant.getPlantUrl())
                .into(holder.image);
        System.out.println("Name from ViewHolder"+plant.getName());
    }

    @Override
    public int getItemCount(){
        return listItems.size();
    }


    public  class PlantViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView description;
        ImageView image;
        public PlantViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textView_name);
            description = (TextView) itemView.findViewById(R.id.textView_description);
            image = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

}
