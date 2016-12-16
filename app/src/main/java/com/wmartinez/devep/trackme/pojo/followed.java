package com.wmartinez.devep.trackme.pojo;

/**
 * Created by WilsonMartinez on 12/13/2016.
 */

public class Followed {
    private String nickname;
    private String email;
    private String gender;
    private String activities;
    private String transportation;

    public Followed() {

    }

    public Followed(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public Followed(String nickname, String email, String gender, String activities, String transportation) {
        this.nickname = nickname;
        this.email = email;
        this.gender = gender;
        this.activities = activities;
        this.transportation = transportation;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }
}
