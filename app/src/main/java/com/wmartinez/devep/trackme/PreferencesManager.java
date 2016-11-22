package com.wmartinez.devep.trackme;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by WilsonMartinez on 11/4/2016.
 */

public class PreferencesManager {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context gContext;
    //Shared Preferences Mode
    int PRIVATE_MODE = 0;
    //Shared Preferences File Name
    private static final String PREFERENCES_NAME = "preferences-welcome";
    private static final String FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String SIGNUP_AND_LOGIN = "IsSignupAndLogin";

    public PreferencesManager(Context context){
        this.gContext = context;
        preferences = gContext.getSharedPreferences(PREFERENCES_NAME, PRIVATE_MODE);
        editor =  preferences.edit();
    }

    public void SetFirstTimeLaunch(boolean firstTime){
        editor.putBoolean(FIRST_TIME_LAUNCH, firstTime);
        editor.commit();
    }

    public boolean IsFirstTimeLaunch(){
        return preferences.getBoolean(FIRST_TIME_LAUNCH, true);
    }

    public void SetIsSignupAndLogin(boolean login){
        editor.putBoolean(SIGNUP_AND_LOGIN, login);
        editor.commit();
    }

    public boolean IsSignupAndLogin(){
        return preferences.getBoolean(SIGNUP_AND_LOGIN, true);
    }
}