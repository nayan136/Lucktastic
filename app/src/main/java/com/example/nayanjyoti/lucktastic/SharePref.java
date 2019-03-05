package com.example.nayanjyoti.lucktastic;

import android.content.Context;
import android.content.SharedPreferences;

import at.favre.lib.armadillo.Armadillo;

public class SharePref {

    private static SharePref sharePref = new SharePref();
    private static SharedPreferences preferences = null;
    private static final String preferenceName = "myPrefs";

    private SharePref(){}

    public static SharePref getInstance(Context context) {
        if (preferences == null) {
            preferences = Armadillo.create(context, preferenceName)
                    .encryptionFingerprint(context)
                    .build();
        }
        return sharePref;
    }

    public SharedPreferences getSharePref(){
        return preferences;
    }

    public void setEmail(String email){
        preferences.edit().putString(Data.SHARE_PREF_EMAIL,email).apply();
    }

    public String getEmail(){
        return preferences.getString(Data.SHARE_PREF_EMAIL,"");
    }

    public void setGoldCoin(int gold){
        preferences.edit().putInt(Data.SHARE_PREF_COIN,gold).apply();
    }

    public int getGoldCoin(){
        return preferences.getInt(Data.SHARE_PREF_COIN,0);
    }

    public void setLastLogin(String date){
        preferences.edit().putString(Data.SHARE_PREF_LAST_LOGIN,date).apply();
    }

    public String getLastLogin(){
        return preferences.getString(Data.SHARE_PREF_LAST_LOGIN,"");
    }

    public void setChanceLeft(int chanceleft){
        preferences.edit().putInt(Data.SHARE_PREF_CHANCE_LEFT,chanceleft).apply();
    }

    public int getChanceLeft(){
        return preferences.getInt(Data.SHARE_PREF_CHANCE_LEFT,0);
    }

}
