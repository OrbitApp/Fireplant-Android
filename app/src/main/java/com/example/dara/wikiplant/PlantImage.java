package com.example.dara.wikiplant;

import com.google.firebase.database.PropertyName;

/**
 * Created by pandawarrior91 on 29/04/2017.
 * Android Studio 2.2
 */

public class PlantImage {
    private String name;
    private String url;
    @PropertyName("main")
    private boolean isMain;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    @Override
    public String toString() {
        return "PlantImage{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", isMain=" + isMain +
                '}';
    }
}
