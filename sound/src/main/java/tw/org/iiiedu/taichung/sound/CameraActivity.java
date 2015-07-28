package tw.org.iiiedu.taichung.sound;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class CameraActivity extends Activity {

    Camera camera;
    FrameLayout frame;
    CameraPreview preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        frame = (FrameLayout) findViewById(R.id.flayout);

        camera = Camera.open();

        preview = new CameraPreview(this,camera);
        frame.addView(preview);

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(new MyShutter(),null,new MyPicCallBack());
            }
        });

    }

    public void ok(View v) {
        camera.takePicture(new MyShutter(),null,new MyPicCallBack());

    }

    class MyShutter implements Camera.ShutterCallback {

        /**
         * Called as near as possible to the moment when a photo is captured
         * from the sensor.  This is a good opportunity to play a shutter sound
         * or give other feedback of camera operation.  This may be some time
         * after the photo was triggered, but some time before the actual data
         * is available.
         */
        @Override
        public void onShutter() {

        }
    }

    class MyPicCallBack implements Camera.PictureCallback {

        /**
         * Called when image data is available after a picture is taken.
         * The format of the data depends on the context of the callback
         * and {@link Camera.Parameters} settings.
         *
         * @param data   a byte array of the picture data
         * @param camera the Camera service object
         */
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pic = new File(Environment.getExternalStorageDirectory(),"henry_takephoto.jpg");

            try {

                FileOutputStream fos = new FileOutputStream(pic);
                fos.write(data);
                fos.flush();
                fos.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
