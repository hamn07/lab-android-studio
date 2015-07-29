package com.example.pagertest;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MainActivity extends Activity {
	private ViewPager pager;
	private LayoutInflater inflater; // 目前UI的浮動版面
	private ArrayList<View> views;	// 存放切換的頁面
	private TextView tv;
	private ImageView[] imgs;
	private int[] imgid = {R.id.img0, R.id.img1, R.id.img2, R.id.img3, R.id.img4};
	private View f1, f2, f3;
	
	private ViewFlipper flipper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv = (TextView)findViewById(R.id.tv);
		imgs = new ImageView[5];
		for (int i=0; i<imgid.length; i++){
			imgs[i] = (ImageView)findViewById(imgid[i]);
			imgs[i].setOnClickListener(new MyImageListener());
		}
		
		pager = (ViewPager)findViewById(R.id.pager);
		initViewPager();
		
	}
	
	private class MyImageListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			int iSel = Arrays.binarySearch(imgid, v.getId());
			Log.i("brad", "" + iSel);
			pager.setCurrentItem(iSel);
		}
	}
	
	
	private void initViewPager(){
		inflater = LayoutInflater.from(this);
		
		// 加入切換的頁面到ArrayList
		views = new ArrayList<View>();
		views.add(inflater.inflate(R.layout.page0, null));
		views.add(inflater.inflate(R.layout.page1, null));
		views.add(inflater.inflate(R.layout.page2, null));
		views.add(inflater.inflate(R.layout.page3, null));
		views.add(inflater.inflate(R.layout.page4, null));

		pager.setOnPageChangeListener(new PageListener());
		pager.setAdapter(new MyPagerAdapter());
		pager.setCurrentItem(1);
		imgs[1].setImageResource(R.drawable.seled);
		
		// 以下處理Page1 中的 flipper
		flipper = (ViewFlipper)views.get(1).findViewById(R.id.flipper);
		f1 = views.get(1).findViewById(R.id.f1);
		f2 = views.get(1).findViewById(R.id.f2);
		f3 = views.get(1).findViewById(R.id.f3);
		
		f1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				flipper.showNext();
			}
		});
		f2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				flipper.showNext();
			}
		});
		f3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				flipper.showNext();
			}
		});
		
		
	}
	
	// 頁面滑動的事件處理
	private class MyPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return views.size();
		}
		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}
		
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager)container).addView(views.get(position));
			return views.get(position);
		}
		
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager)container).removeView(views.get(position));
		}
	}
	
	// 頁面滑動的事件Listener
	private class PageListener extends ViewPager.SimpleOnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
			if (position == 0){
				pager.setCurrentItem(3);
			}else if (position == 4){
				pager.setCurrentItem(1);
			}
		}
		@Override
		public void onPageScrolled(
				int position, float positionOffset, int positionOffsetPixels) {
			tv.setText("Page: " + position);
			for (int i=0; i<imgs.length; i++){
				imgs[i].setImageResource((i==position)?R.drawable.seled:R.drawable.unsel);
			}
		}
	}
}
