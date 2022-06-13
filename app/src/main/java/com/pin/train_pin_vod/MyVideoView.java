package com.pin.train_pin_vod;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;


public class MyVideoView extends VideoView {

    boolean is_full = true;


    public MyVideoView(Context context) {
        super(context);
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(is_full) {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void set_full(boolean full){
        is_full = full;
    }


    public  void lre(){

    }
}
