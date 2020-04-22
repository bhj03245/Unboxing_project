package com.example.googlemaptest;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity{

    Button btn;

    final static String trig = "trigger";
    String str_lat, str_lng;
    float lat, lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.btn);

        GetGPS getGPS = new GetGPS();
        try {
            String result = getGPS.execute().get();
            //System.out.println("result" + result);

            String[] gps = result.split("&");
            str_lat = gps[0];
            str_lng = gps[1];
            //Log.e(TAG, gps[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        //System.out.println("lat=" + str_lat);
        //System.out.println("lng=" + str_lng);

        lat = Float.parseFloat(str_lat);
        lng = Float.parseFloat(str_lng);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, ParkLocation.class);
                in.putExtra("lat", lat);
                in.putExtra("lng", lng);
                startActivity(in);
            }
        });
    }

    public class GetGPS extends AsyncTask<Void, Void, String> {

        String data = "";
        HttpURLConnection conn;

        @Override
        protected String doInBackground(Void... unused) {
            String param= "request_gps"+ "=" + trig;
            try{
                URL url = new URL("http://211.216.137.157/apkCtrl/gps_apk.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                int responseStatusCode = conn.getResponseCode();

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = conn.getInputStream();
                }
                else{
                    inputStream = conn.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine())!= null){
                    sb.append(line);
                }
                bufferedReader.close();

                data = sb.toString();
                Log.e(TAG, data);

                return data;


            }catch(MalformedURLException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                conn.disconnect();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String data){
            super.onPostExecute(data);
            Log.e(TAG, data);
        }
    }

}
