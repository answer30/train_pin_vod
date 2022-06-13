package com.pin.train_pin_vod;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VODPlayerActivity2 extends AppCompatActivity {

    MediaController controller;

    boolean movie_prepared= false;

//    MyVideoView movie_videoview;

    RelativeLayout movieVideoLayout;

    Context con;

    Locale systemLocale;

    String local_str;

    TextView time_text;

    String TAG;

    Boolean fast_play = true;

    MediaController mcontroller;

    private PlayerView exoPlayerView;
    private SimpleExoPlayer player;

    ImageView lo;

    private Boolean playWhenReady = true;
    private int currentWindow = 0;
    private Long playbackPosition = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = this.toString();

        View decoView = getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decoView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_play2);

        Intent aa = getIntent();

        int a = aa.getIntExtra("index", 0) + 1;

        video_url =   "rtsp://192.168.0.101:1935/vod/" + String.format("%03d" , a) + ".mp4";

        con = this;
        init_view();
        Intent intent = getIntent();

        initializePlayer();

        systemLocale = getApplicationContext().getResources().getConfiguration().locale;

        local_str = systemLocale.getLanguage();

        mtime_Handler.sendMessage( mtime_Handler.obtainMessage(0));

        //화면 터치시 바 내려옴
        exoPlayerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP){

                    if(movieVideoLayout.getVisibility() != View.VISIBLE){
//                        movieVideoLayout.setVisibility(View.VISIBLE);
                        slideDown(movieVideoLayout);
                    }
                    mbtn_view_Handler.removeMessages(0);
                    mbtn_view_Handler.sendMessageDelayed(mbtn_view_Handler.obtainMessage(0), 4000); //4초
                }
                return true;
            }
        });


        if (fast_play) {
            fast_play = false;
            findViewById(R.id.loading_Layout).setVisibility(View.VISIBLE);
            ImageView iv = (ImageView) findViewById(R.id.img_loading);


            AnimationDrawable  drawable = (AnimationDrawable)iv.getBackground();

            checkIfAnimationDone(drawable);
            drawable.start();

        }
    }

    public String video_url="http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";


    public void init_view(){

//        movie_videoview = findViewById(R.id.movie_videoview);

        movieVideoLayout = findViewById(R.id.movieVideoLayout);
        time_text = findViewById(R.id.text_time);

        lo = findViewById(R.id.img_lo);
    }

    private void initializePlayer() {

        exoPlayerView = findViewById(R.id.exoPlayerView);

        if (player == null) {

            player =  new SimpleExoPlayer.Builder(con).build();

            //플레이어 연결
            exoPlayerView.setPlayer(player);

        }

//        String sample = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";

//        String sample = "https://1radio.gscdn.kbs.co.kr/1radio_192_1.m3u8?Expires=1630461232&Policy=eyJTdGF0ZW1lbnQiOlt7IlJlc291cmNlIjoiaHR0cHM6Ly8xcmFkaW8uZ3NjZG4ua2JzLmNvLmtyLzFyYWRpb18xOTJfMS5tM3U4IiwiQ29uZGl0aW9uIjp7IkRhdGVMZXNzVGhhbiI6eyJBV1M6RXBvY2hUaW1lIjoxNjMwNDYxMjMyfX19XX0_&Signature=UdOEbM2-pNjo44KkbcOC4LyOIUw2ROSMLhitw~Ex1opajcthj4bSFnoqjDuU9arIKGdk2arcidaBV-TkWGtSB6riIDx9z36rUHQlpX3oE9svv11S6PbcHAJXZWFIH3qQF1N~jk52OGsjdyWzKNdwQ4iyFPc2PW-NCm3RHN5rmJsgOCtL0I1J-mRKF6kph-R1iu6eGZKAnlXwoC3DaeEnYkI6gPNqmv0Jn7nnjFo5azz-44Bj9mQP7blYyEWjOtJibjngre9FbEVF-CMXRdlsLKGHO1Dake5hejDKYJ5HU4CuIyTZIMX2jxOBduMJrIZLNdcyDDIKtu75SWcrX29kuQ__&Key-Pair-Id=APKAICDSGT3Y7IXGJ3TA";

//        String sample = "https://radiolive.sbs.co.kr/love/lovefm.stream/playlist.m3u8?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MzAzMzM1NjQsInBhdGgiOiIvbG92ZWZtLnN0cmVhbSIsImR1cmF0aW9uIjotMSwidW5vIjoiMGMwM2JlNmUtMmNkZC00YjI0LTg1YTYtODkyZGRiZjcxZDM2IiwiaWF0IjoxNjMwMjkwMzY0fQ.GZBO3BRPpWS8L1VRBIWPB6Htdgc7yNSwpTCz3bikQRs";


//        String sample = "https://radiolive.sbs.co.kr/power/powerfm.stream/playlist.m3u8?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MzAzNTAzMjQsInBhdGgiOiIvcG93ZXJmbS5zdHJlYW0iLCJkdXJhdGlvbiI6LTEsInVubyI6IjRlNWMwZTVlLTk1ODUtNDgxYS1hZDc3LWZjZjlkY2IwMzFiNSIsImlhdCI6MTYzMDMwNzEyNH0.rEb3d1I-j6r8pRXJ430ivZRu1zJJWE0d5V8DewYmscs";
//        String sample = "rtsp://192.168.0.101:8554/edit";
        String sample = "https://radiolive.sbs.co.kr/power/powerfm.stream/playlist.m3u8?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MzA1MDE2OTksInBhdGgiOiIvcG93ZXJmbS5zdHJlYW0iLCJkdXJhdGlvbiI6LTEsInVubyI6IjAwODg5N2RlLTNhY2ItNGQ0Ny04NDNhLWJhNTVkYTJhMDgxYiIsImlhdCI6MTYzMDQ1ODQ5OX0.aXTshkte7GuPRP-gn3VNFiv4UiqLcUSpUCZgyUo7r2o";
//        String sample = "http://serpent0.duckdns.org:8088/sbs2fm.pls";

//        MediaSource mediaSource = buildMediaSource(Uri.parse("http://tbs.hscdn.com/tbsradio/fm/playlist.m3u8"));
        MediaSource mediaSource = buildMediaSource(Uri.parse(video_url));


//        player.setMediaItem(MediaItem.fromUri(Uri.parse(sample)));

//        https://1radio.gscdn.kbs.co.kr/1radio_192_1.m3u8?Expires=1630461232&Policy=eyJTdGF0ZW1lbnQiOlt7IlJlc291cmNlIjoiaHR0cHM6Ly8xcmFkaW8uZ3NjZG4ua2JzLmNvLmtyLzFyYWRpb18xOTJfMS5tM3U4IiwiQ29uZGl0aW9uIjp7IkRhdGVMZXNzVGhhbiI6eyJBV1M6RXBvY2hUaW1lIjoxNjMwNDYxMjMyfX19XX0_&Signature=UdOEbM2-pNjo44KkbcOC4LyOIUw2ROSMLhitw~Ex1opajcthj4bSFnoqjDuU9arIKGdk2arcidaBV-TkWGtSB6riIDx9z36rUHQlpX3oE9svv11S6PbcHAJXZWFIH3qQF1N~jk52OGsjdyWzKNdwQ4iyFPc2PW-NCm3RHN5rmJsgOCtL0I1J-mRKF6kph-R1iu6eGZKAnlXwoC3DaeEnYkI6gPNqmv0Jn7nnjFo5azz-44Bj9mQP7blYyEWjOtJibjngre9FbEVF-CMXRdlsLKGHO1Dake5hejDKYJ5HU4CuIyTZIMX2jxOBduMJrIZLNdcyDDIKtu75SWcrX29kuQ__&Key-Pair-Id=APKAICDSGT3Y7IXGJ3TA
        //  https://1radio.gscdn.kbs.co.kr/1radio_192_1.m3u8?Expires=1630462805&Policy=eyJTdGF0ZW1lbnQiOlt7IlJlc291cmNlIjoiaHR0cHM6Ly8xcmFkaW8uZ3NjZG4ua2JzLmNvLmtyLzFyYWRpb18xOTJfMS5tM3U4IiwiQ29uZGl0aW9uIjp7IkRhdGVMZXNzVGhhbiI6eyJBV1M6RXBvY2hUaW1lIjoxNjMwNDYyODA1fX19XX0_&Signature=Lq0MNKm626KhhoUOSDStY4zkolxx-2vtVOR6mo7o~FwAb3pIxRoQ6egVSZsflgOlpGnG4Yt~wc80ADRcFMFN7Tz7Q4D~vaxcGe83niNz-QcEtMzJvM0PAQtCuADvBskG9vBd46Oq-0wbD4FYgJjYuIdReRL-kgfGSdexKy3pWXevlHyydqR-bnhd735iSZtgbocL4mcrqeJn9WVDtriUD7~3dgH-aj7YgTdT2ChqeghUYjSOm2PXHrlprL5Z0eQJULuoyeA0bOx3HkaLc8l2C5GCYTkM2wwF3kkwgka11sS-sOUYz2909vrk5fEiBjohRi9ho42b-YWNqZnIEyDcGQ__&Key-Pair-Id=APKAICDSGT3Y7IXGJ3TA

        player.setMediaSource(mediaSource);
        player.prepare();
        player.play();

        //prepare
//        player.prepare(mediaSource, true, false);

        //start,stop
//        player.setPlayWhenReady(playWhenReady);


    }


