package com.example.parinith.cmanalready;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;

import static com.example.parinith.cmanalready.R.id.albums;

/**
 * Created by parinith on 10/15/17.
 */

public class Albums extends AppCompatActivity{

    //private static final PlaybackManager mPlaybackManager = PlaybackManager.getmPlaybackManagerInstance();
    //private static final QueueManager m
    private MusicProvider mMusicProvider = MusicProvider.getMusicProviderInstance(this);
    private static PresentContent mPresentContent = PresentContent.getPresentContentInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

        final TreeMap<String, ArrayList<MediaMetadataCompat>> mMusicByAlbums = mMusicProvider.getMusicByAlbums();
        ArrayList<String> albums = new ArrayList<String>(mMusicByAlbums.keySet());

        final InfoAdapter itemsAdapter = new InfoAdapter(this, albums);
        ListView listView = (ListView)findViewById(R.id.albums);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresentContent.setPresentContentQueue(mMusicByAlbums.get(itemsAdapter.getItem(position)));
                startActivity(new Intent(Albums.this, ContentView.class));
            }
        });
    }
}
