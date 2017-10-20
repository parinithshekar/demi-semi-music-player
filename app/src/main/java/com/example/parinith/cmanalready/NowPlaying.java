package com.example.parinith.cmanalready;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by parinith on 10/18/17.
 */

public class NowPlaying extends AppCompatActivity {
    private static QueueManager mQueueManager;
    private static PlaybackManager mPlaybackManager;
    private static ArrayList<MediaMetadataCompat> nowPlayingQueue;
    private static MediaMetadataCompat nowPlayingSong;
    private static ImageView nowPlayingImage;
    private static TextView title;
    private static TextView artist;
    private static Button previous;
    private static Button play_pause;
    private static Button next;
    private static ListView nowPlayingQueueView;
    private static MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        mQueueManager = QueueManager.getQueueManagerInstance();
        mPlaybackManager = PlaybackManager.getmPlaybackManagerInstance();
        nowPlayingQueue = mQueueManager.getPresentQueue();
        nowPlayingSong = mQueueManager.getPresentSong();
        mMediaPlayer = mPlaybackManager.mMediaPlayer;

        nowPlayingImage = (ImageView)findViewById(R.id.now_playing_image);
        title = (TextView)findViewById(R.id.now_playing_title);
        artist = (TextView)findViewById(R.id.now_playing_artist);
        previous = (Button)findViewById(R.id.previous);
        play_pause = (Button)findViewById(R.id.play_pause);
        next = (Button)findViewById(R.id.next);
        nowPlayingQueueView = (ListView)findViewById(R.id.now_playing_queue);

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mPlaybackManager.Next();
                update();
            }
        });

        if(nowPlayingQueue!=null) {
            SongAdapter itemsAdapter = new SongAdapter(this, nowPlayingQueue);
            ListView listView = (ListView) findViewById(R.id.now_playing_queue);
            listView.setAdapter(itemsAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(NowPlaying.this, "Song selected", Toast.LENGTH_LONG);
                    mQueueManager.setPresentQueue(nowPlayingQueue, position);
                    mPlaybackManager.Play();
                    update();
                }
            });
        }


        update();

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlaybackManager.Previous();
                update();
            }
        });

        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlaybackManager.Pause();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlaybackManager.Next();
                update();
            }
        });

    }

    private Bitmap getBitmap(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        try {

            byte[] bitData = mmr.getEmbeddedPicture();

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds= true;
            BitmapFactory.decodeByteArray(bitData, 0, bitData.length, opts);
            int srcWidth = opts.outWidth;
            int dstWidth = 128;
            opts.inJustDecodeBounds=false;

            opts.inSampleSize=8;
            opts.inScaled=true;
            opts.inDensity=srcWidth;
            opts.inTargetDensity=dstWidth * opts.inSampleSize;

            return BitmapFactory.decodeByteArray(bitData, 0, bitData.length, opts);
        }catch(Exception e){
            Log.d("FLAC", "Flac files cant extract");
            return null;
        }
    }

    public void update() {
        /**
         * Sets the image and title and artist info based on the current song playing
         */
        nowPlayingQueue = mQueueManager.getPresentQueue();
        nowPlayingSong = mQueueManager.getPresentSong();
        if(nowPlayingQueue!=null) {

            if(nowPlayingSong!=null) {
                Bitmap bitmap = getBitmap(nowPlayingSong.getString("PATH"));
                if(bitmap!=null)
                    nowPlayingImage.setImageBitmap(bitmap);
                else {
                    nowPlayingImage.setBackgroundColor(getResources().getColor(R.color.black_overlay, null));
                }
                title.setText(nowPlayingSong.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
                title.setTextColor(getResources().getColor(R.color.white));
                String nowPlayingArtist = nowPlayingSong.getString(MediaMetadataCompat.METADATA_KEY_ARTIST);
                if(nowPlayingArtist!=null){
                    artist.setText(nowPlayingSong.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
                    artist.setTextColor(getResources().getColor(R.color.white));
                }
            }

            else {
                nowPlayingImage.setBackgroundColor(getResources().getColor(R.color.black_overlay, null));
            }
        }

        else {
            nowPlayingImage.setBackgroundColor(getResources().getColor(R.color.black_overlay, null));
        }
    }
}
