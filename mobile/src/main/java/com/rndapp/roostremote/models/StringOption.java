package com.rndapp.roostremote.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ell on 1/3/17.
 */

public class StringOption extends Option {
    protected String name;
    protected String value;

    public StringOption(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public JSONObject getJsonObject(String key) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(key, value);
        return jsonObject;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
