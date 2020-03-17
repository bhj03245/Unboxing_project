package com.example.urlimage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    //Bitmap bitmap;
    back task;
    public static final String imgURL = "http://theopentutorials.com/totwp331/wp-content/uploads/totlogo.png";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       /* Display dp = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int width = (int)(dp.getWidth()*0.7);
        int height = (int)(dp.getHeight()*0.8);

        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;*/


        imageView = (ImageView) findViewById(R.id.imageView);
       /* try{
            URL url = new URL(URL);
            URLConnection conn = url.openConnection();
            conn.connect();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
            imageView.setImageBitmap(bm);
        }catch(MalformedURLException e1){
            e1.printStackTrace();
        }catch(IOException e2){
            e2.printStackTrace();
        }*/
      /*  Thread mThread = new Thread(){
            HttpURLConnection conn = null;
            BufferedInputStream bis = null;
            @Override
            public void run() {
                try{
                    URL url = new URL(imgURL);

                    conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    int nSize = conn.getContentLength();
                    bis = new BufferedInputStream(conn.getInputStream(), nSize);
                    //InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(bis);
                    //is.close();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(bis!=null){
                        try{bis.close();} catch(IOException e){e.printStackTrace();};
                    }
                    if(conn!=null){conn.disconnect();}
                }
            }
        };
        mThread.start();

        try{
            mThread.join();
            imageView.setImageBitmap(bitmap);
        }catch (InterruptedException e){
            e.printStackTrace();
        }*/


        task = new back();

        task.execute(imgURL);
    }

    private class back extends AsyncTask<String, Void, Bitmap> {
        HttpURLConnection conn = null;
        BufferedInputStream bis = null;
        Bitmap bitmap;
        @Override
        protected Bitmap doInBackground(String... urls) {
            // TODO Auto-generated method stub
            try {
                URL myFileUrl = new URL(urls[0]);
                conn = (HttpURLConnection) myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();

                //InputStream is = conn.getInputStream();
                int nSize = conn.getContentLength();
                bis = new BufferedInputStream(conn.getInputStream(), nSize);
                bitmap = BitmapFactory.decodeStream(bis);


            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                if(bis!=null){
                    try{bis.close();} catch(IOException e){e.printStackTrace();}
                }
                if(conn!=null){conn.disconnect();}
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap img) {

                super.onPostExecute(img);
                imageView.setImageBitmap(img);


        }
    }

}
