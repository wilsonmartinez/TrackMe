package com.wmartinez.devep.trackme.restApi.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.wmartinez.devep.trackme.pojo.UserData;
import com.wmartinez.devep.trackme.restApi.JsonKeys;
import com.wmartinez.devep.trackme.restApi.model.UserLocationResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by WilsonMartinez on 12/5/2016.
 */

public class LastLocationDeserializer implements JsonDeserializer {
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        UserLocationResponse response = gson.fromJson(json, UserLocationResponse.class);
        JsonArray jsonArray = json.getAsJsonObject().getAsJsonArray(JsonKeys.LOCATION);

        response.setUserData(deserializerLastLocationJson(jsonArray));
        return response;
    }

    private ArrayList<UserData> deserializerLastLocationJson(JsonArray jsonArray) {
        ArrayList<UserData> lastLocation = new ArrayList<>();
        int length;
        length = jsonArray.size();
        JsonObject jsonObject = jsonArray.get(length - 1).getAsJsonObject();
        JsonObject locationObject = jsonObject.getAsJsonObject();

        double latitude = locationObject.get(JsonKeys.LATITUDE).getAsDouble();
        double longitude = locationObject.get(JsonKeys.LONGITUDE).getAsDouble();
        double timestamp = locationObject.get(JsonKeys.TIMESTAMP).getAsDouble();

        UserData lastLocationData = new UserData();
        lastLocationData.setLatitude(latitude);
        lastLocationData.setLongitude(longitude);
        lastLocationData.setTimestamp(timestamp);

        lastLocation.add(lastLocationData);
        return lastLocation;
    }
}
