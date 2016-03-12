package com.example.sampathduddu.eagleeye;

/**
 * Created by sampathduddu on 3/1/16.
 */

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhoneListenerService extends WearableListenerService {

    private static final String TOAST = "/send_toast";

    private ArrayList<Congressmen> congressmen = new ArrayList<Congressmen>();

    private double currLatitude;
    private double currLongitude;

    private double selectedLat;
    private double selectedLon;

    private String selectedCity;
    private String selectedCounty;
    private String selectedState;
    private String selectedStateAbbreviation;

    private double obamaPercentage;
    private double romneyPercentage;
    private int selectedIndex;

    private ArrayList<String> names = new ArrayList<String>
            (Arrays.asList("Rep. Denny Heck", "Senator Patty Murray", "Senator Maria Cantwell",
                    "Rep. Barbara Lee", "Senator Diane Feinstein", "Senator Barbara Boxer",
                    "Rep. Gary Palmer", "Senator Richard Selby", "Senator Jeff Sessions"
                    ));
    private ArrayList<Integer> image_resource = new ArrayList<Integer>
            (Arrays.asList(R.drawable.heckdenny, R.drawable.murray, R.drawable.cantwell,
                    R.drawable.barbaralee, R.drawable.dianne, R.drawable.barbaraboxer,
                    R.drawable.garypalmer, R.drawable.richardselby, R.drawable.jeffsessions
                    ));

    private ArrayList<String> parties = new ArrayList<String> (Arrays.asList("Democrat", "Democrat", "Democrat",
            "Democrat", "Democrat", "Democrat", "Republican", "Republican", "Republican"));

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());

        String latLon = messageEvent.getPath();

        String[]latLonArray = latLon.split(" ");

        double lat = Double.parseDouble(latLonArray[0]);
        double lon = Double.parseDouble(latLonArray[1]);

        selectedIndex = Integer.parseInt(latLonArray[2]);

        setLocaleDetails(lat, lon);

//        int index = names.indexOf(name);

//        Intent intent = new Intent(this, DetailActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        //you need to add this flag since you're starting a new activity from a service
//
//        Congressmen cg = new Congressmen("", "", "","", "","","","");
//
//       // Congressmen congressman = new Congressmen(name, parties.get(index), "", "", "", image_resource.get(index));
//
//        intent.putExtra("selected_congressmen", (Serializable) cg);
////        intent.putExtra("name", name);
////        intent.putExtra("image_src", image_resource.get(index));
////        intent.putExtra("party", parties.get(index));
//
//        //Log.d("T", "about to start watch MainActivity with CAT_NAME: Fred");
//        startActivity(intent);

