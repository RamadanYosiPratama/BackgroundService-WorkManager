package com.pjs.kirimgps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.pjs.kirimgps.MyServices;

public class BootService extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent arg1) {
        Intent intent = new Intent(context, MyServices.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);

        }
        Log.i("Autostart", "started");
        }



    }

