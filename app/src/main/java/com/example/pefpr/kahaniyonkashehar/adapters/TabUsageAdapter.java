package com.example.pefpr.kahaniyonkashehar.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.activities.TabUsage;
import com.example.pefpr.kahaniyonkashehar.interfaces.ItemClicked;
import com.example.pefpr.kahaniyonkashehar.modalclasses.AttendenceView;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Usage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ameya on 23-Nov-17.
 */

public class TabUsageAdapter extends ArrayAdapter<Usage> {

    public TabUsageAdapter(Context context, ArrayList<Usage> listForAdapter) {
        super(context,0,listForAdapter);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Usage usage = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.usage_result_row, parent, false);
        }
        // Lookup view for data population
        TextView studentRank = (TextView) convertView.findViewById(R.id.studentRank);
        TextView studentName = (TextView) convertView.findViewById(R.id.studentName);
        TextView usageTime = (TextView) convertView.findViewById(R.id.usageTime);
        // Populate the data into the template view using the data object

        studentRank.setText("#"+(position+1));
        studentName.setText(usage.studentName);
        usageTime.setText(usage.usageTime);
        // Return the completed view to render on screen
        return convertView;
    }
}