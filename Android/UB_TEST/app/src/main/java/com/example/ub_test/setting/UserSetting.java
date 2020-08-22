package com.example.ub_test.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.ub_test.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UserSetting extends Activity {

    EditText et_oldPw;
    EditText et_newPw;
    EditText et_reNewPw;
    Button btn_insert_user;

    String str_oldPw;
    String str_newPw;
    String str_reNewPw;
    String id;


    private static String TAG = "test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_setting);

        Intent intent = getIntent();
        id = intent.getStringExtra("key");
        Toast.makeText(UserSetting.this, id,
                Toast.LENGTH_SHORT).show();

        et_oldPw = (EditText)findViewById(R.id.et_oldPw);
        et_newPw = (EditText)findViewById(R.id.et_newPw);
        et_reNewPw = (EditText)findViewById(R.id.et_reNewPw);
        btn_insert_user = (Button)findViewById(R.id.insertBtn_user);

        btn_insert_user.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try{
                    str_oldPw = et_oldPw.getText().toString();
                    str_newPw = et_newPw.getText().toString();
                    str_reNewPw = et_reNewPw.getText().toString();

                    if(str_oldPw.getBytes().length <= 0 | str_newPw.getBytes().length <= 0
                            | str_reNewPw.getBytes().length <= 0){
                        Toast.makeText(UserSetting.this, "공란 없이 입력해 주세요.",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }else if(!str_newPw.equals(str_reNewPw)){
                        Toast.makeText(UserSetting.this, "새로운 비밀번호와 비밀번호 확인이 다릅니다.",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }else if(str_oldPw.equals(str_newPw)){
                        Toast.makeText(UserSetting.this, "현재 비밀번호와 새로운 비밀번호와 같습니다.",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                }catch(NullPointerException e){
                    Log.e("error", e.getMessage());
                }
                ChangePw cp = new ChangePw();
                cp.execute();
            }
        });
    }


        private class ChangePw extends AsyncTask<String, Void, String> {
        String data = "";
        ProgressDialog progressDialog;

        HttpURLConnection httpURLConnection;


        @Override
        protected String doInBackground(String... params) {
            String param = "id=" + id + "&oldPw=" + str_oldPw + "&newPw=" + str_newPw + "";



            try{
                URL url = new URL(getString(R.string.ip) + "/apkCtrl/userCtrl_apk.php");
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outs = httpURLConnection.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                int responseStatusCode = httpURLConnection.getResponseCode();


                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine())!= null){
                    sb.append(line + "\n");
                }
                bufferedReader.close();

                data = sb.toString().trim();

                Log.e(TAG, data);

                return data;



            }catch(MalformedURLException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                httpURLConnection.disconnect();
            }

            return data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(UserSetting.this, "잠시만 기다려주세요", null, true, true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(UserSetting.this);

            if(s.equals("1")){
                alertBuilder.setTitle("알림").setMessage("비밀번호 변경 성공").setCancelable(true);
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }else if(s.equals("0")){
                alertBuilder.setTitle("알림").setMessage("비밀번호 변경 실패").setCancelable(true);
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }else{
                alertBuilder.setTitle("알림").setMessage("errcode: " + s).setCancelable(true);
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
        }
    }

}
