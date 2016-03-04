package com.example.sampathduddu.eagleeye;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by sampathduddu on 3/1/16.
 */
public class WatchListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());

        String zip = messageEvent.getPath().substring(1);

        Intent intent = new Intent(this, CongressionalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //you need to add this flag since you're starting a new activity from a service
        intent.putExtra("zip", zip);
        Log.d("T", "about to start watch MainActivity with CAT_NAME: Fred");
        startActivity(intent);

    }
}
