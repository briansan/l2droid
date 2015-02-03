//
// title: AudioService
// by: Brian Kim
// description: a service that has the capability to play and pause music
//

package com.breadtech.audioservicehw;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Binder;
import android.provider.MediaStore;

/**
 * Created by bk on 11/4/14.
 */
public class AudioService extends Service
{
    //
    // instance variables
    //
    private MediaPlayer mp = new MediaPlayer();
    private final IBinder binder = new AudioBinder();
    private int length = 0;

    //
    // instance methods
    //
    public void play()
    {
        // make sure the player isn't already playing
        if (!this.mp.isPlaying()) {
            this.mp.seekTo(length);
            this.mp.start();
        }
    }

    public void pause()
    {
        if (this.mp.isPlaying()) {
            this.mp.pause();
            this.length = this.mp.getCurrentPosition();
        }
    }

    //
    // accessor method
    public MediaPlayer getPlayer() { return this.mp; }


    //
    // from Context
    //
    @Override
    public void onCreate()
    {
        super.onCreate();
        this.mp = MediaPlayer.create( this, R.raw.fightsong );
    }

    //
    // from Service
    //
    @Override
    public IBinder onBind( Intent i )
    {
        return binder;
    }

    //
    // inner binder class
    //
    public class AudioBinder extends Binder
    {
        public AudioService getService() { return AudioService.this; }
    }

}
