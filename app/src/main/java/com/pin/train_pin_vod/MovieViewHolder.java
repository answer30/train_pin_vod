package com.pin.train_pin_vod;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;



public class MovieViewHolder extends RecyclerView.ViewHolder {

	public ImageView movie_img;
	public TextView movie_item_text;
	public TextView movie_item_text01;


	public MovieViewHolder(View itemView) {
		super(itemView);

		movie_img = (ImageView) itemView.findViewById(R.id.movie_img);
		movie_item_text = (TextView) itemView.findViewById(R.id.movie_item_text);
		movie_item_text01 = (TextView) itemView.findViewById(R.id.movie_item_text01);

	}



}
