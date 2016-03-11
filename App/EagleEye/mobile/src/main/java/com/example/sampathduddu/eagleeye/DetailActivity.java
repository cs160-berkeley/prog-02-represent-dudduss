package com.example.sampathduddu.eagleeye;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.HashMap;


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


        //For loading high quality image into image view
        ImageView congressImg = (ImageView) findViewById(R.id.congressman_img);

        String url = cg.image_url;

        if (url.substring(url.length() - 3, url.length()).equals("png")) {
            url = url.substring(0, url.length() - 11);
            url += ".png";

        } else if (url.substring(url.length() - 3, url.length()).equals("jpg")) {
            url = url.substring(0, url.length() - 11);
            url += ".jpg";

        } else if (url.substring(url.length() - 4, url.length()).equals("jpeg")) {
            url = url.substring(0, url.length() - 12);
            url += ".jpeg";
        } else {
            Log.d("break", "break");
        }

        Ion.with(congressImg)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.no_image)
                .load(url);


        TextView endTerm = (TextView) findViewById(R.id.termDate);
        String dateDisplay = dateDisplay(cg.endDate);
        endTerm.setText("Term End Date: " + dateDisplay);

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

    public String dateDisplay(String date) {

        String year = date.substring(0,4);

        HashMap<String, String> months = new HashMap<String, String>();
        months.put("01", "January");
        months.put("02", "February");
        months.put("03", "March");
        months.put("04", "April");
        months.put("05", "May");
        months.put("06", "June");
        months.put("07", "July");
        months.put("08", "August");
        months.put("09", "September");
        months.put("10", "October");
        months.put("11", "November");
        months.put("12", "December");

        return months.get(date.substring(5,7)) + " " + year;





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return true;
    }
}
