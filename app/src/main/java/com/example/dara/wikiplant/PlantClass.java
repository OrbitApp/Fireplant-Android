package com.example.dara.wikiplant;

import android.app.LauncherActivity;

import java.util.ArrayList;

/**
 * Created by Dara on 29/04/2017.
 */

public class PlantClass  {
    private String name;

    public PlantClass(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public PlantClass() {
    }
    private String description;


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
                '}';
    }
}
