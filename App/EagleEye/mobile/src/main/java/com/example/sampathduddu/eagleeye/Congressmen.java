package com.example.sampathduddu.eagleeye;

import android.content.Context;
import android.os.Parcelable;
import android.os.Parcel;

import java.io.Serializable;


/**
 * Created by sampathduddu on 2/29/16.
 */
public class Congressmen implements Serializable {

    public String name;
    public String party;
    public String email;
    public String website;
    public String tweet;

    public int image_resource;

    public Congressmen(String name, String party, String email, String website, String tweet, int image_resource) {
        this.name = name;
        this.party = party;
        this.email = email;
        this.website = website;
        this.tweet = tweet;
        this.image_resource = image_resource;
    }


}


