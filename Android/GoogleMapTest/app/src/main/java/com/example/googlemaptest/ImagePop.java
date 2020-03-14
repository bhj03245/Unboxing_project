package com.example.googlemaptest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImagePop extends Activity {

    ImageView imageView;
    back task;
    public static final String imgURL = "http://theopentutorials.com/totwp331/wp-content/uploads/totlogo.png";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_pop);

        imageView = (ImageView)findViewById(R.id.imageView);

        task = new back();
        task.execute(imgURL);


    }




        // 제목창 없애기

        //WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        //layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        //layoutParams.dimAmount = 0.7f;
        //getWindow().setAttributes(layoutParams);
        // 뒷 배경 블러효과


       /* Display dp = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int width = (int)(dp.getWidth()*0.7);
        int height = (int)(dp.getHeight()*0.8);

        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;*/




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

                int nSize = conn.getContentLength();
                bis = new BufferedInputStream(conn.getInputStream(), nSize);
                bitmap = BitmapFactory.decodeStream(bis);



            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                if(bis!=null){
                    try{bis.close();} catch (IOException e){e.printStackTrace();}
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
