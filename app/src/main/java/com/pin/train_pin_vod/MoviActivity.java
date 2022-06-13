package com.pin.train_pin_vod;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.pin.train_pin_vod.Listener.OnSnapPositionChangeListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MoviActivity extends AppCompatActivity {


    int [] movie_img_list = {R.drawable.img_001, R.drawable.img_002, R.drawable.img_003, R.drawable.img_004, R.drawable.img_005, R.drawable.img_006,  R.drawable.img_007,  R.drawable.img_008,  R.drawable.img_009,  R.drawable.img_010};

    int [] movie_img_conlist = {R.drawable.img_001_con, R.drawable.img_002_con, R.drawable.img_003_con, R.drawable.img_004_con, R.drawable.img_005_con, R.drawable.img_006_con,  R.drawable.img_007_con,  R.drawable.img_008_con,  R.drawable.img_009_con,  R.drawable.img_010_con};

    String[] movie_title_list;

    Context con;

    DaoFile dao;

    ArrayList<String[]> local_movie_list;

    private MovieAdapter mmovieAdapter;

    int movie_index = 1;

    HorizontalCarouselRecyclerView movieListView;

    ConstraintLayout constraints;

    TextView textView;

    TextView time_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vod_list);

        con = this;

        dao = new DaoFile(con);


        if(local_movie_list == null) {
            local_movie_list = dao.getVideoAllList();
//            local_movie_list = dao.getUsbMediaMovieList();

            movie_title_list = getResources().getStringArray(R.array.movi_title);

//            movie_text_list = getResources().getStringArray(R.array.movi_text);
        }


        movieListView = findViewById(R.id.movie_list_view);

        constraints = findViewById(R.id.constraints);
        constraints.setBackgroundResource(movie_img_conlist[0]);

        textView = findViewById(R.id.textView);

//        textView.setText(movie_text_list[0]);


        if (mmovieAdapter == null){

            mmovieAdapter = new MovieAdapter();

//            movieListView.setAdapter(mmovieAdapter);

            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(movieListView);

            OnSnapPositionChangeListener aa = new OnSnapPositionChangeListener() {
                @Override
                public void onSnapPositionChange(final int position) {

                    Log.d("JJJ", "onSnapPositionChange: " +position );

                    mmovieAdapter.setSelectedPosition(position);

                    Handler mHandler = new Handler();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
//                            textView.setText(movie_text_list[position]);
                            constraints.setBackgroundResource(movie_img_conlist[position]);

//                            Toast.makeText(con, "onSnapPositionChange" , Toast.LENGTH_SHORT);
                        }
                    });
                }
            };

            movieListView.initialize(mmovieAdapter, snapHelper, aa);

//            movieListView.addOnScrollListener( enw );

            //------------------------------------------------------------------------------------------
