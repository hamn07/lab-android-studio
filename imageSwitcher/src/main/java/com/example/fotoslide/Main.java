package com.example.fotoslide;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

public class Main extends Activity implements ViewFactory {

	ImageSwitcher imageSwitcher;

	Integer[] imageList = { R.drawable.first, R.drawable.second,
			R.drawable.third, R.drawable.fourth };

	int curIndex = 0;
	int downX, upX;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
		imageSwitcher.setFactory(this);
		imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in));
		imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));

		imageSwitcher.setImageResource(imageList[curIndex]);
		imageSwitcher.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					downX = (int) event.getX();
					Log.i("event.getX()", " downX " + downX);
					return true;
				}

				else if (event.getAction() == MotionEvent.ACTION_UP) {
					upX = (int) event.getX();
					Log.i("event.getX()", " upX " + downX);
					if (upX - downX > 100) {

						// curIndex current image index in array viewed by user
						curIndex--;
						if (curIndex < 0) {
							curIndex = imageList.length - 1;
						}

						imageSwitcher.setInAnimation(AnimationUtils
								.loadAnimation(Main.this,
										android.R.anim.fade_in));
						imageSwitcher.setOutAnimation(AnimationUtils
								.loadAnimation(Main.this,
										android.R.anim.fade_out));

						imageSwitcher.setImageResource(imageList[curIndex]);
						// GalleryActivity.this.setTitle(curIndex);
					}

					else if (downX - upX > -100) {

						curIndex++;
						if (curIndex > imageList.length-1) {
							curIndex = 0;
						}

						imageSwitcher.setInAnimation(AnimationUtils
								.loadAnimation(Main.this,
										android.R.anim.fade_in));
						imageSwitcher.setOutAnimation(AnimationUtils
								.loadAnimation(Main.this,
										android.R.anim.fade_out));

						imageSwitcher.setImageResource(imageList[curIndex]);
						// GalleryActivity.this.setTitle(curIndex);
					}
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public View makeView() {
		ImageView i = new ImageView(this);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		return i;
	}
}
