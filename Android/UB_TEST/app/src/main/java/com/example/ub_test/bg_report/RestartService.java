package com.example.ub_test.bg_report;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.ub_test.Login;
import com.example.ub_test.R;

import static android.content.ContentValues.TAG;


public class RestartService extends Service {


    boolean modeChecked = false;


    public RestartService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("신고 기능");
        builder.setContentText(null);
        Intent notificationIntent = new Intent(this, Login.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_NONE));
        }

        Notification notification = builder.build();


        startForeground(9, notification);

        /////////////////////////////////////////////////////////////////////
        //modeChecked = intent.getBooleanExtra("modeChecked", false);
        //String modeChecked = intent.getStringExtra("modeChecked");

        SharedPreferences data = getSharedPreferences("switch_data", MODE_PRIVATE);
        modeChecked = data.getBoolean("switchkey", false);

        //modeChecked = intent.getBooleanExtra("modeChecked", false);
        Log.d(TAG, "Checked! = " + modeChecked);
        System.out.println("모드 체크 = " + modeChecked);

        Intent in = new Intent(this, RealService.class);

        if (modeChecked == true) {

            startService(in);
            stopForeground(true);
            stopSelf();
        } else{
            if (RealService.serviceIntent != null) {
                stopService(in);
            }
        }

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
