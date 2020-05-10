package com.example.ub_test.parking;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;


import com.example.ub_test.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class ImagePop extends Activity {

    ImageView img_view;
    getImage getImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_pop);

        img_view = (ImageView)findViewById(R.id.imageView);

        getImage = new getImage();
        getImage.execute();
    }

    private class getImage extends AsyncTask<Void, Void, Bitmap> {
        HttpURLConnection conn = null;
        BufferedInputStream bis = null;
        Bitmap bitmap;
        @Override
        protected Bitmap doInBackground(Void... unused) {
            // TODO Auto-generated method stub
            try {
                URL myFileUrl = new URL(getString(R.string.ip) + "/Upload/Parkimg/capture_0.jpg");
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
            img_view.setImageBitmap(img);
        }
    }
}
