package com.example.parinith.cmanalready;

import android.media.MediaPlayer;
import android.support.v4.media.MediaMetadataCompat;

import java.util.ArrayList;

/**
 * Created by parinith on 10/21/17.
 */

public class PresentContent {
    private static PresentContent mPresentContentInstance = new PresentContent();
    ArrayList<MediaMetadataCompat> presentContentQueue;

    private PresentContent() {}

    public static PresentContent getPresentContentInstance() {
        return mPresentContentInstance;
    }

    public void setPresentContentQueue(ArrayList<MediaMetadataCompat> inputQueue) {
        presentContentQueue = inputQueue;
    }

    public ArrayList<MediaMetadataCompat> getPresentContentQueue() {
        if(presentContentQueue==null)
            return new ArrayList<MediaMetadataCompat>();
        return presentContentQueue;
    }
}
