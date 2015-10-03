package com.rndapp.roostremote.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ell on 5/16/15.
 */
public class BoolOption extends Option{
    protected String name;
    protected boolean value;

    public BoolOption(String name, boolean value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public JSONObject getJsonObject(String key) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(key, value);
        return jsonObject;
    }

    public String getName() {
        return name;
    }

    public boolean getValue(){
        return value;
    }
}
