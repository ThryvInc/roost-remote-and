package com.rndapp.roostremote;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.rndapp.roostremote.models.Device;
import com.rndapp.roostremote.models.Place;

/**
 * Created by ell on 1/10/16.
 */
public class RoostRemoteApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Place.class);
        ParseObject.registerSubclass(Device.class);
        Parse.initialize(this, "hTpkQ62vQCT1GnCpFzWbg5afa2K64mhVvAknLByG", "NJRxVMxgov1okiu3U5rx89bGHYtYJYXQoMf9ZzQf");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
