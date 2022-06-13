package com.pin.train_pin_vod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Context con;

    TextView time_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        con = this;

        time_text = findViewById(R.id.text_time);

        mtime_Handler.sendMessage( mtime_Handler.obtainMessage(0));

    }


    public void btnClick(View view) {
        switch (view.getId()) {
            case  R.id.button:{
                Intent intent = new Intent(con, MoviActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.button2:{
                Intent intent = new Intent(con, PlayerActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btn_home:{
                finish();
            }
            break;
        }

    }




    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


    private Handler mtime_Handler = new Handler(){
        public void handleMessage(Message msg) {

            try {
                Date date = new Date(System.currentTimeMillis());

                String formattedDate = new SimpleDateFormat("MM월 dd일 HH:mm", Locale.KOREA).format(date);
                time_text.setText(formattedDate);


//                if("ko".equals(local_str)){
//                    String formattedDate = new SimpleDateFormat("MM월 dd일 HH:mm", Locale.KOREA).format(date);
//                    time_text.setText(formattedDate);
//                }else{
//                    String formattedDate = new SimpleDateFormat("MMM dd HH:mm", Locale.ENGLISH).format(date);
//                    time_text.setText(formattedDate);
//                }

            }catch (Exception e){

            }
            mtime_Handler.sendMessageDelayed(mtime_Handler.obtainMessage(0), 500);

        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0){
            if (resultCode==RESULT_OK) {
                Toast.makeText(MainActivity.this, "result ok!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, "result cancle!", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode==1){
        }
    }



}
