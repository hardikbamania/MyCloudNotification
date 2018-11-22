package com.eaziche.mycloudnotification;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by hardik on 08-04-2017.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
