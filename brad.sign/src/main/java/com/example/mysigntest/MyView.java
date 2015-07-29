package com.example.mysigntest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {
	private Paint paint;
	private int viewW, viewH;
	private Resources res;
	private Bitmap ball;
	private float ballW, ballH, ballX, ballY;
	private boolean isInit;
	private LinkedList<LinkedList<HashMap<String,Float>>> lines;
	private LinkedList<LinkedList<HashMap<String,Float>>> recycle;
	private Timer timer;
	private runBall rBall;
	
	private GestureDetector gd;
	
	
	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//setBackgroundColor(Color.GREEN);
		setBackgroundColor(Color.TRANSPARENT);
		
		gd = new GestureDetector(context, new MyGDListener());
		
		timer = new Timer();
		res = context.getResources();
		ballX = ballY = 0;
		isInit = false;
	}
	
	private class MyGDListener extends SimpleOnGestureListener {
		@Override
		public boolean onDown(MotionEvent e) {
			Log.i("brad", "down");
			return true;
		}
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			Log.i("brad", "fling:" + velocityX + " x " + velocityY);
			rBall.setDxDy((int)(velocityX/100), (int)(velocityY/100));
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}
	
	Timer getTimer(){
		return timer;
	}
	
	private void init(){
		viewW = getWidth();
		viewH = getHeight();
		
		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStrokeWidth(4);

		ball = BitmapFactory.decodeResource(
					res, R.drawable.ball);
		ballW = viewW / 8f; ballH = ballW;
		Matrix matrix = new Matrix();
		
		matrix.reset();
		matrix.setScale(ballW/ball.getWidth(), 
				ballH/ball.getHeight());
		
		ball = Bitmap.createBitmap(
			ball, 0, 0, 
			ball.getWidth(), ball.getHeight(), 
			matrix, false);
		
		lines = new LinkedList<LinkedList<HashMap<String,Float>>>();
		recycle = new LinkedList<LinkedList<HashMap<String,Float>>>();
		
		rBall = new runBall(10, 10);
		timer.schedule(rBall, 500, 80);
		
		isInit = true;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!isInit) init();
		
		canvas.drawCircle(100, 100, 30, paint);
		canvas.drawBitmap(ball, ballX, ballY, null);
		
		for(LinkedList<HashMap<String,Float>> line : lines){
			for (int i=1; i<line.size(); i++){
				HashMap<String,Float> p0 = line.get(i-1);
				HashMap<String,Float> p1 = line.get(i);
				canvas.drawLine(p0.get("x"), p0.get("y"), 
						p1.get("x"), p1.get("y"), paint);
			}
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//event.getAction() == MotionEvent.ACTION_DOWN
		
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			LinkedList<HashMap<String,Float>> line = 
				new LinkedList<HashMap<String,Float>>();
			lines.add(line);
		}
		
		float tx = event.getX();
		float ty = event.getY();
		//Log.i("brad", tx + " x " + ty);
		//ballX = tx - ballW/2f; ballY = ty - ballH/2f;
		HashMap<String,Float> point = 
			new HashMap<String, Float>();
		point.put("x", tx); point.put("y", ty);
		
		lines.getLast().add(point);
		
		invalidate();
		
		//return super.onTouchEvent(event);
		return gd.onTouchEvent(event);
	}
	
	void undo(){
		if (lines.size()>0){
			recycle.add(lines.removeLast());
			invalidate();
		}
		
	}
	void redo(){
		if (recycle.size()>0){
			lines.add(recycle.removeLast());
			invalidate();
		}
		
	}
	
	private class runBall extends TimerTask {
		private int dx = 10, dy = 10;
		runBall(int dx, int dy){
			this.dx = dx; this.dy = dy;
		}
		void setDxDy(int x, int y){
			this.dx = x; this.dy = y;
			postInvalidate();
		}
		@Override
		public void run() {
			if (ballY + ballH > viewH || ballY < 0){
				dy *= -1;
			}
			if (ballX + ballW > viewW || ballX < 0){
				dx *= -1;
			}
			ballX += dx;
			ballY += dy;
			//invalidate();		// 本物件的update
			postInvalidate();	// Thread的update
		}
	}
	

}
