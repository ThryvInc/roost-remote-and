package com.rndapp.roostremote.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by ell on 5/17/15.
 */
public abstract class Option {
    public abstract String getName();
    public abstract JSONObject getJsonObject(String key) throws JSONException;

    public static class OptionDeserializer implements JsonDeserializer<Option> {

        @Override
        public Option deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

            JsonObject jsonObject = json.getAsJsonObject();

            String name = jsonObject.get("name").getAsString();

            try{
                String intString = jsonObject.get("value").getAsString();
                int intValue = Integer.parseInt(intString);
                return new IntOption(name, intValue);
            }catch (NumberFormatException e) {
                try {
                    boolean boolValue = jsonObject.get("value").getAsBoolean();
                    return new BoolOption(name, boolValue);
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }

            return null;

        }

    }
}