//            movieListView.addOnScrollListener(new RecyclerView.OnScrollListener(){
//                @Override
//                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                    if(newState == 0){  // idle
//                        int itemWidth = mmovieAdapter.item_widt;
//
//                        int first_pos = getFirstVisiblePosition(movieListView);
//                        int last_pos = getLastVisiblePosition(movieListView);
//                        int offset = movieListView.computeHorizontalScrollOffset();
//                        //                        //------------------------------------------------------------------------------ latch
//                        if(first_pos == 0){                                                     // first of list
//                            movieListView.scrollToPosition(0);
//                            movie_index = first_pos+1;
//                        }else if(last_pos == (mmovieAdapter.getItemCount()-1)) {
//                            movieListView.scrollToPosition(mmovieAdapter.getItemCount()-1);          // last of list
//                            movie_index = last_pos-1;
//
//                        }else if((offset-(first_pos*itemWidth)) < (itemWidth/2)){               // go left edge
//                            movieListView.scrollToPosition(first_pos);          // fit position
//                            movie_index = first_pos+1;
//                        }else if((offset-(first_pos*itemWidth)) > (itemWidth/2)){               // go right edge
//                            movieListView.scrollToPosition(first_pos+3);        // fit position
//                            movie_index = first_pos+2;
//                        }
//                        mmovieAdapter.setSelectedPosition(movie_index);          // set selected
//                        mmovieAdapter.notifyDataSetChanged();
//                    }
//                }
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy){
//                }
//            });

        }



        mmovieAdapter.notifyDataSetChanged();


        time_text = findViewById(R.id.text_time);

        mtime_Handler.sendMessage( mtime_Handler.obtainMessage(0));
    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_home:{
                finish();
            }
            break;
        }

    }


    public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

        private int selected_pos = 0;

        public int item_widt = -1;

        public MovieAdapter(){
        }

        @Override
        public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_movie, parent, false);

            MovieViewHolder holder = new MovieViewHolder(view);

            return holder;
        }
        //----------------------------------------------------------------------------------------------

        @Override
        public void onBindViewHolder(MovieViewHolder holder, final int position) {

            if(item_widt == -1){
                item_widt =  holder.itemView.getLayoutParams().width ;
            }



            holder.itemView.setVisibility(View.VISIBLE);
//                holder.null_view.setVisibility(View.VISIBLE);

            ViewGroup.LayoutParams params = holder.movie_img.getLayoutParams();

            params.width = 229;
            params.height = 323;

            holder.movie_img.setY(28);

            holder.movie_img.setLayoutParams(params);

            holder.movie_img.setBackgroundResource( movie_img_list[position]);
            holder.movie_item_text.setText(movie_title_list[position]);
//            holder.movie_item_text01.setText(movie_text_list[position]);

            //------------------------------------------------------------------------------------------
//            if(position < 1 ||  local_movie_list.size() + 1 <= position){
//                holder.itemView.setFocusable(false);
//                holder.itemView.setVisibility(View.INVISIBLE);
//            }else if(selected_pos == position){
//                holder.itemView.setVisibility(View.VISIBLE);
////                holder.null_view.setVisibility(View.GONE);
//
//                ViewGroup.LayoutParams params = holder.movie_img.getLayoutParams();
//
//                params.width = 270;
//                params.height = 380;
//
//                holder.movie_img.setY(0);
//
//                holder.movie_img.setLayoutParams(params);
//
//                holder.movie_img.setBackgroundResource(  movie_img_list[position-1]);
//                holder.movie_item_text.setText(movie_title_list[position-1]);
//                holder.movie_item_text01.setText(movie_text_list[position-1]);
//
//
////                binding.movieMainLayout.movieTitleTextview.setText( Util.getMovieTitle(con,   local_movie_list.get(position-2)[1].toString()));
//            }else{
//                holder.itemView.setVisibility(View.VISIBLE);
////                holder.null_view.setVisibility(View.VISIBLE);
//
//                ViewGroup.LayoutParams params = holder.movie_img.getLayoutParams();
//
//                params.width = 229;
//                params.height = 323;
//
//                holder.movie_img.setY(28);
//
//                holder.movie_img.setLayoutParams(params);
//
//                holder.movie_img.setBackgroundResource( movie_img_list[position-1]);
//                holder.movie_item_text.setText(movie_title_list[position-1]);
//                holder.movie_item_text01.setText(movie_text_list[position-1]);
//            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(selected_pos == position) {
                        movie_index = position;
                        int pre_Selected_pos = mmovieAdapter.getSelectedPosition();
                        int pad = 1;

                        if (movie_index <= pad) {
                            movie_index = pad;
                        } else if (movie_index >= (mmovieAdapter.getItemCount() - 1 - pad)) {
                            movie_index = (mmovieAdapter.getItemCount() - 1 - pad);
                        } else if (position < 1 || local_movie_list.size() + 1 <= position) {
                            movie_index = pre_Selected_pos;
                        }

                        Intent intent = new Intent(con, VODPlayerActivity.class);
                        intent.putExtra("index", position);
                        startActivity(intent);

                        /*
                        top_mode = movie_play_mode;

                        init_view();

                        init_video(local_movie_list.get(position-1)[0]);

                        */

//                        shiftPos(movie_index, shift_center, type_movie, binding.movieListView, true);
//                        notifyDataSetChanged();

//                        mmovieAdapter.setSelectedPosition(movie_index);

                    }else{
                        movieListView.smoothScrollToPosition(position);
                    }
//                    movie_start_Handler.sendMessageDelayed(movie_start_Handler.obtainMessage(0), 1000);
                }
            });
        }
        //----------------------------------------------------------------------------------------------

        @Override
        public int getItemCount() {
            return local_movie_list.size();
        }

        //----------------------------------------------------------------------------------------------
        public void setSelectedPosition(int pos) {
            selected_pos = pos;
        }

        public int getSelectedPosition() {

            return selected_pos;
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
}
