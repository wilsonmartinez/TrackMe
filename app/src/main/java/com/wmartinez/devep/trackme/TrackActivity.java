package com.wmartinez.devep.trackme;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.wmartinez.devep.trackme.pojo.UserData;
import com.wmartinez.devep.trackme.restApi.EndPoints;
import com.wmartinez.devep.trackme.restApi.adapter.RestApiAdapter;
import com.wmartinez.devep.trackme.restApi.model.UserLocationResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    protected static final String TAG = "TrackActivity";
    //Request code for location permission request.
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    // Variables to management of the Followed data.
    private static String nickname;
    private static String email;
    private static ArrayList<UserData> userDatas, datasLocation, arrayLastLocation;
    private static PolylineOptions polylineOptions;
    private static GoogleMap mMap;
    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;
    private FloatingActionButton btnDrawPath;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //Flag indicating whether a requested permission has been denied after returning in
    private boolean mPermissionDenied = false;
    private Marker mCurrLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        //buildGoogleApiClient();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        userDatas = new ArrayList<>();
        polylineOptions = new PolylineOptions();
        btnDrawPath = (FloatingActionButton) findViewById(R.id.btn_draw_his_path);

        Intent intent = getIntent();
        nickname = intent.getStringExtra("user_name");
        email = intent.getStringExtra("email");
        if (email != null) {
            email = email.replace('.', '_');
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                //TODO:
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                //(just doing it here for now, note that with this code, no explanation is shown)
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
            return false;
        }else {
            return true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop locations update when activity is no longer active
        // Maybe this is a mistake at final
//        if (mGoogleApiClient != null){
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    public void onStart() {
        super.onStart();
        //mGoogleApiClient.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                // If request is cancelled, the result arrays is empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Permission was granted!
                    // Do the location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED){
                        if (mGoogleApiClient ==null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }else {
                    // Permission denied!
                    // Disable the functionality that depends on the permission.
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // Other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        // Initialize the Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

//        SharedPreferences preferencesAccount = getSharedPreferences("account", Context.MODE_PRIVATE);
//        String userAccount = preferencesAccount.getString("edit_account", null);
//        if (userAccount != null){
        if (email != null) {
            RestApiAdapter adapter = new RestApiAdapter();
            Gson gsonLastLocation = adapter.buildGsonDeserializerLastLocation();
            EndPoints pointsLastLocation = adapter.setConnectionRestAPIHeroku(gsonLastLocation);

            Call<UserLocationResponse> callLastLocation = pointsLastLocation.getUserLocation(email);
            callLastLocation.enqueue(new Callback<UserLocationResponse>() {
                @Override
                public void onResponse(Call<UserLocationResponse> call, Response<UserLocationResponse> response) {
                    UserLocationResponse lastLocation = response.body();
                    if (lastLocation.getUserData() != null) {
                        arrayLastLocation = lastLocation.getUserData();
                        LatLng latLng = new LatLng(arrayLastLocation.get(0).getLatitude(), arrayLastLocation.get(0).getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(getString(R.string.marker_last_location));
                        markerOptions.icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                        mCurrLocationMarker = mMap.addMarker(markerOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                        // Define Map Style ... depend the hour day...
                        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.style_json);
                        try {
                            // Customise the styling of the base map using a JSON object defined
                            // in a raw resource file.
                            boolean success = mMap.setMapStyle(
                                    MapStyleOptions.loadRawResourceStyle(
                                            getApplicationContext(), R.raw.style_json));

                            if (!success) {
                                Log.e("MapsActivityRaw", "Style parsing failed.");
                            }
                        } catch (Resources.NotFoundException e) {
                            Log.e("MapsActivityRaw", "Can't find style.", e);
                        }
                        mMap.setMapStyle(style);
                    }
                }

                @Override
                public void onFailure(Call<UserLocationResponse> call, Throwable t) {

                }
            });
        }

        btnDrawPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferencesAccount = getSharedPreferences("account", Context.MODE_PRIVATE);
                String userAccount = preferencesAccount.getString("edit_account", null);
                if (email != null) {
                    RestApiAdapter restApiAdapter = new RestApiAdapter();
                    Gson gsonLocations = restApiAdapter.buildGsonDeserializerUserLocations();
                    EndPoints endPoints = restApiAdapter.setConnectionRestAPIHeroku(gsonLocations);

                    Call<UserLocationResponse> responseCall = endPoints.getUserLocation(email);
                    responseCall.enqueue(new Callback<UserLocationResponse>() {
                        @Override
                        public void onResponse(Call<UserLocationResponse> call, Response<UserLocationResponse> response) {
                            UserLocationResponse locationResponse = response.body();
                            if (locationResponse.getUserData() != null) {
                                datasLocation = locationResponse.getUserData();
                                userDatas.addAll(datasLocation);
                                for (int i = 0; i < userDatas.size(); i++) {
                                    // Make a polyline
                                    polylineOptions.add(new LatLng(userDatas.get(i).getLatitude(), userDatas.get(i).getLongitude()));
                                    // Place current location marker
                                }
                                polylineOptions.clickable(true);
                                polylineOptions.geodesic(true);
                                polylineOptions.color(-16800000);
                                polylineOptions.width(20);
                                mMap.addPolyline(polylineOptions);
                                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                                // Define Map Style ... depend the hour day...
                                MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.style_json);
                                try {
                                    // Customise the styling of the base map using a JSON object defined
                                    // in a raw resource file.
                                    boolean success = mMap.setMapStyle(
                                            MapStyleOptions.loadRawResourceStyle(
                                                    getApplicationContext(), R.raw.style_json));

                                    if (!success) {
                                        Log.e("MapsActivityRaw", "Style parsing failed.");
                                    }
                                } catch (Resources.NotFoundException e) {
                                    Log.e("MapsActivityRaw", "Can't find style.", e);
                                }
                                mMap.setMapStyle(style);
                            }
                        }

                        @Override
                        public void onFailure(Call<UserLocationResponse> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //Toast.makeText(this, "My Location button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100);
        mLocationRequest.setFastestInterval(100);
        // Test with other priorities
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

}
