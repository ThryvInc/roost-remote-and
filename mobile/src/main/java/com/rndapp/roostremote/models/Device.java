package com.rndapp.roostremote.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.json.JSONObject;

/**
 * Created by ell on 1/11/16.
 */
@ParseClassName("Device")
public class Device extends ParseObject{

    public String getName(){
        return (String) get("name");
    }

    public String getHost(){
        return (String) get("host");
    }

    public String getNamespace(){
        return (String) get("namespace");
    }

    public ParseFile getImage(){
        return (ParseFile) get("image");
    }

    public JSONObject getProperties(){
        return getJSONObject("properties");
    }

}
