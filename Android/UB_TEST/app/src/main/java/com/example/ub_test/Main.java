package com.example.ub_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.ub_test.bg_report.RealService;
import com.example.ub_test.bg_report.RecvImpt;
import com.example.ub_test.bg_report.RestartService;
import com.example.ub_test.list.ImptList;
import com.example.ub_test.list.ManlList;
import com.example.ub_test.list.NormList;
import com.example.ub_test.list.ParkList;
import com.example.ub_test.parking.ParkLocation;
import com.example.ub_test.setting.ReportSetting;
import com.example.ub_test.setting.UserSetting;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public class Main extends AppCompatActivity {

    private Context context = this;
    private Intent serviceIntent;


    Switch mode_switch;

    SharedPreferences data;
    boolean load_mode;

    final static String trig = "trigger";
    String str_lat, str_lng;
    float lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mode_switch = (Switch) findViewById(R.id.mode_switch);

        mode_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (RealService.serviceIntent == null) {
                        serviceIntent = new Intent(context, RestartService.class);
                        startService(serviceIntent);
                        Toast.makeText(context, "주행모드", Toast.LENGTH_SHORT).show();

                    } else {
                        serviceIntent = RealService.serviceIntent;//getInstance().getApplication();
                        Toast.makeText(Main.this, "already started", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    stopService(serviceIntent);
                    Toast.makeText(context, "주차모드", Toast.LENGTH_SHORT).show();


                }

                System.out.println("isCecked 확인" + isChecked);
                if(isChecked == false) {
                    RecvImpt recvImpt = new RecvImpt(Main.this);
                    recvImpt.execute();
                    ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                    for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
                        if(RealService.class.getName().equals(service.service.getClassName())){
                            stopService(serviceIntent);
                        }
                    }
                }

                SharedPreferences data = getSharedPreferences("switch_data", MODE_PRIVATE);
                SharedPreferences.Editor editor = data.edit();
                editor.putBoolean("switchkey", isChecked);
                editor.commit();


            }
        });

        data = getSharedPreferences("switch_data", MODE_PRIVATE);
        load_mode = data.getBoolean("switchkey", false);
        mode_switch.setChecked(load_mode);

        Main.GetGPS getGPS = new Main.GetGPS();
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

        InitializeLayout();
    }
    public void InitializeLayout(){

        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        setSupportActionBar(toolbar);

        //App Bar의 좌측 영영에 Drawer를 Open 하기 위한 Incon 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_icon);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.closed
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.user_set:
                        Intent toUser = new Intent(Main.this, UserSetting.class);
                        startActivity(toUser);
                        break;
                    case R.id.report_set:
                        Intent toReport = new Intent(Main.this, ReportSetting.class);
                        startActivity(toReport);
                        break;
                    case R.id.park_loc:
                        Intent toParcLoc = new Intent(Main.this, ParkLocation.class);
                        toParcLoc.putExtra("lat", lat);
                        toParcLoc.putExtra("lng", lng);
                        startActivity(toParcLoc);
                        break;
                    case R.id.norm_sel:
                        Intent toNorm = new Intent(Main.this, NormList.class);
                        startActivity(toNorm);
                        break;
                    case R.id.manl_sel:
                        Intent toManl = new Intent(Main.this, ManlList.class);
                        startActivity(toManl);
                        break;
                    case R.id.park_sel:
                        Intent toPark = new Intent(Main.this, ParkList.class);
                        startActivity(toPark);
                        break;
                    case R.id.impt_sel:
                        Intent toImpt = new Intent(Main.this, ImptList.class);
                        startActivity(toImpt);
                        break;
                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
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
