package kr.ac.kpu.ce2015150012.ub_app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImagePop extends Activity {

    ImageView img_view;
    getImage getImage;
    public static final String imgURL="http://theopentutorials.com/totwp331/wp-content/uploads/totlogo.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_pop);

        img_view = (ImageView)findViewById(R.id.imageView);

        getImage = new getImage();
        getImage.execute(imgURL);
    }

    private class getImage extends AsyncTask<String, Void, Bitmap> {
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
            img_view.setImageBitmap(img);
        }
    }
}
