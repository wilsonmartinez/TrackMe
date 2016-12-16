package com.wmartinez.devep.trackme.restApi.model;

import com.wmartinez.devep.trackme.pojo.Followed;

import java.util.ArrayList;

/**
 * Created by WilsonMartinez on 12/13/2016.
 */

public class FollowedsResponse {

    ArrayList<Followed> followeds;

    public ArrayList<Followed> getFolloweds() {
        return followeds;
    }

    public void setFolloweds(ArrayList<Followed> followeds) {
        this.followeds = followeds;
    }
}
