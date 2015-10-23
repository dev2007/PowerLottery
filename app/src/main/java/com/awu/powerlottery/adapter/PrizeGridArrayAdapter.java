package com.awu.powerlottery.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.awu.powerlottery.R;

/**
 * Created by awu on 2015-10-23.
 */
public class PrizeGridArrayAdapter extends ArrayAdapter {
    private int mResource;
    private LayoutInflater inflater;
    private String[] dataArray;

    public PrizeGridArrayAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        this.mResource = resource;
        inflater = LayoutInflater.from(context);
        this.dataArray = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            View v = inflater.inflate(mResource, null);
            convertView = v.findViewById(R.id.tv_txt);
        }
        TextView tv = ((TextView) convertView);
        tv.setText(dataArray[position]);

        if (position == 6 || position == 10)
            tv.setTextColor(Color.rgb(255, 0, 0));
        return convertView;
    }
}
