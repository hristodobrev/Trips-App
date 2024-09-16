package com.example.trips;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<Trip> {
    private Context context;
    private int resource;

    public CustomArrayAdapter(@NonNull Context context, int resource,
                              @NonNull List<Trip>
                                      objects
    ) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Trip trip = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    resource, parent, false
            );
        }

        ((TextView) (convertView.findViewById(R.id.listViewId))).setText(trip.Id);
        ((TextView) (convertView.findViewById(R.id.listViewTitle))).setText(trip.Title);
        ((TextView) (convertView.findViewById(R.id.listViewDescription))).setText(trip.Description);
        ((TextView) (convertView.findViewById(R.id.listViewDate))).setText(trip.Date);
        ((TextView) (convertView.findViewById(R.id.listViewPicture))).setText(trip.Picture);
        ((TextView) (convertView.findViewById(R.id.listViewLocation))).setText(trip.Location);

        return convertView;
    }

}