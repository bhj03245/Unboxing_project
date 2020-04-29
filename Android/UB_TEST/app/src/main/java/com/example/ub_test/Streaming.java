package com.example.ub_test;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class Streaming extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);

        String uri = "rtsp://125.142.56.46:8554/unicast";
        VideoView v = (VideoView) findViewById( R.id.videoView );
        v.setVideoURI( Uri.parse(uri) );
        //v.setMediaController( new MediaController( this ) );
        v.requestFocus();
        v.start();
    }
}
