package tw.org.iiiedu.taichung.sound;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.IOException;


public class VideoActivity extends Activity implements SurfaceHolder.Callback {

    private SurfaceView sv;
    private SurfaceHolder holder;
    private MediaRecorder mr;
    private Camera camera;
    private File sdroot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        sv = (SurfaceView) findViewById(R.id.sv);
        holder = sv.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        sdroot = Environment.getExternalStorageDirectory();
    }

    private void initRecoder(Surface surface){

        if (camera==null) {
            camera = Camera.open();
            camera.unlock();
        }
        if (mr==null) {
            mr = new MediaRecorder();
        }
        mr.setPreviewDisplay(surface);
        mr.setCamera(camera);

        mr.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mr.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mr.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

        mr.setOutputFile(new File(sdroot, "henry_video.mp4").getAbsolutePath());
        mr.setVideoFrameRate(30);
        mr.setMaxDuration(-1);

        try {
            mr.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start(View v) {
        initRecoder(holder.getSurface());
        mr.start();
    }

    private void stop(View v) {
        mr.stop();
        mr.reset();
        mr=null;
    }

    /**
     * This is called immediately after the surface is first created.
     * Implementations of this should start up whatever rendering code
     * they desire.  Note that only one thread can ever draw into
     * a {@link Surface}, so you should not draw into the Surface here
     * if your normal rendering will be in another thread.
     *
     * @param holder The SurfaceHolder whose surface is being created.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    /**
     * This is called immediately after any structural changes (format or
     * size) have been made to the surface.  You should at this point update
     * the imagery in the surface.  This method is always called at least
     * once, after {@link #surfaceCreated}.
     *
     * @param holder The SurfaceHolder whose surface has changed.
     * @param format The new PixelFormat of the surface.
     * @param width  The new width of the surface.
     * @param height The new height of the surface.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * This is called immediately before a surface is being destroyed. After
     * returning from this call, you should no longer try to access this
     * surface.  If you have a rendering thread that directly accesses
     * the surface, you must ensure that thread is no longer touching the
     * Surface before returning from this function.
     *
     * @param holder The SurfaceHolder whose surface is being destroyed.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera!=null) {
            camera.release();
            camera=null;
        }
    }
}
