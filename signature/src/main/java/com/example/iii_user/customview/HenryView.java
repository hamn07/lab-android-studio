package com.example.iii_user.customview;

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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by iii-user on 15/7/16.
 */
public class HenryView extends View{

    private int viewW, viewH;
    private Resources res;
    private Bitmap ball;
    private Paint paint;
    private float ballW, ballH;
    private float ballX, ballY;
    private boolean isInit;
    private LinkedList<HashMap<String,Float>> signList;
    private Timer timer = new Timer();

    public HenryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.GREEN);

        res = context.getResources();
        ballX=ballY=0;

        timer = new Timer();
    }
    private void init(){
        viewW = getWidth();
        viewH = getHeight();

        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(4f);

        ball = BitmapFactory.decodeResource(res,R.drawable.basketball);

        ballW = viewW/2f;
        ballH = viewH/2f;

        Matrix matrix = new Matrix();
        matrix.setScale(ballW/ball.getWidth(),ballH/ball.getHeight());

        ball = Bitmap.createBitmap(ball,0,0,ball.getWidth(),ball.getHeight(),matrix,false);

        signList = new LinkedList<HashMap<String,Float>>();

        isInit = true;

        timer.schedule(new RunBallTimerTask(),500,80);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInit){
            init();
        }
        canvas.drawBitmap(ball,ballX,ballY,null);

        for (int i=1;i<signList.size();i++){
            HashMap<String, Float> p0 = signList.get(i-1);
            HashMap<String, Float> p1 = signList.get(i);
            canvas.drawLine(p0.get("x"),p0.get("y"),p1.get("x"),p1.get("y"),paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        event.getAction() == MotionEvent.ACTION_DOWN

        float tx = event.getX();
        float ty = event.getY();
        Log.i("henry","x: " + tx + " y : "+ty);
        ballX=tx;ballY=ty;

        HashMap<String, Float> p = new HashMap<String, Float>();
        p.put("x",tx);
        p.put("y",ty);
        signList.add(p);

        // trigger onDraw() again

        invalidate();


        //return super.onTouchEvent(event);
        return true;
    }
    private class RunBallTimerTask extends TimerTask{
        private int dx=10;
        private int dy=10;
        @Override
        public void run() {
            ballX+=dx;
            ballY+=dy;
            postInvalidate();
        }
    }
}
