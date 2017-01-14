package com.rndapp.roostremote.models;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by ell on 1/4/17.
 */

public class MapOption extends Option {
    protected String name;
    protected Map value;

    public MapOption(String name, Map value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public JSONObject getJsonObject(String key) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(key, new JSONObject(new Gson().toJson(value)));
        return jsonObject;
    }

    @Override
    public String getName() {
        return name;
    }

    public Map getValue() {
        return value;
    }
}
