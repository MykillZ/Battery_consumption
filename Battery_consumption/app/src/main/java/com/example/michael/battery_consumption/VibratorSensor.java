package com.example.michael.battery_consumption;

import android.content.Context;
import android.os.Vibrator;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by michael on 11/28/14.
 */
public class VibratorSensor {

    private BatteryMain main;
    private Vibrator v;
    private Logger vibrateLogger;

    public VibratorSensor(BatteryMain bMain, Logger l){
        main = bMain;
        v = (Vibrator)main.getSystemService(Context.VIBRATOR_SERVICE);
        vibrateLogger = l;
    }

    public void enableVibrate(){
       if(v.hasVibrator()){
           long[] pattern = {0, 30000, 0};
           v.vibrate(pattern,0);
           vibrateLogger.log(Level.INFO,"Activate Vibrator ^^");
       }else{
           vibrateLogger.log(Level.INFO,"No Vibrator");
       }

    }

    public void disableVibrate(){
        if(v.hasVibrator()){
            v.cancel();
            vibrateLogger.log(Level.INFO,"Disable Vibrator");
        }

    }
}
