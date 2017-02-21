package com.appxone.heartrateanimationapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appxone.heartrateanimationapp.FrameUtils.MyBaseAdapter;
import com.appxone.heartrateanimationapp.Utils.FontNames;

import java.util.ArrayList;


/**
 * Created by Nadeem on 5/19/2015.
 */
public class HeartRate_Adapter extends MyBaseAdapter {
    ArrayList<Model_HeartRate> items;
    Activity act;
    LayoutInflater inflater;

    public HeartRate_Adapter(Activity ctx, ArrayList<?> items) {
        super(ctx, items);
        this.items = (ArrayList<Model_HeartRate>) items;
        act = ctx;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onTaskComplete(String result, String key) {
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup container) {
        ViewHolder viewHolder = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = inflater.inflate(R.layout.row_heartrate, container, false);
            ViewHolder viewHolder1 = new ViewHolder();
            viewHolder1.datetime = (TextView) convertView.findViewById(R.id.datetime);
            viewHolder1.heartrate = (TextView) convertView.findViewById(R.id.heartrate);
            viewHolder1.bpm = (TextView) convertView.findViewById(R.id.bpm);
            viewHolder1.bpm.setTypeface(Typeface.createFromAsset(act.getAssets(), FontNames.FONT_ROMAN));
            viewHolder1.datetime.setTypeface(Typeface.createFromAsset(act.getAssets(), FontNames.FONT_ROMAN));
            viewHolder1.heartrate.setTypeface(Typeface.createFromAsset(act.getAssets(), FontNames.FONT_BOLD_COND));
            convertView.setTag(viewHolder1);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.heartrate.setText(items.get(position).getHeartrate());
        viewHolder.datetime.setText(items.get(position).getDatetime());
        return convertView;
    }

    private static class ViewHolder {
        TextView datetime, heartrate, bpm;
    }


}
