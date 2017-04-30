package com.example.dara.wikiplant;

/**
 * Created by pandawarrior91 on 29/04/2017.
 * Android Studio 2.2
 */

public class PlantImage {
    private String name;
    private String url;
    private boolean main;

    public PlantImage() {
    }

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
        return main;
    }

    @Override
    public String toString() {
        return "PlantImage{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", main=" + main +
                '}';
    }
}
