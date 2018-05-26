package com.example.jjw26.parkingpal;

/**
 * Created by jjw26 on 12/2/2017.
 */

public class Spot {
    private String id;
    private String lat;
    private String lon;
    private String date;
    private String active;


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setLatitude(String lat) {
        this.lat = lat;
    }

    public String getLatitude() {
        return this.lat;
    }

    public void setLongitude(String lon) {
        this.lon = lon;
    }

    public String getLongitude() {
        return this.lon;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    public void setActive(String active){
        this.active = active;
    }

    public String getActive() {
        return this.active;
    }
}
