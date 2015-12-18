package com.project2.android.dancedroid;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.ToneGenerator;
import android.util.Log;

/**
 * Created by Ashley on 11/17/2015.
 */
public class SoundManager extends Activity {

    private static SoundManager sSoundManager;
    private static final String TAG = "SoundManager";

    //TODO - find a way to make sound less buzzy
    MediaPlayer backgrnd1;
    public SoundManager(){

    }

    public void SetMusic(int id, Context context){
        backgrnd1 = MediaPlayer.create(context, id);
    }

    public void PlayMusic(float speed){
        backgrnd1.start();
        backgrnd1.setLooping(true);
        Log.d(TAG, "Trying to play");
    }

    public void Beep(){
        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
        tg.startTone(ToneGenerator.TONE_PROP_BEEP);
    }

    //when app is closed, music changes
    public void StopMusic(){
        backgrnd1.stop();
    }

    public void ResumeMusic(){
        backgrnd1.start();
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
