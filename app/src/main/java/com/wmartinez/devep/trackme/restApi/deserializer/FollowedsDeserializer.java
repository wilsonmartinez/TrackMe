package com.wmartinez.devep.trackme.restApi.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.wmartinez.devep.trackme.pojo.Followed;
import com.wmartinez.devep.trackme.restApi.JsonKeys;
import com.wmartinez.devep.trackme.restApi.model.FollowedsResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by WilsonMartinez on 12/13/2016.
 */

public class FollowedsDeserializer implements JsonDeserializer {
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();

        FollowedsResponse response = gson.fromJson(json, FollowedsResponse.class);
        JsonArray jsonArray = json.getAsJsonObject().getAsJsonArray(JsonKeys.FOLLOWED);

        response.setFolloweds(deserializerFolloweds(jsonArray));
        return response;
    }

    private ArrayList<Followed> deserializerFolloweds(JsonArray jsonArray) {
        ArrayList<Followed> followeds = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            JsonObject followedInfo = jsonObject.getAsJsonObject();

            String nickname = followedInfo.get(JsonKeys.NICKNAME).getAsString();
            String email = followedInfo.get(JsonKeys.EMAIL).getAsString();
            String gender = followedInfo.get(JsonKeys.GENDER).getAsString();
            String activities = followedInfo.get(JsonKeys.ACTIVITIES).getAsString();
            String transportation = followedInfo.get(JsonKeys.TRANSPORTATION).getAsString();

            Followed followed = new Followed();
            followed.setNickname(nickname);
            followed.setEmail(email);
            followed.setGender(gender);
            followed.setActivities(activities);
            followed.setTransportation(transportation);

            followeds.add(followed);
        }
        return followeds;
    }
}
