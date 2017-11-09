package com.example.parinith.cmanalready;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import static android.os.Build.VERSION_CODES.N;

/**
 * Created by parinith on 10/15/17.
 */

public class PlaybackManager {

    String TAG = "PlabackManager.CLASS";

    private static QueueManager mQueueManager = QueueManager.getQueueManagerInstance();
    private static PlaybackManager mPlaybackManagerInstance = new PlaybackManager(mQueueManager);
    MediaMetadataCompat mPresentSong;
    public MediaPlayer mMediaPlayer = new MediaPlayer();


    private PlaybackManager(QueueManager qm) {
        mQueueManager = qm;
        Log.d(TAG, "MediaPlayer creating");
        mMediaPlayer.reset();
        Log.d(TAG, "MediaPlayer created");
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Next();
            }
        });
    }

    public static PlaybackManager getmPlaybackManagerInstance() {
        return mPlaybackManagerInstance;
    }

    public void Play() {
        Log.d(TAG, "Play called");
        mPresentSong = mQueueManager.getPresentSong();
        if(mPresentSong == null){
            Log.d(TAG, "presentSong received NULL");
            return;
        }
        else {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            try {
                Log.d(TAG, "DataSource setting");
                mMediaPlayer.setDataSource(mPresentSong.getString("PATH"));
                Log.d(TAG, "DataSource set");
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException Exception) {
                Log.d(TAG, "onPlay-SetDataSource");
            }

        }
    }

    public void Pause() {
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
        }
        else {
            mMediaPlayer.start();
        }
    }

    public void Stop() {
        mMediaPlayer.stop();
    }

    public void Next() {
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mPresentSong=mQueueManager.getNextSong();
        if(mPresentSong==null)
            return;
        try {
            mMediaPlayer.setDataSource(mPresentSong.getString("PATH"));
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            Log.d(TAG, "on-Next");
        }
    }

    public void Previous() {
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mPresentSong=mQueueManager.getPreviousSong();
        if(mPresentSong==null)
            return;
        try {
            mMediaPlayer.setDataSource(mPresentSong.getString("PATH"));
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            Log.d(TAG, "on-Next");
        }
    }

}
