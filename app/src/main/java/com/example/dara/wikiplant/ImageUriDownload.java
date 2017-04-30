package com.example.dara.wikiplant;

/**
 * Created by pandawarrior91 on 30/04/2017.
 * Android Studio 2.2
 */

public class ImageUriDownload {
    private String error;
    private String key;

    public String getError() {
        return error;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "ImageUriDownload{" +
                "error='" + error + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
