package com.pin.train_pin_vod;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.pin.train_pin_vod.Listener.OnSnapPositionChangeListener;


public class HorizontalCarouselRecyclerView extends RecyclerView {

	private SnapHelper snapHelper;

	private OnSnapPositionChangeListener onSnapPositionChangeListener;

	private int snapPosition = RecyclerView.NO_POSITION;

	public HorizontalCarouselRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public HorizontalCarouselRecyclerView(@NonNull Context context) {
		super(context);
	}

	public void initialize(Adapter newAdapter,SnapHelper snapHelper_, OnSnapPositionChangeListener onSnapPositionChangeListener_){

		this.snapHelper = snapHelper_;
		this.onSnapPositionChangeListener = onSnapPositionChangeListener_;

		LinearLayoutManager aa = new LinearLayoutManager(getContext(), HORIZONTAL, false);
		setLayoutManager(aa);


		newAdapter.registerAdapterDataObserver(new AdapterDataObserver(){

			@Override
			public void onChanged() {
				super.onChanged();
				post(new Runnable() {
					@Override
					public void run() {
						int sidePadding = (getWidth() / 2) - (getChildAt(0).getWidth() / 2);
						setPadding(sidePadding, 0, sidePadding, 0);
						scrollToPosition(0);
						addOnScrollListener(new OnScrollListener() {
							@Override
							public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
								super.onScrolled(recyclerView, dx, dy);
								onScrollChanged();
							}

							@Override
							public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
								super.onScrollStateChanged(recyclerView, newState);

								if (newState == RecyclerView.SCROLL_STATE_IDLE) {
									maybeNotifySnapPositionChange(recyclerView);
								}
							}
						});

					}
				});
			}
		});
		setAdapter(newAdapter);

	}

	private void maybeNotifySnapPositionChange(RecyclerView recyclerView) {
		int  snapPosition = getSnapPosition(recyclerView);
		boolean snapPositionChanged = this.snapPosition != snapPosition;
		if (snapPositionChanged) {
			onSnapPositionChangeListener.onSnapPositionChange(snapPosition);
			this.snapPosition = snapPosition;
		}
	}


	private int getSnapPosition(RecyclerView recyclerView) {
		RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
		if (layoutManager == null) {
			return RecyclerView.NO_POSITION;
		}

		View snapView = snapHelper.findSnapView(layoutManager);
		if (snapView == null) {
			return RecyclerView.NO_POSITION;
		}

		return layoutManager.getPosition(snapView);
	}

	public void onScrollChanged(){
		post(new Runnable() {

			@Override
			public void run() {

				for(int i = 0; i< getChildCount(); i ++){

					View child = getChildAt(i);
					int childCenterX = (child.getLeft() + child.getRight()) /2;
					Float scaleValue = getGaussianScale(childCenterX, 0.75f, 0.5f, (double) 150);

					child.setScaleX(scaleValue);
					child.setScaleY(scaleValue);

				}

			}
		});
	}


	private  Float getGaussianScale(int childCenterX, Float minScaleOffest, Float scaleFactor, Double spreadFactor){

		int recyclerCenterX = (getLeft() + getRight()) / 2;

		Double db = new Double((Math.pow(
				Math.E,
				-Math.pow(childCenterX - recyclerCenterX, 2) / (2 * Math.pow(
						spreadFactor,
						2
				))
		) * scaleFactor + minScaleOffest));

		return db.floatValue();


//		return (Math.pow(
//				Math.E,
//				-Math.pow(childCenterX - recyclerCenterX, 2) / (2 * Math.pow(
//						spreadFactor,
//						2
//				))
//		) * scaleFactor + minScaleOffest).floatValue();

	}





//	fun <T : ViewHolder> initialize(newAdapter: Adapter<T>) {
//		layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
//		newAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
//			override fun onChanged() {
//				post {
//					val sidePadding = (width / 2) - (getChildAt(0).width / 2)
//					setPadding(sidePadding, 0, sidePadding, 0)
//					scrollToPosition(0)
//					addOnScrollListener(object : OnScrollListener() {
//						override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//							super.onScrolled(recyclerView, dx, dy)
//							onScrollChanged()
//						}
//					})
//				}
//			}
//		})
//		adapter = newAdapter
//	}


//	public ImageView movie_img;
//	public TextView movie_item_text;
//	public TextView movie_item_text01;
//
//
//	public HorizontalCarouselRecyclerView(View itemView) {
//		super(itemView);
//
//		movie_img = (ImageView) itemView.findViewById(R.id.movie_img);
//		movie_item_text = (TextView) itemView.findViewById(R.id.movie_item_text);
//		movie_item_text01 = (TextView) itemView.findViewById(R.id.movie_item_text01);
//
//	}
}
