package com.example.dara.wikiplant;

import com.google.firebase.database.PropertyName;

import java.util.ArrayList;

/**
 * Created by pandawarrior91 on 29/04/2017.
 * Android Studio 2.2
 */

public class Plant {
    private String name;
    private String status;
    private String genus;
    private String family;
    private String description;
    @PropertyName("images")
    private ArrayList<PlantImage> images;
    private ArrayList<PlantUnit> units;
    private int mImageUrl;

    public Plant(String name, String status, String genus, String family, String description,
                 ArrayList<PlantImage> plantImages, ArrayList<PlantUnit> plantUnits) {
        this.name = name;
        this.status = status;
        this.genus = genus;
        this.family = family;
        this.description = description;
        this.images = plantImages;
        this.units = plantUnits;
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

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public ArrayList<PlantImage> getImages() {
        return images;
    }

    public void setImages(ArrayList<PlantImage> images) {
        this.images = images;
    }

    public ArrayList<PlantUnit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<PlantUnit> units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return "Plant{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", genus='" + genus + '\'' +
                ", family='" + family + '\'' +
                ", description='" + description + '\'' +
                ", images=" + images +
                ", units=" + units +
                '}';
    }

    public String getImageUrl() {
        if (images != null) {
            for (PlantImage image :
                    images) {
                if (image.isMain()) {
                    return image.getUrl();
                }
            }
        }
        return "https://firebasestorage.googleapis.com/v0/b/fireplant-wiki.appspot.com" +
                "/o/plants%2Fsf1.jpg?alt=media&token=9a8ac4e8-d167-4619-94a3-c9d9d970261c";
    }
}
