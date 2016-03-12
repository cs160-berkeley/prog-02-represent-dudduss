package com.example.sampathduddu.eagleeye;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by sampathduddu on 3/1/16.
 */
public class WatchListenerService extends WearableListenerService {

    private String[] names;
    private String[] parties;
    private String[] endDates;
    private String[] bioIds;
    private String city;
    private String state;
    private double obama;
    private double romney;

    private double selectedLat;
    private double selectedLon;

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


    private static final String WEARABLE_DATA_PATH = "/wearable_data";

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        Log.d("myTag", "got here 1");

        DataMap dataMap;
        for (DataEvent event : dataEvents) {
            // Check the data type
            Log.d("myTag", "got here 2");
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // Check the data path
                String path = event.getDataItem().getUri().getPath();
                if (path.equals(WEARABLE_DATA_PATH)) {}

                dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();

//                dataMap.putString("city", selectedCity);
//                dataMap.putString("state", selectedCity);
//                dataMap.putDouble("obama", obamaPercentage);
//                dataMap.putDouble("romney", romneyPercentage);

                names = dataMap.getStringArray("names");
                parties = dataMap.getStringArray("parties");
                endDates = dataMap.getStringArray("endDates");
                bioIds = dataMap.getStringArray("bioIDs");

                city = dataMap.getString("city");
                state = dataMap.get("state");
                obama = dataMap.getDouble("obama");
                romney = dataMap.getDouble("romney");
                selectedLat = dataMap.getDouble("lat");
                selectedLon = dataMap.getDouble("lon");
                Log.d("romney", String.valueOf(romney));

            }
        }

        Log.d("myTag", "got here 3");

        Intent intent = new Intent(this, CongressionalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra("names", names);
        intent.putExtra("parties", parties);
        intent.putExtra("endDates", endDates);
        intent.putExtra("bioIDs", bioIds);
        intent.putExtra("city", city);
        intent.putExtra("state", state);
        intent.putExtra("obama", obama);
        intent.putExtra("romney", romney);
        intent.putExtra("lat", selectedLat);
        intent.putExtra("lon", selectedLon);
        startActivity(intent);

        Log.d("myTag", "got here 4");
    }

}
