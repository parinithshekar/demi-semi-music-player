package com.example.parinith.cmanalready;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

/**
 * Created by parinith on 10/21/17.
 */

public class ContentView extends AppCompatActivity {

    PresentContent mPresentContent = PresentContent.getPresentContentInstance();
    Button shuffle;
    ListView contentView;
    QueueManager mQueueManager = QueueManager.getQueueManagerInstance();
    PlaybackManager mPlaybackManager = PlaybackManager.getmPlaybackManagerInstance();
    MusicProvider mMusicProvider = MusicProvider.getMusicProviderInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_content);

        shuffle = (Button)findViewById(R.id.shuffle);
        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQueueManager.setPresentQueue(mMusicProvider.getShuffledMusic(mPresentContent.getPresentContentQueue()), 0);
                mPlaybackManager.Play();
            }
        });

        SongAdapter itemsAdapter = new SongAdapter(this, mPresentContent.getPresentContentQueue());
        contentView = (ListView)findViewById(R.id.content_list);
        contentView.setAdapter(itemsAdapter);

        contentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mQueueManager.setPresentQueue(mPresentContent.getPresentContentQueue(), position);
                mPlaybackManager.Play();
            }
        });
    }
}
