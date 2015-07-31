package com.example.mycustomview;

import java.util.HashMap;
import java.util.LinkedList;

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
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {
	private int viewW, viewH;
	private Resources res;
	private Bitmap army;
	private float armyW, armyH;
	private float armyX, armyY;
	private LinkedList<LinkedList<HashMap<String,Float>>> lines;
	private LinkedList<LinkedList<HashMap<String,Float>>> recycle;
	private boolean isInit;
	private Paint paintLine;
	
	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setBackgroundColor(Color.GREEN);
		res = context.getResources();
		
		paintLine = new Paint();
		paintLine.setStrokeWidth(4);
		paintLine.setColor(Color.BLUE);
		
		isInit = false;
		armyX = armyY = 0;
		lines = new LinkedList<LinkedList<HashMap<String,Float>>>();
		recycle = new LinkedList<LinkedList<HashMap<String,Float>>>();
	}
	private void init(){
		viewW = getWidth(); viewH = getHeight();
		armyW = viewW / 4f; armyH = armyW;
		army = BitmapFactory.decodeResource(res, R.drawable.army);
		Matrix matrix = new Matrix();
		matrix.reset();
		matrix.setScale(armyW/army.getWidth(), armyH/army.getHeight());
		army = Bitmap.createBitmap(army, 0, 0, army.getWidth(), army.getHeight(), matrix, false);
		isInit = true;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
		if (!isInit) init();
		
		canvas.drawBitmap(army, armyX, armyY, null);
		
//		for (int j=0; j<lines.size(); j++){
//			LinkedList<HashMap<String,Float>> line = lines.get(j);
//			for (int i=1; i<line.size(); i++){
//				HashMap<String, Float> p0 = line.get(i-1);
//				HashMap<String,Float> p1 = line.get(i);
//				canvas.drawLine(p0.get("x"), p0.get("y"), p1.get("x"), p1.get("y"), paintLine);
//			}
//		}
		
		for (LinkedList<HashMap<String,Float>> line : lines){
			for (int i=1; i<line.size(); i++){
				HashMap<String, Float> p0 = line.get(i-1);
				HashMap<String,Float> p1 = line.get(i);
				canvas.drawLine(p0.get("x"), p0.get("y"), p1.get("x"), p1.get("y"), paintLine);
			}
		}
	}
	
	public void clear(){
		lines.clear();
		invalidate();
	}
	
	public void undo(){
		if (lines.size()>0){
			recycle.add(lines.removeLast());
			invalidate();
		}
	}
	public void redo(){
		if (recycle.size()>0){
			lines.add(recycle.removeLast());
			invalidate();
		}
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		armyX = event.getX() - armyW / 2f; armyY = event.getY() - armyH / 2f;
//		invalidate();

		if (event.getAction() == MotionEvent.ACTION_DOWN){
			LinkedList<HashMap<String,Float>> line = 
				new LinkedList<HashMap<String,Float>>();
			lines.add(line);
		}
		
		
		float px = event.getX(), py = event.getY();
		HashMap<String, Float> point = new HashMap<String, Float>();
		point.put("x", px); point.put("y", py);
		lines.getLast().add(point);
		invalidate();
		
		return true;
		//return super.onTouchEvent(event);
	}
	
	
}
