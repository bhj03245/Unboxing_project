package com.example.ub_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ub_test.list.ImptList;
import com.example.ub_test.list.ManlList;
import com.example.ub_test.list.NormList;
import com.example.ub_test.list.ParkList;
import com.example.ub_test.parking.ParkLocation;
import com.example.ub_test.setting.UserSetting;

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

public class MainMenu extends AppCompatActivity {

    Button btn_streaming;
    Button btn_parkLocation;
    Button btn_normList;
    Button btn_manlList;
    Button btn_parkList;
    Button btn_imptList;

    ImageView iv_setting;

    final static String trig = "trigger";
    String str_lat, str_lng;
    float lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btn_streaming = (Button)findViewById(R.id.real_streaming);
        btn_parkLocation = (Button)findViewById(R.id.park_location);
        btn_normList = (Button)findViewById(R.id.norm_list);
        btn_manlList = (Button)findViewById(R.id.manl_list);
        btn_parkList = (Button)findViewById(R.id.park_list);
        btn_imptList = (Button)findViewById(R.id.impt_list);
        iv_setting = (ImageView)findViewById(R.id.settings);

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

        lat = Float.parseFloat(str_lat);
        lng = Float.parseFloat(str_lng);

        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, UserSetting.class);
                startActivity(intent);
            }
        });

        btn_streaming.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainMenu.this, Streaming.class);
                //startActivity(intent);
            }
        });

        btn_parkLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, ParkLocation.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                startActivity(intent);
            }
        });

        btn_normList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, NormList.class);
                startActivity(intent);
            }
        });

        btn_manlList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, ManlList.class);
                startActivity(intent);
            }
        });

        btn_parkList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, ParkList.class);
                startActivity(intent);
            }
        });

        btn_imptList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, ImptList.class);
                startActivity(intent);
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
                URL url = new URL(getString(R.string.ip) + "/apkCtrl/gps_apk.php");
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