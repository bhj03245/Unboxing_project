package kr.ac.kpu.ce2015150012.ub_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText et_id;
    EditText et_pw;
    Button btn_login;
    String str_id;
    String str_pw;
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
                    str_pw = et_id.getText().toString();
                }catch(NullPointerException e){
                    Log.e("error", e.getMessage());
                }
                DB_login DBL = new DB_login();
                DBL.execute();
            }
        });
    }


    public class DB_login extends AsyncTask<Void, Integer, Void>{
        String data="";
        @Override
        protected Void doInBackground(Void... unused) {
            String param = "users_id=" + str_id + "&users_pwd" + "";
            Log.e("POST", param);

            try{
                URL url = new URL("http://localhost/apkCtrl/user_auth_apk.php");
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

                if(data.equals("0")){
                    Log.e("RESULT", "성공");
                }else{
                    Log.e("RESULT", "error_code="+data);
                }

            }catch(MalformedURLException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(data.equals("1")){
                Log.e("RESULT", "성공적으로 처리됨");
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
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
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                alertBuilder.setTitle("알림").setMessage("비밀번호가 틀렸습니다.").setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }else{
                Log.e("RESULT", "errcode: " + data);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
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

