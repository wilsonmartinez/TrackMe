package com.wmartinez.devep.trackme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.wmartinez.devep.trackme.Adapter.FollowedAdapter;
import com.wmartinez.devep.trackme.pojo.Followed;
import com.wmartinez.devep.trackme.restApi.EndPoints;
import com.wmartinez.devep.trackme.restApi.adapter.RestApiAdapter;
import com.wmartinez.devep.trackme.restApi.model.FollowedsResponse;
import com.wmartinez.devep.trackme.service.UpdateLocationService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static MenuInflater menuInflater;
    private static boolean bStart;
    private static Menu mainMenu;
    private static String device_id, user_name;
    ArrayList<Followed> followeds, followedArrayList;
    private Toolbar toolbar;
    private Context context;
    private RecyclerView recyclerView;
    private FloatingActionButton addActionButton;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        followeds = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        device_id = new String();
        user_name = new String();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        addActionButton = (FloatingActionButton) findViewById(R.id.fabAddUser);
        recyclerView = (RecyclerView) findViewById(R.id.rvUsers);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        user_name = preferences.getString("edit_account", null);
        device_id = preferences.getString("device_id", null);
        // Define a layout for RecyclerView
        layoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(layoutManager);
        // Set a click listener for add item button
        addActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDialog(view);
            }
        });
        loadFolloweds();
    }

    public void makeDialog(View view) {

        // Build an Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_followed, null);
        // Specify  alert dialog is not cancelable/ignorable
        builder.setCancelable(false);
        // Set the custom layout as alert dialog view
        builder.setView(dialogView);

        final EditText textNickname = (EditText) dialogView.findViewById(R.id.dialog_nickname);
        final EditText textEmail = (EditText) dialogView.findViewById(R.id.dialog_email);
        final Spinner spinnerGender = (Spinner) dialogView.findViewById(R.id.spinner_gender);
        final Spinner spinnerActivities = (Spinner) dialogView.findViewById(R.id.spinner_activities);
        final Spinner spinnerTransportation = (Spinner) dialogView.findViewById(R.id.spinner_transportation);

        Button posButton = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
        final Button negButton = (Button) dialogView.findViewById(R.id.dialog_negative_btn);

        final AlertDialog dialog = builder.create();

        posButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!textNickname.getText().toString().equals("")
                        && !textEmail.getText().toString().equals("")) {
                    String nickname = textNickname.getText().toString();
                    String email = textEmail.getText().toString();
                    //email = email.replace('.', '_');
                    String gender = String.valueOf(spinnerGender.getSelectedItem());
                    String activities = String.valueOf(spinnerActivities.getSelectedItem());
                    String transportation = String.valueOf(spinnerTransportation.getSelectedItem());
                    Followed addFollowed = new Followed();
                    addFollowed.setNickname(nickname);
                    addFollowed.setEmail(email);
                    addFollowed.setGender(gender);
                    addFollowed.setActivities(activities);
                    addFollowed.setTransportation(transportation);
                    // Save a new followed in firebase
                    saveNewFollowed(addFollowed);

                    followeds.add(addFollowed);
                    // Initialize a new instance of RecyclerView Adapter instance
                    adapter = new FollowedAdapter(followeds, MainActivity.this);
                    // Set the adapter for RecyclerView
                    recyclerView.setAdapter(adapter);

                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplication(),
                            "You need fill all lines", Toast.LENGTH_SHORT).show();
                }
            }
        });

        negButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