//        Intent intent = new Intent(this, CongressionalActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//
//
//        intent.putExtra("id", bioId);
//        intent.putExtra("comingFrom", "PhoneListener");
//        startActivity(intent);

    }

    public void setLocaleDetails(final double lat, final double lon) {

        String geocoderURL = "http://maps.googleapis.com/maps/api/geocode/json?&";

        selectedLat = lat;
        selectedLon = lon;
        geocoderURL += "latlng=" + String.valueOf(lat) + "," +  String.valueOf(lon);

        //Log.d("url", geocoderURL);
        Ion.with(getBaseContext())
                .load(geocoderURL)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        JsonArray resultsArray = result.getAsJsonArray("results");
                        JsonArray addressArray = (JsonArray) resultsArray.get(0).getAsJsonObject().get("address_components");

                        for (JsonElement component: addressArray) {

                            String type = component.getAsJsonObject().get("types").getAsJsonArray().get(0).toString();

                            type = type.replaceAll("\"", "");
                            if (type.equals("locality")) {
                                selectedCity = component.getAsJsonObject().get("long_name").toString();
                                selectedCity = selectedCity.replaceAll("\"", "");

                            } else if (type.equals("administrative_area_level_1")) {
                                selectedState = component.getAsJsonObject().get("long_name").toString();
                                selectedStateAbbreviation = component.getAsJsonObject().get("short_name").toString();

                                selectedState = selectedState.replaceAll("\"", "");
                                selectedStateAbbreviation = selectedStateAbbreviation.replaceAll("\"", "");

                            } else if (type.equals("administrative_area_level_2")) {
                                selectedCounty = component.getAsJsonObject().get("long_name").toString();
                                selectedCounty = selectedCounty.replaceAll("\"", "");

                            }
                        }

                        getRepresentativesCoordinates(lat, lon);
                        return;

                    }
                });
    }

    public void getRepresentativesCoordinates(double lat, double lon) {

        congressmen.clear();
        String url = "http://congress.api.sunlightfoundation.com/legislators/locate?latitude=" +
                Double.toString(lat) +"&longitude="+ Double.toString(lon) + "&apikey=d0bc1683ec03472c9cf0dc4b683b2f0d";

        //Log.d("url", url);

        Ion.with(getBaseContext())
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        Log.d("Location Result", String.valueOf(result));

                        JsonArray array = result.getAsJsonArray("results");

                        for (int i = 0; i < array.size(); i++) {

                            String name = array.get(i).getAsJsonObject().get("first_name").toString() + " " +
                                    array.get(i).getAsJsonObject().get("last_name").toString();
                            String party = array.get(i).getAsJsonObject().get("party").toString();
                            String email = array.get(i).getAsJsonObject().get("oc_email").toString();
                            String website = array.get(i).getAsJsonObject().get("website").toString();
                            String id = array.get(i).getAsJsonObject().get("bioguide_id").toString();
                            String twitterName = array.get(i).getAsJsonObject().get("twitter_id").toString();
                            String endDate = array.get(i).getAsJsonObject().get("term_end").toString();
                            String occupation = array.get(i).getAsJsonObject().get("chamber").toString();

                            name = name.replaceAll("\"", "");
                            party = party.replaceAll("\"", "");
                            email = email.replaceAll("\"", "");
                            website = website.replaceAll("\"", "");
                            twitterName = twitterName.replaceAll("\"", "");
                            endDate = endDate.replaceAll("\"", "");
                            occupation = occupation.replaceAll("\"", "");

                            Congressmen cg = new Congressmen(name, party, email, website, id,
                                    twitterName, endDate, occupation);

                            congressmen.add(cg);
//                            Log.d("count", String.valueOf(congressmen.size()));

                        }
//                        Log.d("count", String.valueOf(congressmen.size()));
                        setCongressmenCommittees(0);

                    }
                });
    }

    public void setCongressmenCommittees(final int index) {

        if (index == congressmen.size()) {
//            Log.d("count", String.valueOf(congressmen.size()));
//            Log.d("committees", String.valueOf(congressmen.get(0).committees));
//            Log.d("committees", String.valueOf(congressmen.get(1).committees));
//            Log.d("committees", String.valueOf(congressmen.get(2).committees));
//            return;

            setCongressmenBills(0);
            return;
        }

        String bio_id =  congressmen.get(index).bio_id;
        String url = "http://congress.api.sunlightfoundation.com/committees?member_ids=" + bio_id+
                "&apikey=d0bc1683ec03472c9cf0dc4b683b2f0d";

        Ion.with(getBaseContext())
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        JsonArray array = result.getAsJsonArray("results");

                        congressmen.get(index).committees = new ArrayList<String>();
                        for (int i = 0; i < array.size(); i++) {

                            if (i == 5) {
                                break;
                            }
                            String committeeName = array.get(i).getAsJsonObject().get("name").toString();

                            committeeName = committeeName.replaceAll("\"", "");

                            congressmen.get(index).committees.add(committeeName);

                        }


                        int copy = index;
                        copy++;
                        setCongressmenCommittees(copy);

                    }
                });

    }

    public void setCongressmenBills(final int index) {

        if (index == congressmen.size()) {
//            Log.d("count", String.valueOf(congressmen.size()));
//            Log.d("bills", String.valueOf(congressmen.get(0).bills));
//            Log.d("bills", String.valueOf(congressmen.get(1).bills));
//            Log.d("bills", String.valueOf(congressmen.get(2).bills));
            getLatestTweet(0);
            return;

        }

        String bio_id =  congressmen.get(index).bio_id;
        String url = "http://congress.api.sunlightfoundation.com/bills?sponsor_id=" + bio_id+
                "&apikey=d0bc1683ec03472c9cf0dc4b683b2f0d";

        Ion.with(getBaseContext())
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error

                        JsonArray array = result.getAsJsonArray("results");

                        congressmen.get(index).bills = new ArrayList<String>();
                        for (int i = 0; i < array.size(); i++) {

                            if (congressmen.get(index).bills.size() == 5) {
                                break;
                            }

                            JsonElement elem = array.get(i).getAsJsonObject().get("short_title");

                            if (!elem.toString().equals("null")) {
                                String bill = elem.toString();

                                bill = bill.replaceAll("\"", "");

                                if (bill.toCharArray().length > 40) {
                                    bill.substring(0, 40);
                                    bill += "...";
                                }

                                congressmen.get(index).bills.add(bill);
                            }

                        }


                        int copy = index;
                        copy++;
                        setCongressmenBills(copy);

//                        getLatestTweet();

//                        Intent i = new Intent(MainActivity.this, CongressionalActivity.class);
//                        i.putExtra("zip", "94720");
//                        startActivity(i);
                    }
                });

    }

    public void goToCongressional() {

        setVotingData();

//        //In Phone
//        Intent i = new Intent(PhoneListenerService.this, CongressionalActivity.class);
//        i.putExtra("congressmen", (Serializable) congressmen);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.putExtra("selectedIndex", selectedIndex);

        //In Phone
        Intent i = new Intent(PhoneListenerService.this, DetailActivity.class);
        i.putExtra("selected_congressmen", congressmen.get(selectedIndex));
        //i.putExtra("congressmen", (Serializable) congressmen);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.putExtra("selectedIndex", selectedIndex);


        startActivity(i);

        //In watch
//        Intent sendIntent = new Intent(PhoneListenerService.this, PhoneToWatchService.class);
//        sendIntent.putExtra("congressmen", congressmen);
//        sendIntent.putExtra("obama", obamaPercentage);
//        sendIntent.putExtra("romney", romneyPercentage);
//        sendIntent.putExtra("state", selectedState);
//        sendIntent.putExtra("city", selectedCity);
//        sendIntent.putExtra("lat", selectedLat);
//        sendIntent.putExtra("lon", selectedLon);
//
//
//        sendIntent.putExtra("zip", "94720");
//        startService(sendIntent);


    }

    public void setVotingData() {

//Get Data From Text Resource File Contains Json Data.
        InputStream inputStream = getResources().openRawResource(R.raw.relection_results_2012);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Log.v("Text Data", byteArrayOutputStream.toString());
        try {
            // Parse the data into jsonobject to get original data in form of json.
            JSONObject jObject = new JSONObject(
                    byteArrayOutputStream.toString());

            String key = selectedCounty + ", " + selectedStateAbbreviation;

            JSONObject voteResult = jObject.getJSONObject(key);

            obamaPercentage = voteResult.getDouble("obama");
            romneyPercentage = voteResult.getDouble("romney");
            Log.d("ready", "ready");



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getLatestTweet(final int i) {

        if (i == congressmen.size()) {
            Log.d("tweet1", congressmen.get(0).tweet);
            goToCongressional();
            return;
        }

        final Congressmen current = congressmen.get(i);

        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        // Can also use Twitter directly: Twitter.getApiClient()
        StatusesService statusesService = twitterApiClient.getStatusesService();


        statusesService.userTimeline(null, current.twitterName, 1, null, null,
                null, null, null, null, new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> result) {

                        Log.d("result", String.valueOf(result.data));
                        List<Tweet> tweets = new ArrayList<Tweet>();
                        tweets = (List<Tweet>) result.data;
                        for (Tweet tweet : tweets) {
                            current.tweet = tweet.text;
                            current.image_url = tweet.user.profileImageUrl;
                        }

                        int copy = i;
                        getLatestTweet(copy + 1);

                        //Do something with result, which provides a Tweet inside of result.data
                    }

                    public void failure(TwitterException exception) {
                        Log.d("failure", "failure");
                        //Do something on failure
                    }
                });

    }







}
