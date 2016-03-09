package com.example.sampathduddu.eagleeye;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
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
        Congressmen cg =  (Congressmen) i.getSerializableExtra("selected_congressmen");

        if (cg.occupation.equals("senate")) {
            toolbar.setTitle("Senator " + cg.name);
        } else {
            toolbar.setTitle("Rep. " + cg.name);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView congressImg = (ImageView) findViewById(R.id.congressman_img);
        congressImg.setImageResource(cg.image_resource);

        TextView endTerm = (TextView) findViewById(R.id.termDate);
        endTerm.setText("Term End Date: " + cg.endDate);

        TextView party = (TextView) findViewById(R.id.party);

        if (cg.party.equals("D")) {
            party.setTextColor(Color.parseColor("#0c7ff3"));
            party.setText("Democrat");
        } else {
            party.setTextColor(Color.parseColor("#e94949"));
            party.setText("Republican");
        }

        TextView committeesText = (TextView) findViewById(R.id.comittees);

        String allCommittees = "";

        for (String committee: cg.committees) {
            allCommittees += " • " + committee + "\n";
//            if (cg.committees.indexOf(committee) < cg.committees.size()-1) {
//                allCommittees += " • ";
//            }
        }

        committeesText.setText(allCommittees);


        TextView billsText = (TextView) findViewById(R.id.bills);

        String allBills = "";


        for (String bill: cg.bills) {
            allBills += " • " + bill + "\n";;
//            if (cg.bills.indexOf(bill) < cg.bills.size()-1) {
//                allBills += " • ";
//            }
        }

        billsText.setText(allBills);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return true;
    }
}
