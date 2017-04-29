package com.example.dara.wikiplant;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by pandawarrior91 on 29/04/2017.
 * Android Studio 2.2
 */

public class UploadPlantImage {
    private File plantImage;
    private Bitmap plantBitmap;

    public UploadPlantImage(File plantImage, Bitmap plantBitmap) {
        this.plantImage = plantImage;
        this.plantBitmap = plantBitmap;
    }

    public File getPlantImage() {
        return plantImage;
    }

    public void setPlantImage(File plantImage) {
        this.plantImage = plantImage;
    }

    public Bitmap getPlantBitmap() {
        return plantBitmap;
    }

    public void setPlantBitmap(Bitmap plantBitmap) {
        this.plantBitmap = plantBitmap;
    }
}
