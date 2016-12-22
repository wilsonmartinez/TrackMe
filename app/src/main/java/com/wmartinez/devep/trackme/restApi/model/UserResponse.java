package com.wmartinez.devep.trackme.restApi.model;

/**
 * Created by WilsonMartinez on 11/24/2016.
 * Data model to the response
 */

public class UserResponse {
    private String idAutogenerado;
    private String device_id;
    private String user_name;
    private double latitude;
    private double longitude;
    private double timestamp;

    public UserResponse(String idAutogenerado, String device_id, String user_name) {
        this.idAutogenerado = idAutogenerado;
        this.device_id = device_id;
        this.user_name = user_name;
    }

    public UserResponse(String user_name, double latitude, double longitude, double timestamp) {
        this.user_name = user_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public UserResponse() {
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

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
