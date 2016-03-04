package com.example.sampathduddu.eagleeye;

/**
 * Created by sampathduddu on 3/1/16.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PhoneListenerService extends WearableListenerService {

    private static final String TOAST = "/send_toast";

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

        String name = messageEvent.getPath();

        int index = names.indexOf(name);

        Intent intent = new Intent(this, DetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //you need to add this flag since you're starting a new activity from a service

        Congressmen congressman = new Congressmen(name, parties.get(index), "", "", "", image_resource.get(index));

        intent.putExtra("selected_congressmen", (Serializable) congressman);
//        intent.putExtra("name", name);
//        intent.putExtra("image_src", image_resource.get(index));
//        intent.putExtra("party", parties.get(index));

        //Log.d("T", "about to start watch MainActivity with CAT_NAME: Fred");
        startActivity(intent);

    }

}
