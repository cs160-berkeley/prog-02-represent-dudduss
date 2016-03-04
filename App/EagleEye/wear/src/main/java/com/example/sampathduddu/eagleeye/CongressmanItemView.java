package com.example.sampathduddu.eagleeye;

import android.support.wearable.view.WearableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.view.View;


import org.w3c.dom.Text;

/**
 * Created by sampathduddu on 3/2/16.
 */
public class CongressmanItemView extends FrameLayout implements WearableListView.OnCenterProximityListener {

    final ImageView portrait;
    final TextView name;
//    final TextView party;

    public CongressmanItemView(Context context) {
        super(context);
        View.inflate(context, R.layout.wearable_congressman_listview_item, this);
        portrait = (ImageView) findViewById(R.id.image);
        name = (TextView) findViewById(R.id.text);

    }


    @Override
    public void onCenterPosition(boolean b) {

        //Animation example to be ran when the view becomes the centered one
        portrait.animate().scaleX(1f).scaleY(1f).alpha(1);
        name.animate().scaleX(1f).scaleY(1f).alpha(1);

    }

    @Override
    public void onNonCenterPosition(boolean b) {

        //Animation example to be ran when the view is not the centered one anymore
        portrait.animate().scaleX(0.8f).scaleY(0.8f).alpha(0.6f);
        name.animate().scaleX(0.8f).scaleY(0.8f).alpha(0.6f);

    }

}
