package com.example.michael.battery_consumption;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by michael on 11/22/14.
 */
public class FlashLight {
    private Camera cam;
    private Logger flashLog;
    private Context c;

    FlashLight(Logger l, Context context){
        flashLog = l;
        c = context;
    }

    void flashLightOn(){
        if(c.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            cam = Camera.open();
            Camera.Parameters params = cam.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            cam.setParameters(params);
            cam.startPreview();
            flashLog.info("Light turned on");
        }else{
            flashLog.log(Level.SEVERE,"No torchlight available");
        }
    }

    void flashLightOff(){
        if(c.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            Camera.Parameters params = cam.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            cam.setParameters(params);
            cam.stopPreview();
            cam.release();
            flashLog.info("Light turned off");
        }
    }

}
