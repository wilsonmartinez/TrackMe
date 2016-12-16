package com.wmartinez.devep.trackme.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.wmartinez.devep.trackme.restApi.EndPoints;
import com.wmartinez.devep.trackme.restApi.adapter.RestApiAdapter;
import com.wmartinez.devep.trackme.restApi.model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by WilsonMartinez on 12/5/2016.
 */

public class UpdateLocationService extends Service implements LocationListener {

    public static boolean status = false;
    public static Handler handler;
    private static double latitude, longitude, timestamp;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 30000; // 10 sec
    private static int FASTEST_INTERVAL = 15000; // 5 sec
    private static int DISPLACEMENT = 20; // 10 meters
    private Context context = null;

    public UpdateLocationService() {
    }

    public void onCreate() {
        handler = new Handler();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        context = this;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return START_NOT_STICKY;
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, FASTEST_INTERVAL, DISPLACEMENT, this);
            //buildLocationRequest();
            return START_STICKY;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        status = false;
        stopSelf();
        handler.removeCallbacksAndMessages(null);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        timestamp = location.getTime();
        saveLocation();

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void saveLocation() {
        SharedPreferences preferencesAccount = getSharedPreferences("account", Context.MODE_PRIVATE);
        String userAccount = preferencesAccount.getString("edit_account", null);
        if (userAccount != null) {
            RestApiAdapter adapter = new RestApiAdapter();
            EndPoints endPoints = adapter.setConnectionRestAPI();
            Call<UserResponse> responseCall = endPoints.setUserLocation(userAccount, latitude, longitude, timestamp);
            responseCall.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    UserResponse userResponse = response.body();
                    //Log.d("LATITUDE", String.valueOf(userResponse.getLatitude()));
                    //Log.d("LONGITUDE", String.valueOf(userResponse.getLongitude()));
                    //Log.d("TIMESTAMP", String.valueOf(userResponse.getTimestamp()));
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {

                }
            });
        }
    }
}
