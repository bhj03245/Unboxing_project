package kr.ac.kpu.ce2015150012.ub_app;

import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

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
