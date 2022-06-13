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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VODPlayerActivity extends AppCompatActivity {

    MediaController controller;

    boolean movie_prepared= false;

    MyVideoView movie_videoview;

    RelativeLayout movieVideoLayout;

    Context con;

    Locale systemLocale;

    String local_str;

    TextView time_text;

    String TAG;

    Boolean fast_play = true;

    MediaController mcontroller;

    ImageView lo;

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
        setContentView(R.layout.activity_play);

        Intent aa = getIntent();

        int a = aa.getIntExtra("index", 0) + 1;

        video_url =   "rtsp://192.168.0.101:1935/vod/" + String.format("%03d" , a) + ".mp4";

        con = this;
        init_view();
        Intent intent = getIntent();
        init_video( intent);
        systemLocale = getApplicationContext().getResources().getConfiguration().locale;

        local_str = systemLocale.getLanguage();

        mtime_Handler.sendMessage( mtime_Handler.obtainMessage(0));

        //화면 터치시 바 내려옴
        movie_videoview.setOnTouchListener(new View.OnTouchListener() {
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

    public String video_url="rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";


    public void init_view(){

        movie_videoview = findViewById(R.id.movie_videoview);

        movieVideoLayout = findViewById(R.id.movieVideoLayout);
        time_text = findViewById(R.id.text_time);

        lo = findViewById(R.id.img_lo);
    }


    void init_video(Intent intent){

        movie_prepared= false;

//        controller = new MediaController(con);
////        controller.setVisibility(View.GONE);
//        movie_videoview.setMediaController(controller);
        movie_videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub

                System.out.println("JJJ setOnCompletionListener: ");
//                Toast.makeText(getApplicationContext(), "END", Toast.LENGTH_SHORT).show();

//
//                String video_url="rtsp://192.168.8.180:8554/edit";
//                Uri uri = Uri.parse(video_url);
//                movie_videoview.setVideoURI(uri);

                finish();

            }
        });


        movie_videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer mp) {

                System.out.println("JJJ setOnPreparedListener: ");
                movie_prepared = true;

                movie_videoview.start();
                movieVideoLayout.setVisibility(View.INVISIBLE);

                slideUp(movieVideoLayout);
//                if (fast_play) {
//                    fast_play = false;
//                    findViewById(R.id.loading_Layout).setVisibility(View.VISIBLE);
//                    ImageView iv = (ImageView) findViewById(R.id.img_loading);
//
//
//                    AnimationDrawable  drawable = (AnimationDrawable)iv.getBackground();
//
//                    checkIfAnimationDone(drawable);
//                    drawable.start();
//
//                }
            }
        });

        movie_videoview.setOnInfoListener(new MediaPlayer.OnInfoListener() {

                                              @Override
                                              public boolean onInfo(MediaPlayer mp, int what, int extra) {

                                                  switch(what){
                                                      case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                                                          System.out.println("JJJ MEDIA_INFO_BUFFERING_START: ");
                                                          // Progress Diaglog 출력
//                                                          Toast.makeText(getApplicationContext(), "Buffering", Toast.LENGTH_LONG).show();
                                                          break;

                                                      case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                                                          System.out.println("JJJ MEDIA_INFO_BUFFERING_END: ");
                                                          // Progress Dialog 삭제
//                                                          Toast.makeText(getApplicationContext(), "Buffering finished.\nResume playing", Toast.LENGTH_LONG).show();
//                                                          movie_videoview.start();
                                                          break;

                                                  }
                                                  return false;
                                              }
                                          }
        );



//        String video_url="https://www.radiantmediaplayer.com/media/bbb-360p.mp4";

//        String video_url="https://58c8b0ad51ce1.streamlock.net:443/ETSU/edgeradio/playlist.m3u8";

//        String video_url="https://5b44cf20b0388.streamlock.net:8443/live/ngrp:live_all/playlist.m3u8";

//        String video_url="rtsp://192.168.0.85:8554/edit";
//        String video_url="rtsp://192.168.0.2:8554/edit";
//        String video_url="rtsp://192.168.0.4:8554/edit";
//        String video_url="rtsp://192.168.8.180:8554/edit";
//        String video_url="rtsp://192.168.8.181:8554/edit";
//        String video_url="rtsp://192.168.0.24:8554/edit";

//        String video_url="rtp://@239.129.0.1:5004";

        Uri uri = Uri.parse(video_url);

        movie_videoview.set_full(false);
        movie_videoview.setVideoURI(uri);
        movie_videoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {

                try {
//                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        mediaPlayer.reset();
//                        String video_url="rtsp://192.168.0.2:8554/edit";

                        Uri uri = Uri.parse(video_url);
                        movie_videoview.set_full(false);
                        movie_videoview.setVideoURI(uri);

//                    downThread mdownThread = new downThread();
//                    mdownThread.start();
//                    }
//                    mediaPlayer.release();
                    System.out.println("JJJ aa: " + i + ", " + i1);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("JJJ EEE: " +e );
                }

//                Toast.makeText(getApplicationContext(), "restart", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        mcontroller = new MediaController(this);

//        downThread mdownThread = new downThread();
//        mdownThread.start();

//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        ((WindowManager)con.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
//        int deviceWidth = displayMetrics.widthPixels;
//        int deviceHeight = displayMetrics.heightPixels;

//        binding.movieVideoview.setLayoutParams(deviceWidth, deviceHeight);

//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
//        int deviceWidth = displayMetrics.widthPixels;
//        int deviceHeight = displayMetrics.heightPixels;
//
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(1280, 720);
//
//        binding.movieVideoview.setLayoutParams(deviceWidth, deviceHeight);

//        MediaPlayer.OnPreparedListener PreParedListener = new MediaPlayer.OnPreparedListener()
//        {
//            public void onPrepared(MediaPlayer player)
//            {
//                player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener()
//                {
//                    @Override
//                    public void onVideoSizeChanged(MediaPlayer player, int width, int height)
//                    {
//                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//
//                                ViewGroup.LayoutParams.MATCH_PARENT);
//                        binding.movieVideoview.setLayoutParams(lp);
//                    }
//
//                });
//
//            }
//        } ;
//
//        binding.movieVideoview.setOnPreparedListener(PreParedListener);

//        binding.movieVideoview.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                if(event.getAction() == MotionEvent.ACTION_UP){
//                    binding.movieVideoLayout.setVisibility(View.VISIBLE);
//                    mbtn_view_Handler.removeMessages(0);
//                    mbtn_view_Handler.sendMessageDelayed(mbtn_view_Handler.obtainMessage(0), 4000);
//                }
//                return true;
//            }
//        });
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
        if (movie_videoview != null) {
            movie_videoview.stopPlayback();
            movie_videoview.suspend();
            movie_videoview = null;
        }

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

}
