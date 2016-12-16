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
 * Created by WilsonMartinez on 12/1/2016.
 */

public class UserLocationDeserializer implements JsonDeserializer {
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        UserLocationResponse response = gson.fromJson(json, UserLocationResponse.class);
        JsonArray jsonArray = json.getAsJsonObject().getAsJsonArray(JsonKeys.LOCATION);

        response.setUserData(deserializerUserLocationJson(jsonArray));
        return response;
    }

    private ArrayList<UserData> deserializerUserLocationJson(JsonArray jsonArray) {
        ArrayList<UserData> userDatas = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

            JsonObject locationInfo = jsonObject.getAsJsonObject();
            double latitude = locationInfo.get(JsonKeys.LATITUDE).getAsDouble();
            double longitude = locationInfo.get(JsonKeys.LONGITUDE).getAsDouble();
            double timestamp = locationInfo.get(JsonKeys.TIMESTAMP).getAsDouble();

            UserData userDataUpdate = new UserData();
            userDataUpdate.setLatitude(latitude);
            userDataUpdate.setLongitude(longitude);
            userDataUpdate.setTimestamp(timestamp);

            userDatas.add(userDataUpdate);
        }
        return userDatas;
    }

}
