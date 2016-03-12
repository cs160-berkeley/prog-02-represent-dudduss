package com.example.sampathduddu.eagleeye;

/**
 * Created by sampathduddu on 3/1/16.
 */

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;


import java.util.ArrayList;
import java.util.Date;


public class PhoneToWatchService extends Service implements GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mApiClient;

    private ArrayList<Congressmen> congressmen = new ArrayList<Congressmen>();

    private String selectedCity;
    private String selectedState;

    private double obamaPercentage;
    private double romneyPercentage;

    private double selectedLat;
    private double selectedLon;

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize the googleAPIClient for message passing

//        GoogleApiClient  mGoogleClient = new GoogleApiClient.Builder(this)
//                .addApi(Wearable.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();


        mApiClient = new GoogleApiClient.Builder( this )
                .addApiIfAvailable(Wearable.API)
                .addConnectionCallbacks(this)
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Which cat do we want to feed? Grab this info from INTENT
        // which was passed over when we called startService
        Bundle extras = intent.getExtras();
        final String zip = extras.getString("zip");

        congressmen=(ArrayList<Congressmen>) intent.getExtras().get("congressmen");
        selectedState = (String) extras.get("state");
        selectedCity = (String) extras.get("city");

        obamaPercentage = (Double) extras.get("obama");
        romneyPercentage = (Double) extras.get("romney");

        selectedLat = (Double) extras.get("lat");
        selectedLon = (Double) extras.get("lon");

        // Send the message with the cat name
        new Thread(new Runnable() {
            @Override
            public void run() {
                //first, connect to the apiclient
                mApiClient.connect();
                //now that you're connected, send a massage with the cat name
                //sendMessage("/" + zip, zip);
            }
        }).start();

        return START_STICKY;
    }

    @Override //alternate method to connecting: no longer create this in a new thread, but as a callback
    public void onConnected(Bundle bundle) {
        String WEARABLE_DATA_PATH = "/wearable_data";

        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> parties = new ArrayList<String>();
        ArrayList<String> endDates = new ArrayList<String>();
        ArrayList<String> bioIDs = new ArrayList<String>();

        // Create a DataMap object and send it to the data layer
        DataMap dataMap = new DataMap();

        for (Congressmen cg: congressmen) {
            names.add(cg.name);
            parties.add(cg.party);
            endDates.add(cg.endDate);
            bioIDs.add(cg.bio_id);
        }

        String[] names1 = new String[names.size()];
        names1 = names.toArray(names1);

        String[] parties1 = new String[parties.size()];
        parties1 = parties.toArray(parties1);

        String[] endDates1 = new String[endDates.size()];
        endDates1 = endDates.toArray(endDates1);

        String[] bioIDs1 = new String[bioIDs.size()];
        bioIDs1 = bioIDs.toArray(bioIDs1);

        dataMap.putStringArray("names", names1);
        dataMap.putStringArray("parties", parties1);
        dataMap.putStringArray("endDates", endDates1);
        dataMap.putStringArray("bioIDs", bioIDs1);

        dataMap.putString("city", selectedCity);
        dataMap.putString("state", selectedState);
        dataMap.putDouble("obama", obamaPercentage);
        dataMap.putDouble("romney", romneyPercentage);
        dataMap.putDouble("lat", selectedLat);
        dataMap.putDouble("lon", selectedLon);

        dataMap.putLong("time", new Date().getTime());


        Log.d("obamaPhone", String.valueOf(obamaPercentage));
        Log.d("romneyPhone", String.valueOf(romneyPercentage));

        new SendToDataLayerThread(WEARABLE_DATA_PATH, dataMap).start();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

//    public void onConnected(Bundle connectionHint) {
//
//        String WEARABLE_DATA_PATH = "/wearable_data";
//
//        ArrayList<String> names = new ArrayList<String>();
//        ArrayList<String> parties = new ArrayList<String>();
//
//        // Create a DataMap object and send it to the data layer
//        DataMap dataMap = new DataMap();
//
//        for (Congressmen cg: congressmen) {
//            names.add(cg.name);
//            parties.add(cg.party);
//        }
//
//        String[] names1 = new String[names.size()];
//        names1 = names.toArray(names1);
//
//        String[] parties1 = new String[parties.size()];
//        parties1 = parties.toArray(parties1);
//
//        dataMap.putStringArray("names", names1);
//        dataMap.putStringArray("parties", parties1);
//        dataMap.putString("city", selectedCity);
//        dataMap.putString("state", selectedCity);
//        dataMap.putDouble("obama", obamaPercentage);
//        dataMap.putDouble("romney", romneyPercentage);
//
//        new SendToDataLayerThread(WEARABLE_DATA_PATH, dataMap).start();
//    }

    @Override //remember, all services need to implement an IBiner
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendMessage( final String path, final String text ) {
        //one way to send message: start a new thread and call .await()
        //see watchtophoneservice for another way to send a message
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    //we find 'nodes', which are nearby bluetooth devices (aka emulators)
                    //send a message for each of these nodes (just one, for an emulator)
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), path, text.getBytes() ).await();
                    //4 arguments: api client, the node ID, the path (for the listener to parse),
                    //and the message itself (you need to convert it to bytes.)
                }
            }
        }).start();
    }


    public void onConnectionFailed(ConnectionResult connectionResult) { }


    class SendToDataLayerThread extends Thread {
        String path;
        DataMap dataMap;

        // Constructor for sending data objects to the data layer
        SendToDataLayerThread(String p, DataMap data) {
            path = p;
            dataMap = data;
        }

        public void run() {
            // Construct a DataRequest and send over the data layer
            PutDataMapRequest putDMR = PutDataMapRequest.create(path);
            putDMR.getDataMap().putAll(dataMap);
            PutDataRequest request = putDMR.asPutDataRequest();
            DataApi.DataItemResult result = Wearable.DataApi.putDataItem(mApiClient, request).await();
            if (result.getStatus().isSuccess()) {
                Log.v("myTag", "DataMap: " + dataMap + " sent successfully to data layer ");
                mApiClient.disconnect();
            }
            else {
                // Log an error
                Log.v("myTag", "ERROR: failed to send DataMap to data layer");
            }
        }
    }





}
