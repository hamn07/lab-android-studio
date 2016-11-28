package tk.pichannel.viewer.player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import tk.pichannel.viewer.R;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class SlidesPlayerTest {

    private SlidesPlayer mPlayer;

    @Before
    public void setUp() throws Exception {

//        mPlayer = new SlidesPlayer(InstrumentationRegistry.getTargetContext(),null);


    }

    @Test
    public void exerciseBitmap() throws IOException {

//        ClassLoader classLoader = getClass().getClassLoader();
//        URL resource = classLoader.getResource("19add8f9e461fc8c4774800d67759d9cb1bbd17.jpg");
//        File file = new File(resource.getPath());
//        URL resource = InstrumentationRegistry.getContext().getPackageResourcePath()
//        File file = new File(resource.getPath());
//        Log.i("henry","getclass---> "+InstrumentationRegistry.getContext().getClass());
//        Log.i("henry","getclass---> "+InstrumentationRegistry.getTargetContext().getPackageResourcePath());
//        Log.i("henry","getclassloader---> "+getClass().getClassLoader().getResource("/").getPath());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bitmap = BitmapFactory.decodeResource(InstrumentationRegistry.getContext().getResources(), tk.pichannel.view.test.R.raw.broken_image, options);

        assertEquals(-1,options.outHeight);
    }

}