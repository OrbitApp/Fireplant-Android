package com.example.dara.wikiplant;

/**
 * Created by pandawarrior91 on 30/04/2017.
 * Android Studio 2.2
 */

public class ImageUrlPayload {
    String imageUri;

    public ImageUrlPayload(String s) {
        imageUri = s;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
