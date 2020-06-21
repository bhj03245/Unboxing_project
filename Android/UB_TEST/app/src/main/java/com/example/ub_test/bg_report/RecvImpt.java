package com.example.ub_test.bg_report;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;
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

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class RecvImpt extends AsyncTask<String, Void, String> {
    private SharedPreferences report_data;
    private SharedPreferences switch_data;
    boolean bSwitch;
    String strSwith;
    Context mContext;
    String data = "";

    final static String trig = "trigger";
    HttpURLConnection httpURLConnection;

    public RecvImpt(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(String... unused) {

        switch_data = mContext.getSharedPreferences("switch_data", MODE_PRIVATE);
        bSwitch = switch_data.getBoolean("switchkey", false);
        strSwith = String.valueOf(bSwitch);
        Log.d(TAG, "mode!: " + bSwitch);


        String param = "request_impt" + "=" + strSwith;
        String ip = mContext.getString(R.string.ip);
        try {
            URL url = new URL(ip + "/apkCtrl/reportImpt_apk.php");
            httpURLConnection = (HttpURLConnection) url.openConnection();
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

        report_data = mContext.getSharedPreferences("report_data", MODE_PRIVATE);
        String phone = report_data.getString("phone", "");
        String content = report_data.getString("content", "");


        if (s.equals("Impt")) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone, null, content, null, null);
                Toast.makeText(mContext, "Send Succeeded", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "recvData: " + s);
        }

    }
}
