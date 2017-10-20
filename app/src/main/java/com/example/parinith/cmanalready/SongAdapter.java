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
 * Created by parinith on 10/15/17.
 */

public class SongAdapter extends ArrayAdapter<MediaMetadataCompat>{

    public SongAdapter(Activity context, ArrayList<MediaMetadataCompat> musicList) {
        super(context, 0, musicList);
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.music_element, parent, false);
        }

        MediaMetadataCompat currentSong = getItem(position);

        TextView songTitle = listItemView.findViewById(R.id.title);
        songTitle.setText(currentSong.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
        //songTitle.setText("Haaki");

        TextView albumTitle = listItemView.findViewById(R.id.description);
        albumTitle.setText(currentSong.getString(MediaMetadataCompat.METADATA_KEY_ALBUM));

        return listItemView;
    }
}
