package com.rndapp.roostremote;

import android.app.Application;

import com.rndapp.roostremote.api_calls.VolleyManager;

/**
 * Created by ell on 1/10/16.
 */
public class RoostRemoteApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        VolleyManager.Companion.init(this);
    }
}
