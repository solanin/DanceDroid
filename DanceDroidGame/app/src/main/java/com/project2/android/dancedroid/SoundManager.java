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

    private static SoundManager sSoundManager;
    private static final String TAG = "SoundManager";

    //TODO - find a way to make sound less buzzy
    SoundPool sp = new SoundPool(500000000, AudioManager.STREAM_MUSIC, 0);
    int musicID;
    public SoundManager(){

    }

    public void SetMusic(int id, Context context){
        musicID = sp.load(context, id, 1);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                Log.d(TAG, "music loaded");
            }
        });
    }

    public void PlayMusic(float speed){
        sp.play(musicID, 1, 1, 1, -1, speed);
        Log.d(TAG, "Trying to play");
    }

    //when app is closed, music changes
    public void StopMusic(){
        sp.autoPause();
    }

    public void ResumeMusic(){
        sp.autoResume();
    }

    public static SoundManager getInstance()
    {
        if (sSoundManager == null)
        {
            sSoundManager = new SoundManager();
        }
        return sSoundManager;
    }
}
