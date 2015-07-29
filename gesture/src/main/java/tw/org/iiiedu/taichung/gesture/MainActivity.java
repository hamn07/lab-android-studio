package tw.org.iiiedu.taichung.gesture;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;


public class MainActivity extends Activity {
    private View view;
    private GestureDetector gd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.v);

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("henry","onClick");
//            }
//        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("henry", "onTouch");
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i("henry", "ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i("henry", "ACTION_MOVE");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i("henry", "ACTION_UP");
                        break;
                    default:
                        Log.i("henry", "Default " + event.getAction());
                        break;
                }


//                return true;
                return gd.onTouchEvent(event);

            }
        });

        view.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Log.i("henry", "onDrag");
                return false;
            }
        });

        gd = new GestureDetector(this,new MyGD());
    }

    private class MyGD extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            Log.i("henry", "onLongPress");

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.i("henry","onScroll");

            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.i("henry","onDown");

//            return super.onDown(e);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.i("henry","onFling");

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}