package com.example.dara.wikiplant;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pandawarrior91 on 29/04/2017.
 * Android Studio 2.2
 */

public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ViewHolder> {

    ArrayList<File> mUploadPlantImages;

    public ArrayList<File> getUploadPlantImages() {
        return mUploadPlantImages;
    }

    public void setUploadPlantImages(ArrayList<File> uploadPlantImages) {
        mUploadPlantImages = uploadPlantImages;
        notifyDataSetChanged();
    }

    public void addBitmaps(File uploadPlantImage) {
        if (mUploadPlantImages == null) {
            mUploadPlantImages = new ArrayList<>();
        }
        mUploadPlantImages.add(uploadPlantImage);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallery_image, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File bitmap = mUploadPlantImages.get(position);
        if (bitmap != null) {
            Picasso.with(holder.itemView.getContext())
                    .load(bitmap)
                    .into(holder.mButtonGalleryThumb);
        }
    }

    @Override
    public int getItemCount() {
        if (mUploadPlantImages == null) {
            return 0;
        }
        return mUploadPlantImages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.gallery_image_thumb)
        ImageView mButtonGalleryThumb;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
