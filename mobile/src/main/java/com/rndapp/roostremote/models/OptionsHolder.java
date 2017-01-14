package com.rndapp.roostremote.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ell on 5/15/15.
 */
public class OptionsHolder {
    protected String key;
    @SerializedName("static_values")
    protected ArrayList<Option> staticValues;
    protected ArrayList<Option> values;

    public String getKey() {
        return key;
    }

    public ArrayList<Option> getValues() {
        return values;
    }

    public ArrayList<Option> getStaticValues() {
        return staticValues;
    }
}
