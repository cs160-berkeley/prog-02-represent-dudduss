package com.example.sampathduddu.eagleeye;

import android.support.wearable.view.WearableListView;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.TextView;
import android.widget.ImageView;
import java.util.List;

/**
 * Created by sampathduddu on 3/2/16.
 */
public class CongressAdapter extends WearableListView.Adapter {

    private final Context context;
    private final List<Congressman> items;

    public CongressAdapter(Context context, List<Congressman> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new WearableListView.ViewHolder(new CongressmanItemView(context));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder viewHolder, final int position) {
        CongressmanItemView SettingsItemView = (CongressmanItemView) viewHolder.itemView;
        final Congressman item = items.get(position);

        TextView textView = (TextView) SettingsItemView.findViewById(R.id.text);
        textView.setText(item.name);

//        final ImageView imageView = (ImageView) SettingsItemView.findViewById(R.id.image);
//        imageView.setImageResource(item.imageRes);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
