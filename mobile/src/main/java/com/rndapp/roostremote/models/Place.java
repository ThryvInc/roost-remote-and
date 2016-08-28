package com.rndapp.roostremote.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ell on 1/10/16.
 */
public class Place extends ServerObject implements Serializable{
    private String name;
    private String imageUrl;
    private List<String> ssids;

    public String getName(){
        return name;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public List<String> getSsids(){
        return ssids;
    }
}
