package com.rndapp.roostremote.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ell on 1/10/16.
 */
@ParseClassName("Place")
public class Place extends ParseObject{

    public String getName(){
        return (String) get("name");
    }

    public ParseFile getImage(){
        return (ParseFile) get("image");
    }

    public List<Device> getDevices(){
        return (List<Device>) get("devices");
    }

    public ArrayList<String> getSsids(){
        return new ArrayList<>((List)getList("ssids"));
    }
}
