package com.example.workmanagertest;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ReportWorker extends Worker {


    private Context mContext;

    public ReportWorker(@NonNull Context context, @NonNull WorkerParameters workerParameters){
        super(context, workerParameters);
        mContext = context;
    }

    @Override
    public Worker.Result doWork(){

        Context context = getApplicationContext();

        //Async로 계속해서 받아오는데 trigger를 만나면 문자메세지가 보내지게 하기


        return Result.retry();
    }
}
