package com.wmartinez.devep.trackme.restApi.model;

import com.wmartinez.devep.trackme.pojo.UserData;

import java.util.ArrayList;

/**
 * Created by WilsonMartinez on 12/1/2016.
 */

public class UserLocationResponse {
    ArrayList<UserData> userDatas;

    public ArrayList<UserData> getUserData() {
        return userDatas;
    }

    public void setUserData(ArrayList<UserData> userDatas) {
        this.userDatas = userDatas;
    }
}
