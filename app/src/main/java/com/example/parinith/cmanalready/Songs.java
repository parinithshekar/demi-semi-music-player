package com.example.parinith.cmanalready;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.os.Build.ID;

/**
 * Created by parinith on 10/15/17.
 */

public class Songs extends AppCompatActivity {

    String TAG = "Songs.CLASS";

    private MusicProvider mMusicProvider = MusicProvider.getMusicProviderInstance(this);
    private ArrayList<MediaMetadataCompat> songList;
    private QueueManager mQueueManager = QueueManager.getQueueManagerInstance();
    private static final PlaybackManager mPlaybackManager = PlaybackManager.getmPlaybackManagerInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        songList = mMusicProvider.getMusic();

        Button shuffleButton = (Button)findViewById(R.id.shuffle);
        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQueueManager.setPresentQueue(mMusicProvider.getShuffledMusic(songList), 0);
                mPlaybackManager.Play();
            }
        });

        SongAdapter itemsAdapter = new SongAdapter(this, songList);
        ListView listView = (ListView) findViewById(R.id.songsList);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Songs.this, "Song selected", Toast.LENGTH_LONG);
                mQueueManager.setPresentQueue(songList, position);
                mPlaybackManager.Play();
            }
        });
    }
}
