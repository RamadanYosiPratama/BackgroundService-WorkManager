package com.pjs.kirimgps;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class MyServices extends Service {
    private final static String TAG = "APPLOG";
    public static final int notify = 1000;  //every 5 Minute 600000
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    MyApp MAPP;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        MAPP = new MyApp(this);
        if (mTimer != null)
            mTimer.cancel();
        else
            mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);   //Schedule task
        super.onCreate();
        Log.d("TAG", "Service Started");

        AlarmHandler alarmHandler = new AlarmHandler(this);
//        cancel the previous schedule alarms
        alarmHandler.cancelAlarm();
//        set the new alarm after one hour
        alarmHandler.setAlarmManager();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TAG", "Service Started");
        return super.onStartCommand(intent, flags, startId);

    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d("TAG", "Service started.");
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mTimer.cancel();    //For Cancel Timer
//        Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show();
//    }
//
    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // display toast
                    Log.d(TAG,"Services Runing");
                    MAPP.cobaGPS();
                    //cobaGps();
                    Toast.makeText(MyServices.this, "Service is running", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
