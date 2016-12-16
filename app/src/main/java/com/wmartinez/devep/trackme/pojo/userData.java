package com.wmartinez.devep.trackme.pojo;

/**
 * Created by WilsonMartinez on 12/1/2016.
 */

public class UserData {
    private String idAutogenerado;
    private String device_id;
    private String user_name;
    private double latitude;
    private double longitude;
    private double timestamp;

    public UserData(String device_id, String user_name) {
        this.device_id = device_id;
        this.user_name = user_name;
    }

    public UserData(String idAutogenerado, String device_id, String user_name) {
        this.idAutogenerado = idAutogenerado;
        this.device_id = device_id;
        this.user_name = user_name;
    }

    public UserData(int latitude, int longitude, int timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public UserData(String user_name, int latitude, int longitude, int timestamp) {
        this.user_name = user_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public UserData() {
    }

    public String getIdAutogenerado() {
        return idAutogenerado;
    }

    public void setIdAutogenerado(String idAutogenerado) {
        this.idAutogenerado = idAutogenerado;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }
}