//    class downThread extends Thread{
//        @Override
//        public void run() {
//            String video_url="rtsp://192.168.0.2:8554/edit";
//            Uri uri = Uri.parse(video_url);
//            movie_videoview.setVideoURI(uri);
//        }
//
//        @Override
//        public void interrupt() {
//            super.interrupt();
//        }
//    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        System.out.println("JJJ onWindowFocusChanged: ");

        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

//            View.SYSTEM_UI_FLAG_IMMERSIVE
//                    // Set the content to appear under the system bars so that the
//                    // content doesn't resize when the system bars hide and show.
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    // Hide the nav bar and status bar
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN);

        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        app_end();
    }


    public void btnClick(View view) {

        switch (view.getId()){
            case R.id.btn_home:{
//                finish();
                app_end();
            }

            break;
//            case R.id.btn_back:{
//            }
//            break;
        }
    }

    private Handler mbtn_view_Handler = new Handler(){
        public void handleMessage(Message msg) {

            slideUp(movieVideoLayout);

            movieVideoLayout.setVisibility(View.INVISIBLE);
        }
    };


    private Handler mtime_Handler = new Handler(){
        public void handleMessage(Message msg) {

            try {
//                if (tv != null) {
////                    info_txt.setText("");
//                    Toast.makeText(con,"event : " , Toast.LENGTH_SHORT).show();
//                }

//                val date = Date(System.currentTimeMillis())
//                val formattedDate = SimpleDateFormat("yyyy.MM.dd", Locale.ENGLISH).format(date)
//                val formattedWeek = SimpleDateFormat("E", Locale.ENGLISH).format(date)
//                val formattedTime = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(date)
//
//                text_date_01.text = formattedDate
//                text_date_02.text = "("+formattedWeek+")"
//                text_date_03.text = formattedTime

                Date date = new Date(System.currentTimeMillis());
//                String formattedDate = new SimpleDateFormat("MM.dd", Locale.KOREA).format(date);
//                String formattedWeek = new SimpleDateFormat("E", Locale.KOREA).format(date);
//                String formattedTime = new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(date);


                if("ko".equals(local_str)){
                    String formattedDate = new SimpleDateFormat("MM월 dd일 HH:mm", Locale.KOREA).format(date);
                    time_text.setText(formattedDate);
                }else{
                    String formattedDate = new SimpleDateFormat("MMM dd HH:mm", Locale.ENGLISH).format(date);
                    time_text.setText(formattedDate);
                }


                if(lo.getVisibility() == View.VISIBLE){
                    lo.setVisibility(View.INVISIBLE);
                }else{
                    lo.setVisibility(View.VISIBLE);
                }


            }catch (Exception e){

            }
            mtime_Handler.sendMessageDelayed(mtime_Handler.obtainMessage(0), 1000);

        }
    };

    public void slideUp(View view){

        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                -view.getHeight());                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.INVISIBLE);
    }

    public void slideDown(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                -view.getHeight(),                 // fromYDelta
                0); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);

    }


    public void app_end(){
//        movie_videoview.stopPlayback();

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    private void checkIfAnimationDone(AnimationDrawable anim){
        final AnimationDrawable a = anim;
        int timeBetweenChecks = 300;
        Handler h = new Handler();
        h.postDelayed(new Runnable(){
            public void run(){
                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)){
                    checkIfAnimationDone(a);
                } else{
//                    Toast.makeText(getApplicationContext(), "ANIMATION DONE!", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.loading_Layout).setVisibility(View.INVISIBLE);
                }
            }
        }, timeBetweenChecks);
    }


    private MediaSource buildMediaSource(Uri uri) {

//        String userAgent = Util.getUserAgent(this, "blackJin");
//        val url = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"

        MediaItem mediaItem = MediaItem.fromUri(uri);

//        return new ProgressiveMediaSource.Factory(new DefaultHttpDataSource.Factory()).createMediaSource(mediaItem);

//        return new HlsMediaSource.Factory(new DefaultHttpDataSource.Factory()).createMediaSource(mediaItem);
        return new RtspMediaSource.Factory().createMediaSource(MediaItem.fromUri(uri));


    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();

            exoPlayerView.setPlayer(null);
            player.release();
            player = null;

        }
    }
}
