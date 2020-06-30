package com.example.reportservicetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        boolean checked = intent.getBooleanExtra("toAlarmChecked", false);
        System.out.println("알람 모드 체크 = " + checked);
        if(checked) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent in = new Intent(context, ReportService.class);
                context.startForegroundService(in);
            } else {
                Intent in = new Intent(context, ReportService.class);
                context.startService(in);
            }
        }
    }
}
