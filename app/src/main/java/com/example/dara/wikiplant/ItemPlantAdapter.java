package com.example.dara.wikiplant;

import android.app.LauncherActivity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Dara on 29/04/2017.
 */

public class ItemPlantAdapter extends RecyclerView.Adapter<ItemPlantAdapter.PlantViewHolder> {

   private List<PlantClass> listItems;
    private Context context;

    public ItemPlantAdapter(List<PlantClass> list, Context context) {
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
       PlantClass listItem = listItems.get(position);
        holder.name.setText(listItem.getName());
        holder.description.setText(listItem.getDescription());
        System.out.println("Name from ViewHolder"+listItem.getName());
    }

    @Override
    public int getItemCount(){
        return listItems.size();
    }


    public  class PlantViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView description;
        public PlantViewHolder(View itemView) {
            super(itemView);
          name = (TextView) itemView.findViewById(R.id.textView_name);
          description = (TextView) itemView.findViewById(R.id.textView_description);
        }
    }

}
