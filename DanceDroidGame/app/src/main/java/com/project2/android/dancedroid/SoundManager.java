package com.project2.android.dancedroid;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

/**
 * Created by Ashley on 11/17/2015.
 */
public class SoundManager extends Activity {

    private static final String TAG = "SoundManager";

    boolean loaded = false, play = false;

    SoundPool sp = new SoundPool(50000, AudioManager.STREAM_MUSIC, 0);
    int musicID;
    public SoundManager(int id, Context context){
        musicID = sp.load(context, id, 1);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
               loaded = true;
                Log.d(TAG, "music loaded");
        }
        });
    }

    public void PlayMusic(float speed){
        if(loaded) {
            sp.play(musicID, 1, 1, 1, -1, speed);
            Log.d(TAG, "Trying to play");
        }
    }
}
