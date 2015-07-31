package tw.org.iiiedu.taichung.sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends Activity {

    private SensorManager mgr;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private boolean isSensorEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> list = mgr.getSensorList(Sensor.TYPE_ALL);


//        for (Sensor sensor:list) {
//            Log.i("henry",sensor.getName()+" : "+sensor.getType()+" : "+sensor.getVendor());
//        }
        sensor = mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor!=null) {
            isSensorEnable=true;
        }
        /**
         * define
         */
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
//                Log.i("henry","sensor OK");
                float[] values = event.values;

                ((TextView)findViewById(R.id.tv1)).setText("x : "+values[0]);
                ((TextView)findViewById(R.id.tv2)).setText("y : "+values[1]);
                ((TextView)findViewById(R.id.tv3)).setText("z : "+values[2]);

                MyView mv = (MyView) findViewById(R.id.mv);

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSensorEnable) {
            mgr.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isSensorEnable) {
            mgr.unregisterListener(sensorEventListener, sensor);
        }
    }
}
