package com.example.sampathduddu.eagleeye;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.Button;

import lecho.lib.hellocharts.view.PieChartView;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;

import java.util.List;
import java.util.ArrayList;
import android.content.Intent;
import android.widget.TextView;

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


        String county = intent.getStringExtra("county");
        String state = intent.getStringExtra("state");
        int obama = intent.getIntExtra("Obama", 0);
        int romney = intent.getIntExtra("Romney", 0);


        TextView location = (TextView) findViewById(R.id.location);


        location.setText(county + " County, " + state);


        PieChartView pieChart = (PieChartView) findViewById(R.id.chart);

        List<SliceValue> values = new ArrayList<SliceValue>();

        SliceValue a = new SliceValue(obama, ChartUtils.COLOR_BLUE);
        SliceValue b = new SliceValue(romney, ChartUtils.COLOR_RED);
        a.setLabel(("Obama " + (int) a.getValue() + "%").toCharArray());
        b.setLabel(("Romney " + (int) b.getValue() + "%").toCharArray());

        values.add(a);
        values.add(b);

        PieChartData data = new PieChartData(values);
        data.setHasLabels(true);

        pieChart.setPieChartData(data);

    }

}
