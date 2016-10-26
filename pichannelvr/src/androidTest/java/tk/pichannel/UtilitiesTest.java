package tk.pichannel;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

/**
 * Created by HamnLee on 2016/10/27.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class UtilitiesTest {

    Context mContext = InstrumentationRegistry.getTargetContext();

    @Test
    public void fileExists() throws Exception {

        assertFalse(Utilities.fileExists(mContext,"not_exists_file.txt"));
        assertTrue(Utilities.fileExists(mContext,"d5bdc710cb0923b9b2bce871c9cd5661cd8a6b41.jpg"));
    }

}