//                Toast.makeText(getApplication(),
//                        "No button clicked", Toast.LENGTH_SHORT).show();
            }
        });
        //// Display the custom alert dialog on interface
        dialog.show();

    }

    private void loadFolloweds() {
        SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        user_name = preferences.getString("edit_account", null);
        device_id = preferences.getString("device_id", null);

        RestApiAdapter adapter = new RestApiAdapter();

        Gson gsonFolloweds = adapter.buildGsonDeserializerFolloweds();
        EndPoints endPoints = adapter.setConnectionRestAPIHeroku(gsonFolloweds);

        Call<FollowedsResponse> responseCall = endPoints.getFolloweds(device_id, user_name);
        responseCall.enqueue(new Callback<FollowedsResponse>() {
            @Override
            public void onResponse(Call<FollowedsResponse> call, Response<FollowedsResponse> response) {
                FollowedsResponse followedsResponse = response.body();
                if (followedsResponse.getFolloweds() != null) {
                    followedArrayList = followedsResponse.getFolloweds();
                    followeds.addAll(followedArrayList);
                    showFolloweds();
                } else {
                    Toast.makeText(getApplication(),
                            "You don't have any Followed, Please add one!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<FollowedsResponse> call, Throwable t) {

            }
        });
    }

    private void saveNewFollowed(Followed followed) {
        SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        user_name = preferences.getString("edit_account", null);
        device_id = preferences.getString("device_id", null);
        RestApiAdapter adapter = new RestApiAdapter();
        EndPoints endPoints = adapter.setConnectionRestAPI();
        Call<Followed> responseCall = endPoints.setFolloweds(device_id, user_name, followed.getNickname(), followed.getEmail(),
                followed.getGender(), followed.getActivities(), followed.getTransportation());
        responseCall.enqueue(new Callback<Followed>() {
            @Override
            public void onResponse(Call<Followed> call, Response<Followed> response) {
                Followed followedResponse = response.body();
            }

            @Override
            public void onFailure(Call<Followed> call, Throwable t) {

            }
        });
    }

    public void showFolloweds() {
        setAdapterRecicleView(makeFollowedsAdapter(followeds));
        setGridLayout();
    }

    public void setAdapterRecicleView(FollowedAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    public FollowedAdapter makeFollowedsAdapter(ArrayList<Followed> followeds) {
        FollowedAdapter adapter = new FollowedAdapter(followeds, this);
        return adapter;
    }

    public void setGridLayout() {
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main_action_bar, menu);
//        SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
//        bStart = preferences.getBoolean("bStart", false);
//        if (bStart){
//            menu.findItem(R.id.action_start).setTitle(R.string.menu_start);
//        } else {
//            menu.findItem(R.id.action_start).setTitle(R.string.menu_stop);
//        }
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        //menu.clear();
//        SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
//        bStart = preferences.getBoolean("bStart", false);
//        if (bStart){
//            menu.findItem(R.id.action_start).setTitle(R.string.menu_start);
//        } else {
//            menu.findItem(R.id.action_start).setTitle(R.string.menu_stop);
//        }
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_start:
                SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
                bStart = preferences.getBoolean("bStart", false);
                if (!bStart) {
                    SharedPreferences preferencesAccount = getSharedPreferences("account", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorAccount = preferencesAccount.edit();
                    editorAccount.putBoolean("bStart", true);
                    if (editorAccount.commit()) {
                        Intent intentUpdateLocationService = new Intent(this, UpdateLocationService.class);
                        startService(intentUpdateLocationService);
                        item.setTitle(R.string.menu_stop);
                    }
                } else {

                    SharedPreferences preferencesAccount = getSharedPreferences("account", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorAccount = preferencesAccount.edit();
                    editorAccount.putBoolean("bStart", false);
                    if (editorAccount.commit()) {
                        Intent intentUpdateLocationService = new Intent(this, UpdateLocationService.class);
                        stopService(intentUpdateLocationService);
                        item.setTitle(R.string.menu_start);
                    }
                }
                return super.onOptionsItemSelected(item);
            case R.id.action_change_account:
                return super.onOptionsItemSelected(item);
            case R.id.action_settings:
                return super.onOptionsItemSelected(item);
            case R.id.action_sign_out:
                signOut();
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void signOut() {
        auth.signOut();

        // Launch login activity
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();

    }

}
