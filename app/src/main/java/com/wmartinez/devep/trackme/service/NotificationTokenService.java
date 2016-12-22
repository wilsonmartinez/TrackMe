package com.wmartinez.devep.trackme.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by WilsonMartinez on 11/24/2016.
 */

public class NotificationTokenService extends FirebaseInstanceIdService {
    private static final String TAG = "FIREBASE_TOKEN";

    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        sendRegisterToken(token);
    }

    private void sendRegisterToken(String token) {
        Log.d(TAG, token);
    }
}
