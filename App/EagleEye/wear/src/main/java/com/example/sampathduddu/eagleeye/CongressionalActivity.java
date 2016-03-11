package com.example.sampathduddu.eagleeye;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sampathduddu.eagleeye.ShakeDetector.OnShakeListener;


/**
 * Created by sampathduddu on 3/1/16.
 */
public class CongressionalActivity extends WearableActivity {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    private String zipcode;

    private String[] names;
    private int[] img_resource;
    private String[] parties;

    private String city;
    private String state;
    private double obamaPercentage;
    private double romneyPercentage;


    private WearableListView listView;

    int index = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_congressmen);


        Log.d("myTag", "got here 4");

        Button prev = (Button) findViewById(R.id.prev);
        prev.setEnabled(false);

        Intent info = getIntent();

        names = info.getStringArrayExtra("names");
        parties = info.getStringArrayExtra("parties");
        city = info.getStringExtra("city");
        state = info.getStringExtra("state");

//        Log.d("cityFromPhone", city);
//        Log.d("stateFromPhone", state);

        obamaPercentage = info.getDoubleExtra("obama", 50);
        romneyPercentage = info.getDoubleExtra("romney", 49);

        Log.d("obamaFromPhone", String.valueOf(obamaPercentage));
        Log.d("romneyFromPhone", String.valueOf(romneyPercentage));

        img_resource = new int[]{R.drawable.heckdenny, R.drawable.murray, R.drawable.cantwell};

//        zipcode = zip.getStringExtra("zip");
//
//        zipcode = "98502";
//        setData();
        setView();



        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new OnShakeListener() {

            @Override
            public void onShake(int count) {
				/*
				 * The following method, "handleShakeEvent(count):" is a stub //
				 * method you would use to setup whatever you want done once the
				 * device has been shook.
				 */
                handleShakeEvent(count);
            }
        });
    }

    public void handleShakeEvent(int count) {

        if (!zipcode.equals("98502") && !zipcode.equals("94720")) {
            zipcode = "98502";
        } else {
            zipcode = "35766";
        }
        setData();

    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    public void setData() {

        if (zipcode.equals("98502")) {
            names = new String[]{"Rep. Denny Heck", "Senator Patty Murray", "Senator Maria Cantwell"};

            img_resource = new int[]{R.drawable.heckdenny, R.drawable.murray, R.drawable.cantwell};
            parties = new String[]{"Democrat", "Democrat", "Democrat"};
        } else if (zipcode.equals("94720")){

            names = new String[]{"Rep. Barbara Lee", "Senator Diane Feinstein", "Senator Barbara Boxer"};

            img_resource = new int[]{R.drawable.barbaralee, R.drawable.dianne, R.drawable.barbaraboxer};
            parties = new String[]{"Democrat", "Democrat", "Democrat"};
        } else {
            names = new String[]{"Rep. Gary Palmer", "Senator Richard Selby", "Senator Jeff Sessions"};

            img_resource = new int[]{R.drawable.garypalmer, R.drawable.richardselby, R.drawable.jeffsessions};
            parties = new String[]{"Republican", "Republican", "Republican"};
        }

        setView();

    }

    public void setView() {

//        names = new String[]{"Rep. Denny Heck", "Senator Patty Murray", "Senator Maria Cantwell"};
//
//        img_resource = new int[]{R.drawable.heckdenny, R.drawable.murray, R.drawable.cantwell};
//        parties = new String[]{"Democrat", "Democrat", "Democrat"};

        ImageView img = (ImageView) findViewById(R.id.congressImage);
        img.setImageResource(img_resource[index]);

        TextView name = (TextView) findViewById(R.id.name);
        name.setText(names[index]);

        TextView party = (TextView) findViewById(R.id.party);
        party.setText(parties[index]);

        if (parties[index].equals("D")) {
            party.setText("Democrat");
            party.setTextColor(Color.parseColor("#0c7ff3"));
        } else {
            party.setText("Republican");
            party.setTextColor(Color.parseColor("#e94949"));
        }

    }



    public void nextClicked(View v)
    {
        index++;

        Button prev = (Button) findViewById(R.id.prev);
        prev.setEnabled(true);

        Button next = (Button) findViewById(R.id.next);

        if (index == 2) {
            next.setText("Vote");
        }

        if (index == 3) {
            Intent toVote = new Intent(CongressionalActivity.this, VoteActivity.class);

            toVote.putExtra("city", city);
            toVote.putExtra("state", state);

            Log.d("obamaToVote", String.valueOf(obamaPercentage));
            Log.d("romneyToVote", String.valueOf(romneyPercentage));
            toVote.putExtra("Obama", obamaPercentage);
            toVote.putExtra("Romney", romneyPercentage);

//            if (zipcode.equals("98502")) {
//                toVote.putExtra("county", "Thurston");
//                toVote.putExtra("state", "WA");
//                toVote.putExtra("Obama", 58);
//                toVote.putExtra("Romney", 41);
//            } else if (zipcode.equals("94720")) {
//                toVote.putExtra("county", "Alameda");
//                toVote.putExtra("state", "CA");
//                toVote.putExtra("Obama", 56);
//                toVote.putExtra("Romney", 44);
//            } else {
//                toVote.putExtra("county", "Shelby");
//                toVote.putExtra("state", "AL");
//                toVote.putExtra("Obama", 41);
//                toVote.putExtra("Romney", 59);
//            }

            startActivity(toVote);
            index = 2;
            return;
        }

        setView();
//        ImageView img = (ImageView) findViewById(R.id.congressImage);
//        img.setImageResource(img_resource[index]);
//
//        TextView name = (TextView) findViewById(R.id.name);
//        name.setText(names[index]);
    }

    public void prevClicked(View v)
    {
        index--;

        Button next = (Button) findViewById(R.id.next);
        next.setEnabled(true);

        if (index == 0) {
            Button prev = (Button) findViewById(R.id.prev);
            prev.setEnabled(false);
        }
        setView();

//        ImageView img = (ImageView) findViewById(R.id.congressImage);
//        img.setImageResource(img_resource[index]);
//
//        TextView name = (TextView) findViewById(R.id.name);
//        name.setText(names[index]);

    }

    public void readClicked(View v)
    {
        Log.d("READ", "read recognized");

        Intent sendIntent = new Intent(CongressionalActivity.this, WatchToPhoneService.class);

        TextView name = (TextView) findViewById(R.id.name);
        String congress_name = name.getText().toString();

        Log.d("name", congress_name);

        sendIntent.putExtra("name",congress_name);
        startService(sendIntent);
//        ImageView img = (ImageView) findViewById(R.id.congressImage);
//        img.setImageResource(R.drawable.murray);
    }


}
