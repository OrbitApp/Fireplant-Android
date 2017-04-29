package com.example.dara.wikiplant;

/**
 * Created by pandawarrior91 on 30/04/2017.
 * Android Studio 2.2
 */

class PlantUnit {
    private String longitude;
    private String latitude;
    private String url;

    public PlantUnit() {
    }


    public PlantUnit(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "PlantUnit{" +
                "longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
