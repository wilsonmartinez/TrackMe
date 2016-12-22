package com.wmartinez.devep.trackme.restApi;

import com.wmartinez.devep.trackme.pojo.Followed;
import com.wmartinez.devep.trackme.restApi.model.FollowedsResponse;
import com.wmartinez.devep.trackme.restApi.model.UserLocationResponse;
import com.wmartinez.devep.trackme.restApi.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by WilsonMartinez on 11/24/2016.
 */

public interface EndPoints {

    @FormUrlEncoded
    @POST(RestApiConstants.KEY_POST_ID_TOKEN)
    Call<UserResponse> recordDevice(@Field("device_id") String device_id,
                                    @Field("user_name") String user_name);

    @FormUrlEncoded
    @POST(RestApiConstants.KEY_SET_USER_LOCATION)
    Call<UserResponse> setUserLocation(@Field("user_name") String user_name,
                                       @Field("latitude") double latitude,
                                       @Field("longitude") double longitude,
                                       @Field("timestamp") double timestamp);

    @GET(RestApiConstants.KEY_GET_USER_LOCATION)
    Call<UserLocationResponse> getUserLocation(
            @Path("userSelected") String userSelected
    );

    @FormUrlEncoded
    @POST(RestApiConstants.KEY_SET_FOLLOWEDS)
    Call<Followed> setFolloweds(@Field("device_id") String device_id,
                                @Field("user_name") String user_name,
                                @Field("nickname") String nickname,
                                @Field("email") String email,
                                @Field("gender") String gender,
                                @Field("activities") String activities,
                                @Field("transportation") String transportation);

    @GET(RestApiConstants.KEY_GET_FOLLOWEDS)
    Call<FollowedsResponse> getFolloweds(
            @Path("device_id") String device_id,
            @Path("user_name") String user_name
    );

}