package com.example.sampathduddu.eagleeye;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sampathduddu on 2/29/16.
 */
public class CongressionalActivity extends AppCompatActivity implements OnItemClickListener {

    ListView congressList;

    private String zipcode;

    private String[] names;
    private int[] img_resource;
    private String[] parties;
    private String[] emails;
    private String[] websites;

    private int moveToDetailIndex = -1;

    private ArrayList<Congressmen> congressmen = new ArrayList<Congressmen>();

    Congressmen selected;

    String[] tweets = {"Tonight we are showing that this is the People's House. #ExIm4jobs",
            "Murray continues fighting to end the VA’s outdated ban on IVF for veterans injured during service. More: http://nyti.ms/1RzwET6",
            "The mega ship proves that when we invest in our freight infrastructure, we create more jobs & have more containers moving thru our ports –MC"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.congressional);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("  Congress Members");
        toolbar.setTitleTextColor(0xFFFFFFFF);

        Intent getCongressmen = getIntent();
//        zipcode = getZipcode.getStringExtra("zip");

        Log.d("congressmen", String.valueOf(congressmen.size()));


        Log.d("create", "creating the congressional view");


        if (getCongressmen.getIntExtra("selectedIndex", -1) != -1) {


            moveToDetailIndex = getCongressmen.getIntExtra("selectedIndex", -1);

            Log.d("somehow", "somehow got here");

        }

        congressmen = (ArrayList<Congressmen>) getCongressmen.getSerializableExtra("congressmen");


//        setData();
//        toolbar.setLogo("eagle");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        congressList = (ListView) findViewById(R.id.something);

        CongressCell adapter = new CongressCell(getApplicationContext(), R.layout.congress_row);

        congressList.setAdapter(adapter);

        for (int i = 0; i < congressmen.size(); i++) {

            adapter.add(congressmen.get(i));
        }

        congressList.setOnItemClickListener(this);

        //Here move to detail view when need be

        if (moveToDetailIndex >= 0) {

            selected = congressmen.get(moveToDetailIndex);
            //  selected = new Congressmen(names[index], parties[index], emails[index], websites[index], tweets[index], img_resource[index]);

            Log.d("name", selected.name);
            Intent i = new Intent(CongressionalActivity.this, DetailActivity.class);
            i.putExtra("selected_congressmen", (Serializable) selected);
            startActivity(i);

        }


//        congressList.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position,
//                                    long id) {
//
//                Toast.makeText(getBaseContext(), "sadfldsfadsf", Toast.LENGTH_LONG).show();
//
//
//            }
//        });



    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // your stuff here
//        selected = obj;

        int index = arg2;
        selected = congressmen.get(index);
      //  selected = new Congressmen(names[index], parties[index], emails[index], websites[index], tweets[index], img_resource[index]);


        Log.d("name", selected.name);
        Intent i = new Intent(CongressionalActivity.this, DetailActivity.class);
        i.putExtra("selected_congressmen", (Serializable) selected);
        startActivity(i);

//        Log.d("yesssir", Integer.toString(arg2));
//        Toast.makeText(getBaseContext(), "sdafsdf", Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            // Respond to the action bar's Up/Home button
//            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);

        onBackPressed();
        return true;
    }


    public void setData() {

        if (zipcode.equals("98502")) {
            names = new String[]{"Rep. Denny Heck", "Senator Patty Murray", "Senator Maria Cantwell"};

            img_resource = new int[]{R.drawable.heckdenny, R.drawable.murray, R.drawable.cantwell};
            parties = new String[]{"Democrat", "Democrat", "Democrat"};

            emails = new String[]{"dennyheck@gmail.com", "pattymurray@gmail.com", "mariacantwell@gmail.com"};
            websites = new String[]{"dennyheck.com", "pattymurray.com", "mariacantwell.com"};


        } else if (zipcode.equals("94720")){

            names = new String[]{"Rep. Barbara Lee", "Senator Diane Feinstein", "Senator Barbara Boxer"};

            img_resource = new int[]{R.drawable.barbaralee, R.drawable.dianne, R.drawable.barbaraboxer};
            parties = new String[]{"Democrat", "Democrat", "Democrat"};

            emails = new String[]{"barbaralee@gmail.com", "dianefeinstein@gmail.com", "barbaraboxer@gmail.com"};
            websites = new String[]{"barbaralee.com", "dianefeinstein.com", "barbaraboxer.com"};



        } else {
            names = new String[]{"Rep. Gary Palmer", "Senator Richard Selby", "Senator Jeff Sessions"};

            img_resource = new int[]{R.drawable.garypalmer, R.drawable.richardselby, R.drawable.jeffsessions};
            parties = new String[]{"Republican", "Republican", "Republican"};

            emails = new String[]{"garypalmer@gmail.com", "richardselby@gmail.com", "jeffsessions@gmail.com"};
            websites = new String[]{"garypalmer.com", "richardselby.com", "jeffsessions.com"};
        }

    }


}
