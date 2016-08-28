package com.rndapp.roostremote;

import android.content.Context;

/**
 * Created by ell on 5/15/15.
 */
public class Constants {
    public static final String DESCRIBER_SCHEME = "http://";
    public static final String HOST_SCHEME = "http://";

    public static final String USER_URL = "https://roost-remote-devices.herokuapp.com/api/v1/";

    public static final String CONTENT_TYPE_HEADER = "application/json";
    public static final String CONTENT_TYPE = "Content-type";
    public static final String API_KEY_HEADER = "X-API-Key";

    public static String getApiKey(Context context){
        return context.getSharedPreferences(API_KEY_HEADER, Context.MODE_PRIVATE).getString(API_KEY_HEADER, null);
    }

    public static void setApiKey(Context context, String apiKey){
        context.getSharedPreferences(API_KEY_HEADER, Context.MODE_PRIVATE).edit().putString(API_KEY_HEADER, apiKey).apply();
    }
}
