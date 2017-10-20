package com.example.parinith.cmanalready;

import android.app.Activity;
import android.support.v4.media.MediaMetadataCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by parinith on 10/17/17.
 */

public class InfoAdapter extends ArrayAdapter<String> {

    public InfoAdapter(Activity context, ArrayList<String> infoList) {
        super(context, 0, infoList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.music_element, parent, false);
        }

        String currentElement = getItem(position);

        TextView songTitle = listItemView.findViewById(R.id.title);
        songTitle.setText(currentElement);

        TextView albumTitle = listItemView.findViewById(R.id.description);
        albumTitle.setText("");

        return listItemView;
    }
}
