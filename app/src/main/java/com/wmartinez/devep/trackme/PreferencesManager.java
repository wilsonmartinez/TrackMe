package com.wmartinez.devep.trackme;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by WilsonMartinez on 11/4/2016.
 */

public class PreferencesManager {
    //Shared Preferences File Name
    private static final String PREFERENCES_INTRO = "preferences-intro";
    private static final String PREFERENCES_LOGIN = "preferences-login";
    private static final String FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_LOGIN = "IsLogin";
    SharedPreferences preferences_intro, preferences_login;
    SharedPreferences.Editor editor_intro, editor_login;
    Context gContext;
    //Shared Preferences Mode
    int PRIVATE_MODE = 0;

    public PreferencesManager(Context context){
        this.gContext = context;
        preferences_intro = gContext.getSharedPreferences(PREFERENCES_INTRO, PRIVATE_MODE);
        editor_intro = preferences_intro.edit();

        preferences_login = gContext.getSharedPreferences(PREFERENCES_LOGIN, PRIVATE_MODE);
        editor_login = preferences_login.edit();
    }

    public void SetFirstTimeLaunch(boolean firstTime){
        editor_intro.putBoolean(FIRST_TIME_LAUNCH, firstTime);
        editor_intro.commit();
    }

    public boolean IsFirstTimeLaunch(){
        return preferences_intro.getBoolean(FIRST_TIME_LAUNCH, true);
    }

    public void SetLogin(boolean login) {
        editor_login.putBoolean(IS_LOGIN, login);
        editor_login.commit();
    }

    public boolean IsLogin() {
        return preferences_login.getBoolean(IS_LOGIN, true);
    }
}