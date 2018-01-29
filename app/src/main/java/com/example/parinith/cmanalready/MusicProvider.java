package com.example.parinith.cmanalready;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static android.R.id.list;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.os.Build.ID;

/**
 * Created by parinith AND RAGHAV on 10/15/17.
 */

public class MusicProvider extends AsyncTask {

    private static MusicProvider mMusicProviderInstance = new MusicProvider();

    private String TAG = "MusicProvider.CLASS";
    private ArrayList<MediaMetadataCompat> music = new ArrayList<>();

    private TreeMap<String, ArrayList<MediaMetadataCompat>> musicByAlbums = new TreeMap<>();
    private TreeMap<String, ArrayList<MediaMetadataCompat>> musicByArtists = new TreeMap<>();
    private TreeMap<String, ArrayList<MediaMetadataCompat>> musicByPlaylists = new TreeMap<>();
    private static Context mContext;

    public enum State {
        INITIALIZED, INITIALIZING, NOT_INITIALIZED
    };

    public volatile State mCurrentState = State.NOT_INITIALIZED;

    public MusicProvider() {
    }

    public static MusicProvider getMusicProviderInstance(Activity context) {
        mContext = context;
        return mMusicProviderInstance;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        retrieveMusic();
        return null;
    }

    public void retrieveMusic(){
        Log.d(TAG, "retrieveMusic() called");
        mCurrentState = State.INITIALIZING;
        try{
            ContentResolver cr = mContext.getContentResolver();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String selection = MediaStore.Audio.Media.IS_MUSIC;
            String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
            Cursor cursor = cr.query(uri, null, selection, null, sortOrder);
            if (cursor!=null && cursor.getCount()>0) {
                while(cursor.moveToNext()) {
                    music.add(buildMusic(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))));
                }
            }
            //buildAlbums();
            //buildArtists();
            mCurrentState = State.INITIALIZED;
            Log.d(TAG, "retrieveMusic done, State==INITIALIZED");
        } catch (Exception e) {
            Log.e(TAG, "retrieveMusic error");
            mCurrentState=State.NOT_INITIALIZED;
        }
    }

    private MediaMetadataCompat buildMusic(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        String TITLE = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String ALBUM = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        String ARTIST = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        //String TRACK_NUMBER = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER);
        //String DURATION = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        //String ID = String.valueOf(TITLE.hashCode());

        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, TITLE)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, ALBUM)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, ARTIST)
                //.putString(MediaMetadataCompat.METADATA_KEY_DURATION, DURATION)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, TITLE)
                //.putString(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, TRACK_NUMBER)
                .putString("PATH", path)
                .build();
    }

    public ArrayList<MediaMetadataCompat> getMusic() {
        Log.d(TAG, "getMusic() called");
        if(mCurrentState==State.INITIALIZED)
            return music;
        if(mCurrentState!=State.INITIALIZING)
            retrieveMusic();
        while(mCurrentState==State.INITIALIZING)
            continue;
        if(mCurrentState==State.INITIALIZED)
            return music;
        return null;
    }

    private synchronized void buildAlbums() {

        for (MediaMetadataCompat m : music) {
            String album = m.getString(MediaMetadataCompat.METADATA_KEY_ALBUM);
            if(album==null) {
                album="Unknown";
            }
            ArrayList<MediaMetadataCompat> list;
            if (!musicByAlbums.keySet().contains(album)) {
                list = new ArrayList<>();
                list.add(m);
                musicByAlbums.put(album, list);
            }
            else {
                list = musicByAlbums.get(album);
                list.add(m);
            }
        }
    }

    public TreeMap<String, ArrayList<MediaMetadataCompat>> getMusicByAlbums() {
        if(mCurrentState==State.NOT_INITIALIZED)
            retrieveMusic();
        while(mCurrentState==State.INITIALIZING)
            continue;
        if(musicByAlbums.size()==0)
            buildAlbums();
        if(mCurrentState==State.INITIALIZED && musicByAlbums.size()!=0) {
            Log.d(TAG, "musicByAlbums returned");
            return musicByAlbums;
        }
        return null;
    }

    private synchronized void buildArtists() {

        for (MediaMetadataCompat m : music) {
            String artist = m.getString(MediaMetadataCompat.METADATA_KEY_ARTIST);
            if(artist==null) {
                artist="Unknown";
            }
            ArrayList<MediaMetadataCompat> list;
            if (!musicByArtists.keySet().contains(artist)) {
                list = new ArrayList<>();
                list.add(m);
                musicByArtists.put(artist, list);
            }
            else {
                list = musicByArtists.get(artist);
                list.add(m);
            }
        }
    }

    public TreeMap<String, ArrayList<MediaMetadataCompat>> getMusicByArtists() {
        if(mCurrentState==State.NOT_INITIALIZED)
            retrieveMusic();
        while(mCurrentState==State.INITIALIZING)
            continue;
        if(musicByArtists.size()==0)
            buildArtists();
        if(mCurrentState==State.INITIALIZED && musicByArtists.size()!=0) {
            Log.d(TAG, "musicByArtists returned");
            return musicByArtists;
        }
        return null;
    }

    public ArrayList<MediaMetadataCompat> getShuffledMusic(ArrayList<MediaMetadataCompat> inputList) {
        Log.d(TAG, "getShuffledMusic called");
        if (inputList==null) {
            return null;
        }
        ArrayList<MediaMetadataCompat> shuffledList = new ArrayList<>();
        for(MediaMetadataCompat m : inputList) {
            shuffledList.add(m);
        }
        Collections.shuffle(shuffledList);
        return shuffledList;
    }

}
