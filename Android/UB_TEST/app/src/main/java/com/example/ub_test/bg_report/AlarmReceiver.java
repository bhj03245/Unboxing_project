package com.example.ub_test.bg_report;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AlarmReceiver extends BroadcastReceiver {
    Intent in;

    @Override
    public void onReceive(Context context, Intent intent) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            in = new Intent(context, RestartService.class);
            context.startForegroundService(in);
        } else {
            in = new Intent(context, RealService.class);
            context.startService(in);
        }
    }
}
