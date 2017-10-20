package com.example.parinith.cmanalready;

import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.ContentValues.TAG;
import static java.util.Collections.EMPTY_LIST;

/**
 * Created by parinith on 10/15/17.
 */

public class QueueManager {

    private static QueueManager mQueueManagerInstance = new QueueManager();

    private String TAG = "QueueManager.CLASS";

    private ArrayList<MediaMetadataCompat> presentQueue;
    private MediaMetadataCompat presentSong;
    private MediaMetadataCompat nextSong;
    private MediaMetadataCompat previousSong;
    private int presentIndex;

    private QueueManager() {
        presentQueue=null;
        previousSong=null;
        nextSong=null;
        previousSong=null;
        presentIndex=0;
    }

    public static QueueManager getQueueManagerInstance() {
        return mQueueManagerInstance;
    }

    private void setPresentSong() {
        Log.d(TAG, "setPresentSong called");
        presentSong = presentQueue.get(presentIndex);
        Log.d(TAG, "Song from index fetched");
        if(presentIndex==presentQueue.size()-1) {
            nextSong = null;
            Log.d(TAG, "Next Song Set NULL");
        } else {
            nextSong = presentQueue.get(presentIndex + 1);
            Log.d(TAG, "Next Song Set");
        }
        if(presentIndex==0) {
            previousSong = null;
            Log.d(TAG, "Prev Song Set NULL");
        } else {
            previousSong = presentQueue.get(presentIndex -1);
            Log.d(TAG, "Prev Song Set");
        }
    }

    public MediaMetadataCompat getPresentSong() {
        if(presentQueue==null){
            Log.d(TAG, "presentQueue is NULL");
            return null;
        }
        setPresentSong();
        return presentSong;
    }

    public MediaMetadataCompat getNextSong() {
        if(presentQueue==null || nextSong==null)
            return null;
        presentIndex++;
        setPresentSong();
        return presentSong;
    }

    public MediaMetadataCompat getPreviousSong() {
        if(presentQueue==null || previousSong==null)
            return null;
        presentIndex--;
        setPresentSong();
        return presentSong;
    }

    public void setPresentQueue(ArrayList<MediaMetadataCompat> inputQueue, int inputIndex) {
        Log.d(TAG, "setPresentQueue called");
        presentQueue = inputQueue;
        presentIndex = inputIndex;
    }

    public ArrayList<MediaMetadataCompat> getPresentQueue() {
        if(presentQueue.size()!=0)
            return presentQueue;
        return null;
    }
}