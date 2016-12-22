package com.wmartinez.devep.trackme.restApi.adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wmartinez.devep.trackme.restApi.EndPoints;
import com.wmartinez.devep.trackme.restApi.RestApiConstants;
import com.wmartinez.devep.trackme.restApi.deserializer.FollowedsDeserializer;
import com.wmartinez.devep.trackme.restApi.deserializer.LastLocationDeserializer;
import com.wmartinez.devep.trackme.restApi.deserializer.UserLocationDeserializer;
import com.wmartinez.devep.trackme.restApi.model.FollowedsResponse;
import com.wmartinez.devep.trackme.restApi.model.UserLocationResponse;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by WilsonMartinez on 11/24/2016.
 */

public class RestApiAdapter {

    public EndPoints setConnectionRestAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestApiConstants.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(EndPoints.class);
    }

    public EndPoints setConnectionRestAPIHeroku(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestApiConstants.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(EndPoints.class);
    }

    public Gson buildGsonDeserializerUserLocations() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(UserLocationResponse.class, new UserLocationDeserializer());
        return gsonBuilder.create();
    }

    public Gson buildGsonDeserializerLastLocation() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(UserLocationResponse.class, new LastLocationDeserializer());
        return gsonBuilder.create();
    }

    public Gson buildGsonDeserializerFolloweds() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(FollowedsResponse.class, new FollowedsDeserializer());
        return gsonBuilder.create();
    }
}
