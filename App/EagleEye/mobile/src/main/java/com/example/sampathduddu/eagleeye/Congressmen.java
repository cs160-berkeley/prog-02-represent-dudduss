package com.example.sampathduddu.eagleeye;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by sampathduddu on 2/29/16.
 */
public class Congressmen implements Serializable {

    public String name;
    public String party;
    public String email;
    public String website;
    public String bio_id;
    public String twitterName;
    public String endDate;
    public String occupation;

    //From other SunLife API calls
    public ArrayList<String> committees;
    public ArrayList<String> bills;

    //From Twitter APIs
    public String tweet;
    public String image_url;
    public int image_resource;


    public Congressmen(String name, String party, String email, String website, String bio_id,
                       String twitterName, String endDate, String occupation) {

        this.name = name;
        this.party = party;
        this.email = email;
        this.website = website;
        this.bio_id = bio_id;
        this.twitterName = twitterName;
        this.endDate = endDate;
        this.image_resource = R.drawable.barbaralee;
        this.tweet = "Murray continues fighting to end the VA’s outdated ban on IVF for veterans " +
                "injured during service. More: http://nyti.ms/1RzwET6";
        this.occupation = occupation;

    }


}


