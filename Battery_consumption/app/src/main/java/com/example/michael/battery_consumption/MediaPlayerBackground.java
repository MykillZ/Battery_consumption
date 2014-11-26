package com.example.michael.battery_consumption;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.view.WindowManager;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by michael on 11/26/14.
 */
public class MediaPlayerBackground extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnCompletionListener{
    private Logger movieLogger;
    MediaPlayer mP;
    AudioManager audioManager;
    Context main;

    public MediaPlayerBackground(Logger l, Context c){
        super();
        movieLogger = l;
        main = c;
        audioManager = (AudioManager) main.getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            movieLogger.log(Level.INFO, "Could not get Audiofocus.");
        }
    }



    public void playMovie(){
        movieLogger.log(Level.INFO,"Initialise MediaPlayer");
        mP = new MediaPlayer();
        String url = "https://ia600401.us.archive.org/19/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
        mP.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mP.setLooping(true);
        mP.setOnPreparedListener(this);
        mP.setOnErrorListener(this);
        try {
            mP.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mP.prepareAsync(); // might take long! (for buffering, etc)
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void stopMovie(){
        movieLogger.log(Level.INFO,"Stop MediaPlayer");
        mP.stop();
        mP.release();
        mP = null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        movieLogger.log(Level.INFO,"start movie");
        mP.start();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        movieLogger.log(Level.INFO, "Error: " + what + " Extra " + extra);
        return false;
    }

    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mP == null) playMovie();
                else if (!mP.isPlaying()) mP.start();
                mP.setVolume(1.0f, 1.0f);
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if(mP != null) {
                    if (mP.isPlaying()) mP.stop();
                    movieLogger.log(Level.INFO, "MediaPlayer lost Audio focus");
                    mP.release();
                }
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if(mP != null) {
                    if (mP.isPlaying()) mP.pause();
                }
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if(mP !=null) {
                    if (mP.isPlaying()) mP.setVolume(0.1f, 0.1f);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mP.release();
        mP = null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        this.playMovie();
    }
}
