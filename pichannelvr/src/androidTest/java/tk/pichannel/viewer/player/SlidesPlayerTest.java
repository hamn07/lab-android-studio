package tk.pichannel.viewer.player;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class SlidesPlayerTest {

    private SlidesPlayer mPlayer;

    @Before
    public void setUp() throws Exception {

        mPlayer = new SlidesPlayer(InstrumentationRegistry.getTargetContext(),null);


    }

    @Test
    public void prepare() throws Exception {
        mPlayer.prepare();

        assertEquals(1,mPlayer.getCursor().getCount());
    }

}