package com.example.dara.wikiplant;

import android.app.LauncherActivity;

import java.util.ArrayList;

/**
 * Created by Dara on 29/04/2017.
 */

public class PlantClass  {
    private String name;
    private String description;
    private String imageURI;
    public PlantClass(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public PlantClass() {
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PlantClass{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageURI='" + imageURI + '\'' +
                '}';
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }
}
