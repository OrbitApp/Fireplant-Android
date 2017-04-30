package com.example.dara.wikiplant;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by pandawarrior91 on 29/04/2017.
 * Android Studio 2.2
 */

public class Plant {
    private String name;
    private String status;
    private String genus;
    private String taxonomy;
    private String description;
    private HashMap<String, PlantImage> images;

    public Plant() {
    }

    public Plant(String name, String status, String genus, String taxonomy, String description) {
        this.name = name;
        this.status = status;
        this.genus = genus;
        this.taxonomy = taxonomy;
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    public HashMap<String, PlantImage> getImages() {
        return images;
    }

    public String getImageUrl(){
        Iterator it = images.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();
            PlantImage plantImage = (PlantImage) pair.getValue();
            if (plantImage != null && plantImage.isMain()){
                return plantImage.getUrl();

            }

            it.remove();
        }
        return null;
    }

    public void setImages(HashMap<String, PlantImage> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Plant{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", genus='" + genus + '\'' +
                ", taxonomy='" + taxonomy + '\'' +
                ", description='" + description + '\'' +
                ", images=" + images +
                '}';
    }
}
