package kr.ac.kpu.ce2015150012.ub_app.setting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

import kr.ac.kpu.ce2015150012.ub_app.R;

import static android.content.ContentValues.TAG;

public class ReportSetting extends Activity {

    EditText et_phone;
    EditText et_contents;
    Button btn_insert;

    String str_phone;
    String str_contents;

    private static String IP_ADDRESS = "http://211.216.137.157/apkCtrl/reportSet_apk.php";

    private SharedPreferences report_data;
    private String load_phone;
    private String load_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_report_setting);

        et_phone = (EditText)findViewById(R.id.et_phoneNum);
        et_contents = (EditText)findViewById(R.id.et_contents);
        btn_insert = (Button)findViewById(R.id.insertBtn);

        report_data = getSharedPreferences("report_data", MODE_PRIVATE);
        load();

        et_phone.setText(load_phone);
        et_contents.setText(load_content);

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_phone = et_phone.getText().toString();
                str_contents = et_contents.getText().toString();
                save();

                SendData SD = new SendData();
                SD.execute(IP_ADDRESS, "phone", str_phone, "content", str_contents);

            }
        });
    }
    private void save(){
        SharedPreferences.Editor editor = report_data.edit();

        editor.putString("phone", str_phone.trim());
        editor.putString("content", str_contents.trim());

        editor.apply();
    }

    private void load(){
        load_phone = report_data.getString("phone", "");
        load_content = report_data.getString("content", "");
    }




    private class SendData extends AsyncTask<String, Void, String> {
        String data = "";
        ProgressDialog progressDialog;

        HttpURLConnection httpURLConnection;

        @Override
        protected String doInBackground(String... params) {
            //String param = "phone=" + str_phone + "content=" + str_contents + "";

            String serverURL = (String) params[0];

            String key1 = (String) params[1];
            String value1 = (String) params[2];

            String key2 = (String) params[3];
            String value2 = (String) params[4];

            String postParameters = key1 +"=" + value1 + "&" + key2 + "=" + value2;

            Log.e(TAG, postParameters);

            try{
                URL url = new URL(serverURL);
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outs = httpURLConnection.getOutputStream();
                outs.write(postParameters.getBytes("UTF-8"));
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
                    sb.append(line);
                }
                bufferedReader.close();

                data = sb.toString();

                return data;

                //Log.e(TAG, data);

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
            progressDialog = ProgressDialog.show(ReportSetting.this, "잠시만 기다려주세요", null, true, true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            if(s.equals("Insert")){
                Toast.makeText(getApplicationContext(), "데이터 저장 완료", Toast.LENGTH_LONG).show();
            }else if(s.equals("Update")){
                Toast.makeText(getApplicationContext(), "데이터 업데이트 완료", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "errcode: " + s, Toast.LENGTH_LONG).show();
            }

        }
    }
}
