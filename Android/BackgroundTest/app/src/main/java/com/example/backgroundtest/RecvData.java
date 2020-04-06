package com.example.backgroundtest;


import android.content.Context;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RecvData extends AsyncTask<String, Void, String> {
    Context mContext;

    public RecvData(Context context){
        mContext= context;
    }


    private static String SERVER_URL = "http://211.216.137.157/apkCtrl/reportImpt_apk.php";

    String data = "";

    HttpURLConnection httpURLConnection;

    @Override
    protected String doInBackground(String... unused) {


        try {
            URL url = new URL(SERVER_URL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            int responseStatusCode = httpURLConnection.getResponseCode();


            InputStream inputStream;
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                inputStream = httpURLConnection.getErrorStream();
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();

            data = sb.toString();

            return data;

            //Log.e(TAG, data);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }

        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Log.d(TAG, "recvData: " + s);

        if(s.equals("Impt")){
            final String phone = "010XXXXXXXX";
            final String contents = "테스트 성공띠";

            try{
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone, null, contents, null, null);
                Toast.makeText(mContext, "Send Succeeded",Toast.LENGTH_LONG).show();

            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            Log.d(TAG, "recvData: " + s);
        }

    }

}