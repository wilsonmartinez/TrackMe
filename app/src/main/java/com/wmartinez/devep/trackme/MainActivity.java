package com.wmartinez.devep.trackme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        findViewById(R.id.btn_play_again).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // We normally won't show the welcome slider again in real app
//                // but this is for testing
//                PreferencesManager prefManager = new PreferencesManager(getApplicationContext());
//
//                // make first time launch TRUE
//                prefManager.SetFirstTimeLaunch(true);
//
//                startActivity(new Intent(MainActivity.this, IntroActivity.class));
//                finish();
//            }
//        });

    }

    public void irMapa(View view){
//        double lat = 4.701468878372607;
//        double lng = -74.02946920540967;
//
        Intent intent = new Intent(this, TrackActivity.class);
//        intent.putExtra("latitud", lat);
//        intent.putExtra("longitud", lng);
        startActivity(intent);
    }
}
