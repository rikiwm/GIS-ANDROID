package com.example.kedaikopi.Model;

public class DataMaps {
    private double latitude, longitude;
    private String name;
    private String id;

    public DataMaps(){
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString (){

        return ""+id+"\n"+
                ""+name+"\n"+
                ""+latitude+"\n"+
                ""+longitude;
    }
    public DataMaps( String idd,String nm,double la,double lo){
        id=idd;
        name=nm;
        latitude=la;
        longitude=lo;
    }

}