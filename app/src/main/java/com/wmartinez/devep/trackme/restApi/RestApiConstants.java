package com.wmartinez.devep.trackme.restApi;

/**
 * Created by WilsonMartinez on 11/24/2016.
 */

public final class RestApiConstants {
    public static final String ROOT_URL = "https://apptrackme.herokuapp.com/";
    public static final String KEY_POST_ID_TOKEN = "record_device/";
    public static final String KEY_SET_USER_LOCATION = "locations/";
    public static final String KEY_GET_USER_LOCATION = "locations/{userSelected}";
    //public static final String KEY_GET_LAST_USER_LACATION = "locations/{userSelected}";
    public static final String KEY_SET_FOLLOWEDS = "add_followeds/";
    public static final String KEY_GET_FOLLOWEDS = "record_device/{device_id}/{user_name}/followeds";
}
