package com.example.sampathduddu.eagleeye;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by sampathduddu on 3/2/16.
 *
 *
 */
public class VoteActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_vote_layout);

        Intent intent = getIntent();


        String city = intent.getStringExtra("city");
        String state = intent.getStringExtra("state");
        double obama = intent.getDoubleExtra("Obama", 0);
        double romney = intent.getDoubleExtra("Romney", 0);

        Log.d("obama", String.valueOf(obama));
        Log.d("romney", String.valueOf(romney));


        TextView location = (TextView) findViewById(R.id.location);


        location.setText(city + ", " + state);


        PieChartView pieChart = (PieChartView) findViewById(R.id.chart);

        List<SliceValue> values = new ArrayList<SliceValue>();

        SliceValue a = new SliceValue( (float) obama, ChartUtils.COLOR_BLUE);
        SliceValue b = new SliceValue( (float) romney, ChartUtils.COLOR_RED);
        a.setLabel(("Obama " +  a.getValue() + "%").toCharArray());
        b.setLabel(("Romney " + b.getValue() + "%").toCharArray());

        values.add(a);
        values.add(b);

        PieChartData data = new PieChartData(values);
        data.setHasLabels(true);

        pieChart.setPieChartData(data);

    }

}
