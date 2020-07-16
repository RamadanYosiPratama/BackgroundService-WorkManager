package com.pjs.kirimgps;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.TimerTask;

public class MyWorker extends Worker {
    MyApp MAPP;
    private static final String TAG = "MyWorker";

    public static final String WORK_NUMBER_KEY = "number";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }



    @NonNull
    @Override
    public Result doWork() {
        displayNotification("Kirim GPS", "Coba Track Demo GPS");


        Data inputData = getInputData();
        int number = inputData.getInt(WORK_NUMBER_KEY, -1);
        if (number != -1) {
            for (int i=0; i<number; i++) {
                Log.d(TAG, "doWork: i was: " + i);
                MAPP.cobaGPS();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return Result.failure();
                }
             }
            }

        return Result.success();

    }


        private void displayNotification(String task, String desc) {

        NotificationManager manager =(NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("Rama", "Rama",NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            Intent intent = new Intent(getApplicationContext(), MyServices.class);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Rama")
                .setContentTitle(task)
                .setContentText(desc)
                .setSmallIcon(R.drawable.ic_android);

        manager.notify(1,builder.build());
    }
}
