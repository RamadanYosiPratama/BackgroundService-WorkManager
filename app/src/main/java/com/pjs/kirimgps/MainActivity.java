package com.pjs.kirimgps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import static com.pjs.kirimgps.MyWorker.WORK_NUMBER_KEY;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST = 112;
    private AppCompatButton btnDoWork;
    private AppCompatTextView tvStatus;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NotificationHandler.oneOffRequest();
        btnDoWork = findViewById(R.id.btnDoWork);
        tvStatus = findViewById(R.id.tvStatus);
        initWorker();

        final PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES).build();
//
        btnDoWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkManager.getInstance(MainActivity.this).enqueue(request);
            }
        });


        WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        String status = workInfo.getState().name();

                        tvStatus.append(status + '\n');
                    }
                });



        AlarmHandler alarmHandler = new AlarmHandler(this);

//        cancel the previous schedule alarms
        alarmHandler.cancelAlarm();
//        set the new alarm after one hour
        alarmHandler.setAlarmManager();

        Toast.makeText(this, "Alarm", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= 26) {
            String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION};
            if (!hasPermissions(MainActivity.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) MainActivity.this, PERMISSIONS, REQUEST );
            } else {
                startService(new Intent(this, MyServices.class));
            }
        } else {
            startService(new Intent(this, MyServices.class));
        }


    }
    private void initWorker() {
        Data data = new Data.Builder()
                .putInt(WORK_NUMBER_KEY, 10)
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .build();

        OneTimeWorkRequest downloadRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInitialDelay(5, TimeUnit.HOURS)
                .setInputData(data)
                .setConstraints(constraints)
                .addTag("download")
                .build();

        WorkManager.getInstance(this).enqueue(downloadRequest);

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                MyWorker.class,
                1,
                TimeUnit.DAYS
        ).setInputData(data)
                .setConstraints(constraints)
//                .setInitialDelay(2, TimeUnit.HOURS)
                .addTag("daily_notification")
                .build();

//        WorkManager.getInstance(this).enqueue(periodicWorkRequest);

//        WorkManager.getInstance(this).getWorkInfosByTagLiveData("download").observe(this,
//                new Observer<List<WorkInfo>>() {
//                    @Override
//                    public void onChanged(List<WorkInfo> workInfos) {
//                        for (WorkInfo w: workInfos) {
//                            Log.d(TAG, "onChanged: Work status: " + w.getState());
//                        }
//                    }
//                });

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(downloadRequest.getId()).observe(this,
                new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        Log.d(TAG, "onChanged: Work status: " + workInfo.getState());
                    }
                });

        WorkManager.getInstance(this).cancelWorkById(downloadRequest.getId());
    }
    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startService(new Intent(this, MyServices.class));
                } else {
                    Toast.makeText(MainActivity.this, "The app was not allowed to access your location", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


}