package com.example.sampathduddu.eagleeye;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import android.content.Intent;
import android.os.Parcelable;
import android.os.Parcel;
import android.widget.TextView;


/**
 * Created by sampathduddu on 2/29/16.
 */
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
//        toolbar.setLogo("eagle");
        Intent i = getIntent();
        Congressmen myObject =  (Congressmen) i.getSerializableExtra("selected_congressmen");


        toolbar.setTitle(myObject.name);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView congressImg = (ImageView) findViewById(R.id.congressman_img);
        congressImg.setImageResource(myObject.image_resource);

        TextView party = (TextView) findViewById(R.id.party);
        party.setText(myObject.party);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return true;
    }
}
