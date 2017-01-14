package com.rndapp.roostremote.models;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ell on 5/17/15.
 */
public abstract class Option {
    public abstract String getName();
    public abstract Object getValue();
    public abstract JSONObject getJsonObject(String key) throws JSONException;

    protected static void addStaticValues(JSONObject jsonObject, List<Option> staticValues) {
        if (jsonObject != null) {
            if (staticValues != null) {
                try {
                    for (Option value : staticValues){
                        jsonObject.put(value.getName(), value.getValue());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class OptionDeserializer implements JsonDeserializer<Option> {

        @Override
        public Option deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            return tryIntOption(jsonObject);
        }

        private Option tryIntOption(JsonObject jsonObject) {
            String name = jsonObject.get("name").getAsString();
            try{
                String intString = jsonObject.get("value").getAsString();
                int intValue = Integer.parseInt(intString);
                return new IntOption(name, intValue);
            }catch (NumberFormatException e) {
                return tryStringOption(jsonObject);
            }catch (UnsupportedOperationException ue) {
                return tryMapOption(jsonObject);
            }
        }

        private Option tryMapOption(JsonObject jsonObject) {
            String name = jsonObject.get("name").getAsString();
            Map<String, Object> map = new Gson().fromJson(jsonObject.get("value").toString(),
                    new TypeToken<HashMap<String, Object>>() {}.getType());
            if (map == null){
                return tryStringOption(jsonObject);
            }
            return new MapOption(name, map);
        }

        private Option tryStringOption(JsonObject jsonObject){
            String name = jsonObject.get("name").getAsString();
            try {
                String stringValue = jsonObject.get("value").getAsString();
                return new StringOption(name, stringValue);
            }catch (Exception e2){
                e2.printStackTrace();
                return tryBoolOption(jsonObject);
            }
        }

        private Option tryBoolOption(JsonObject jsonObject) {
            String name = jsonObject.get("name").getAsString();
            try {
                boolean boolValue = jsonObject.get("value").getAsBoolean();
                return new BoolOption(name, boolValue);
            }catch (Exception e1){
                return tryStringOption(jsonObject);
            }
        }

    }
}
