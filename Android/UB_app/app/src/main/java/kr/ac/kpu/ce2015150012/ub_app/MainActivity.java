package kr.ac.kpu.ce2015150012.ub_app;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kr.ac.kpu.ce2015150012.ub_app.background_report.RealService;

public class MainActivity extends AppCompatActivity {

    EditText et_id;
    EditText et_pw;
    Button btn_login;
    String str_id;
    String str_pw;

    private Intent serviceIntent;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int MULTIPLE_PERMISSIONS = 101;

    private String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.SEND_SMS
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_id = (EditText)findViewById(R.id.id);
        et_pw = (EditText)findViewById(R.id.password);
        btn_login = (Button)findViewById(R.id.login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    str_id = et_id.getText().toString();
                    str_pw = et_pw.getText().toString();
                }catch(NullPointerException e){
                    Log.e("error", e.getMessage());
                }
                DB_login DBL = new DB_login();
                DBL.execute();
            }
        });

        //ArrayList<String> permissions = new ArrayList<String>();

        //int permissionCheck_wes =  ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //int permissionCheck_sms = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if(Build.VERSION.SDK_INT >= 23){
            checkPermissions();
        }

        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE);
        boolean isWhiteListing = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            isWhiteListing = pm.isIgnoringBatteryOptimizations(getApplicationContext().getPackageName());
        }
        if (!isWhiteListing) {
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivity(intent);
        }

        if (RealService.serviceIntent==null) {
            serviceIntent = new Intent(this, RealService.class);
            startService(serviceIntent);
        } else {
            serviceIntent = RealService.serviceIntent;//getInstance().getApplication();
            Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show();
        }


        /*if(permissionCheck_sms == PackageManager.PERMISSION_GRANTED && permissionCheck_wes == PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.SEND_SMS);



        }else{
            String[] rePermissionArray = new String[permissions.size()];
            rePermissionArray = permissions.toArray(rePermissionArray);
            ActivityCompat.requestPermissions(this, rePermissionArray, PERMISSION_REQUEST_CODE);
            //ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
            //ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
*/
       /* if(permissions.size() > 0){

        }*/
    }

    private boolean checkPermissions(){
        int result;
        List<String>permissionList= new ArrayList<>();
        for(String pm : permissions){
            result = ContextCompat.checkSelfPermission(this, pm);
            if(result!=PackageManager.PERMISSION_GRANTED){
                permissionList.add(pm);
            }
        }
        if(!permissionList.isEmpty()){
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 ){
                    for(int i =0; i < permissions.length; i++){
                        if(permissions[i].equals(this.permissions[i])){
                            if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                                Toast.makeText(this, "권한 요청에 동의하셔야 제대로 서비스를 이용하실 수 있습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }
                }else{
                    Toast.makeText(this, "권한 요청에 동의하셔야 제대로 서비스를 이용하실 수 있습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(serviceIntent!=null){
            stopService(serviceIntent);
            serviceIntent=null;
        }
    }


    public class DB_login extends AsyncTask<Void, Integer, String>{
        String data="";
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(Void... unused) {
            String param = "id=" + str_id + "&pw=" + str_pw + "";


            try{
                URL url = new URL("http://211.216.137.157/apkCtrl/user_auth_apk.php");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                //안드로이드 -> 서버로 파라미터 값 전달
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                //서버 -> 안드로이드 파라미터 값 전달
                InputStream is = null;
                BufferedReader in= null;
                //String data="";

                is=conn.getInputStream();
                in=new BufferedReader(new InputStreamReader(is), 8*1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while((line = in.readLine())!= null){
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();

                //서버로부터 응답
                Log.e("RECV DATA", data);


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
            progressDialog = ProgressDialog.show(MainActivity.this, "잠시만 기다려주세요", null, true, true);
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            progressDialog.dismiss();
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);

            if(data.equals("1")){
                Log.e("RESULT", "성공적으로 처리됨");
                alertBuilder.setTitle("알림").setMessage("로그인 성공").setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(MainActivity.this, MainMenu.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }else if(data.equals("0")){
                Log.e("RESULT", "비밀번호가 틀렸습니다.");
                alertBuilder.setTitle("알림").setMessage("비밀번호가 틀렸습니다.").setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //finish();
                            }
                        });

                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }else{
                Log.e("RESULT", "errcode: " + data);
                alertBuilder.setTitle("알림").setMessage("errcode: "+ data).setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
        }
    }

}

