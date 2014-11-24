package com.example.michael.battery_consumption;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by michael on 11/22/14.
 */
public class Brightness {
    private String defaultBrightnessLevel;
    private String defaultBrightnessMode;
    private String defaultScreenOffTime;
    private Context c;
    private ContentResolver cr;
    private Logger logBrightness;

    Brightness(Context mainContext, Logger l){
        c = mainContext;
        cr = c.getContentResolver();
        defaultScreenOffTime = Settings.System.getString(cr,Settings.System.SCREEN_OFF_TIMEOUT);
        defaultBrightnessMode = Settings.System.getString(cr,Settings.System.SCREEN_BRIGHTNESS_MODE);
        defaultBrightnessLevel = Settings.System.getString(cr, Settings.System.SCREEN_BRIGHTNESS);
        logBrightness = l;
    }

    void changeBrightness(int mode){
        if(mode==1){
            defaultScreenOffTime = Settings.System.getString(cr,Settings.System.SCREEN_OFF_TIMEOUT);
            defaultBrightnessMode = Settings.System.getString(cr,Settings.System.SCREEN_BRIGHTNESS_MODE);
            defaultBrightnessLevel = Settings.System.getString(cr, Settings.System.SCREEN_BRIGHTNESS);
            Settings.System.putInt(cr, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            Settings.System.putString(cr, Settings.System.SCREEN_BRIGHTNESS, "255");
            logBrightness.log(Level.INFO,"Set Brightness to 255!");

        }else{
            Settings.System.putString(cr,Settings.System.SCREEN_BRIGHTNESS, defaultBrightnessLevel);
            Settings.System.putString(cr,Settings.System.SCREEN_BRIGHTNESS_MODE, defaultBrightnessMode);
            Settings.System.putString(cr,Settings.System.SCREEN_OFF_TIMEOUT, defaultScreenOffTime);
            logBrightness.log(Level.INFO,"Change Settings to Brightness: " + defaultBrightnessLevel +
            " BrightnessMode " + defaultBrightnessMode + " Screen Time Out " + defaultScreenOffTime);
        }
    }
}
