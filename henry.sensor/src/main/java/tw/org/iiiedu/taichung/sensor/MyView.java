package tw.org.iiiedu.taichung.sensor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by iii-user on 15/7/31.
 */
public class MyView extends View {

    private boolean isInit;
    private float viewW,viewH, ballX, ballY,ballR;
    private Paint paintBall;

    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     * <p/>
     * <p/>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     * @see #View(Context, AttributeSet, int)
     */
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInit) {
            init();
        }
        canvas.drawCircle(ballX, ballY, ballR, paintBall);
    }

    private void init() {
        viewW = getWidth();
        viewH = getHeight();
        ballR = viewH/8;
        ballX = viewW/2;
        ballY = viewH/2;

        paintBall = new Paint();
        paintBall.setColor(Color.BLUE);

        isInit = true;
    }

    void setXY(float x,float y){
        ballX = x;
        ballY = y;

        invalidate();
    }
}
