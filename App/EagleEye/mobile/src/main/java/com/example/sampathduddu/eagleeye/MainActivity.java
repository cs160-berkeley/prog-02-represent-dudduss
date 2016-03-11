package com.example.sampathduddu.eagleeye;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

//import com.twitter.sdk.android.core.internal.G;


public class MainActivity extends AppCompatActivity implements LocationListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "40KO6KoOri8alJhkg3zMfgCJQ";
    private static final String TWITTER_SECRET = "uMMB8u3ZsUeFCTQyxXWYMhWcSND1WvnPWPRRNSIwmiz2tQSShu";

    private TwitterApiClient twitterApiClient;
    private AppSession guestAppSession;

    private ArrayList<Congressmen> congressmen = new ArrayList<Congressmen>();

    private double currLatitude;
    private double currLongitude;

    private String selectedCity;
    private String selectedCounty;
    private String selectedState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

//        authenticate();

        //Seting layout basics
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("  Eagle Eye");
        toolbar.setLogo(R.drawable.eaglelarge);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final EditText zipcode = (EditText) findViewById(R.id.zipcode);

        zipcode.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO ||
                        actionId == EditorInfo.IME_ACTION_NEXT || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {

                    String zip = zipcode.getText().toString();

                    setLocaleDetails("zip", zip, 0.0, 0.0);
                   // getRepresentativesZipcode(zip);
                    return true;
                }
                return false;
            }
        });

//        getLatestTweet();

    }

//    public void authenticate() {
//        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
//            @Override
//            public void success(Result<AppSession> result) {
//                Log.d("Twitter", "twitter success");
//                guestAppSession = result.data;
//                twitterApiClient = TwitterCore.getInstance().getApiClient(guestAppSession);
//                twitterApiClient.getSearchService().tweets("#fabric", null, null, null, null, 50, null, null, null, true, new GuestCallback<>(new Callback<Search>() {
//                    @Override
//                    public void success(Result<Search> result) {
//                        // use result tweets
//                        Log.d("something", "something else");
//                    }
//
//                    @Override
//                    public void failure(TwitterException exception) {
//                        // handle exceptions
//                    }
//                }));
////                getLatestTweet();
//            }
//
//            @Override
//            public void failure(TwitterException e) {
//
//            }
//        });
//
//    }



    @Override
    public void onLocationChanged(Location location) {

        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        currLatitude = location.getLatitude();
        currLongitude = location.getLongitude();

//        // Creating a LatLng object for the current location
//        getRepresentativesCoordinates(latitude, longitude);

//        return;

    }

    public void onClickCongressional(View view) {

        if (view.getId() == R.id.buttonGetLocation) {


            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                // Getting Current Location
                Location location = locationManager.getLastKnownLocation(provider);

                if(location!=null){
                    //Sets global vars lat and lon
                    onLocationChanged(location);
                }

                locationManager.requestLocationUpdates(provider, 200000, 0, this);

            }
            //Populates Representative Arrays
            setLocaleDetails("latlon", "", currLatitude, currLongitude);
//            getRepresentativesCoordinates(currLatitude, currLongitude);

//            Intent i = new Intent(MainActivity.this, CongressionalActivity.class);
//            i.putExtra("zip", "94720");
//            startActivity(i);
//
//            Intent sendIntent = new Intent(MainActivity.this, PhoneToWatchService.class);
//            sendIntent.putExtra("zip", "94720");
//            startService(sendIntent);

        }
    }

    public void setLocaleDetails(String inputType, String zipcode, final double lat, final double lon) {

        String geocoderURL = "http://maps.googleapis.com/maps/api/geocode/json?&";

        if (inputType.equals("latlon")) {

            geocoderURL += "latlng=" + String.valueOf(lat) + "," +  String.valueOf(lon);

        } else {

            getLatLonForZip(zipcode);
            return;
            //geocoderURL += "address=" + zipcode;
        }

        Log.d("url", geocoderURL);
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
                            } else if (type.equals("administrative_area_level_1")) {
                                selectedState = component.getAsJsonObject().get("long_name").toString();

                            } else if (type.equals("administrative_area_level_2")) {
                                selectedCounty = component.getAsJsonObject().get("long_name").toString();
                            }

                        }

                        getRepresentativesCoordinates(lat, lon);
                        return;

                    }
                });

    }


    public void getLatLonForZip(final String zipcode) {

        String geocoderURL = "http://maps.googleapis.com/maps/api/geocode/json?&address=" + zipcode;

        Ion.with(getBaseContext())
                .load(geocoderURL)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        JsonArray resultsArray = result.getAsJsonArray("results");
                        JsonElement k = resultsArray.get(0);
                        JsonElement l = resultsArray.get(0).getAsJsonObject();

                        JsonElement geometry = resultsArray.get(0).getAsJsonObject().get("geometry");
                        JsonElement location = geometry.getAsJsonObject().get("location");


                        double lat = location.getAsJsonObject().get("lat").getAsDouble();
                        double lon = location.getAsJsonObject().get("lng").getAsDouble();

                        setLocaleDetails("latlon", zipcode, lat, lon);
                        return;

                    }

                });


    }

    public void getRepresentativesZipcode(String zipcode) {

        congressmen.clear();

        String url = "http://congress.api.sunlightfoundation.com/legislators/locate?zip=" + zipcode +"&apikey=d0bc1683ec03472c9cf0dc4b683b2f0d";

       // String url = "http://congress.api.sunlightfoundation.com/legislators/locate?latitude=42.96&longitude=-108.09&apikey=d0bc1683ec03472c9cf0dc4b683b2f0d";
        Ion.with(getBaseContext())
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
//                        Log.d("Zipcode Result", String.valueOf(result));

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

    public void getRepresentativesCoordinates(double lat, double lon) {

        congressmen.clear();
        String url = "http://congress.api.sunlightfoundation.com/legislators/locate?latitude=" +
                Double.toString(lat) +"&longitude="+ Double.toString(lon) + "&apikey=d0bc1683ec03472c9cf0dc4b683b2f0d";

        Log.d("url", url);

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

        Intent i = new Intent(MainActivity.this, CongressionalActivity.class);
        i.putExtra("congressmen", (Serializable) congressmen);
//        i.putExtra("zip", "94720");
        startActivity(i);

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


        statusesService.userTimeline(null, current.twitterName , 1, null, null,
                null, null,null,null,new Callback<List<Tweet>>() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

}
