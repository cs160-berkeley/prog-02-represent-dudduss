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

import io.fabric.sdk.android.Fabric;

//import com.twitter.sdk.android.core.internal.G;


public class MainActivity extends AppCompatActivity implements LocationListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "40KO6KoOri8alJhkg3zMfgCJQ";
    private static final String TWITTER_SECRET = "uMMB8u3ZsUeFCTQyxXWYMhWcSND1WvnPWPRRNSIwmiz2tQSShu";

    private TwitterApiClient twitterApiClient;
    private AppSession guestAppSession;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        authenticate();


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("  Eagle Eye");
        toolbar.setLogo(R.drawable.eaglelarge);
//        toolbar.setLogo("eagle");

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

                if (actionId == EditorInfo.IME_ACTION_DONE || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
                    String zip = zipcode.getText().toString();
                    Intent myintent = new Intent(MainActivity.this, CongressionalActivity.class);

                    myintent.putExtra("zip", zip);
                    startActivity(myintent);

                    Intent sendIntent = new Intent(MainActivity.this, PhoneToWatchService.class);
                    sendIntent.putExtra("zip", zip);
                    startService(sendIntent);

                    return true;
                }
                return false;
            }
        });

    }

    public void authenticate() {
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> result) {
                Log.d("Twitter", "twitter success");
                guestAppSession = result.data;
                twitterApiClient = TwitterCore.getInstance().getApiClient(guestAppSession);
                getLatestTweet();
            }

            @Override
            public void failure(TwitterException e) {

            }
        });

    }



    @Override
    public void onLocationChanged(Location location) {

        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        getRepresentatives(latitude, longitude);

//
//        // Setting latitude and longitude in the TextView tv_location
//        tvLocation.setText("Latitude:" +  latitude  + ", Longitude:"+ longitude );

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

    public void onClickCongressional(View view) {

        if (view.getId() == R.id.buttonGetLocation) {

//
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
                    onLocationChanged(location);
                }

                locationManager.requestLocationUpdates(provider, 20000, 0, this);
            }


//            Intent i = new Intent(MainActivity.this, CongressionalActivity.class);
//            i.putExtra("zip", "94720");
//            startActivity(i);
//
//            Intent sendIntent = new Intent(MainActivity.this, PhoneToWatchService.class);
//            sendIntent.putExtra("zip", "94720");
//            startService(sendIntent);

        }
    }

    public void getRepresentatives(double lat, double lon) {

//        Context a = new MockContext();

        String url = "http://congress.api.sunlightfoundation.com/legislators/locate?zip=98502&apikey=d0bc1683ec03472c9cf0dc4b683b2f0d";
        Ion.with(getBaseContext())
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        Log.d("sfsdaf", String.valueOf(result));

                        getLatestTweet();

//                        Intent i = new Intent(MainActivity.this, CongressionalActivity.class);
//                        i.putExtra("zip", "94720");
//                        startActivity(i);
                    }
                });
    }

    public void getLatestTweet() {

        String url = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=SenatorBoxer&count=1";
        Ion.with(getBaseContext())
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        Log.d("Tweet", String.valueOf(result));
//                        getLatestTweet();
//                        Intent i = new Intent(MainActivity.this, CongressionalActivity.class);
//                        i.putExtra("zip", "94720");
//                        startActivity(i);
                    }
                });
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
