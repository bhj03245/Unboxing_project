package com.example.googlemaptest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import static android.content.ContentValues.TAG;

public class GetGPS extends AsyncTask<String, Void, String> {

    Context mContext;
    final static String trig = "trigger";
    String data = "";
    ProgressDialog progressDialog;

    public GetGPS(Context context){mContext = context;}

    @Override
    protected String doInBackground(String... unused) {
        String param= "request_gps=" + trig;
        try{
            URL url = new URL("http://211.216.137.157/apkCtrl/gps_apk.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();

            OutputStream outs = conn.getOutputStream();
            outs.write(param.getBytes("UTF-8"));
            outs.flush();
            outs.close();

            InputStream is = null;
            BufferedReader in = null;

            is = conn.getInputStream();
            in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
            String line = null;
            StringBuffer buff = new StringBuffer();
            while ((line = in.readLine()) != null) {
                buff.append(line + "\n");
            }
            data = buff.toString();
            Log.e(TAG, data);
            return data;



        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(mContext, "잠시만 기다려주세요", null, true, true);
    }

    @Override
    protected void onPostExecute(String data){
        super.onPostExecute(data);
        progressDialog.dismiss();

        String[] gps = data.split("/");

        Log.e(TAG, gps[0]);

        Intent in = new Intent(mContext, ParkLocation.class);
        in.putExtra("lat", gps[0]);
        in.putExtra("lng", gps[1]);
        mContext.startActivity(in);
    }
}
