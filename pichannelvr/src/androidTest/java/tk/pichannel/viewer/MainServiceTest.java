package tk.pichannel.viewer;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by HamnLee on 2016/10/17.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class MainServiceTest {

    @Test
    public void apiHost_fromProductFlavors_isExpectedValue() {
        assertEquals(BuildConfig.API_HOST,"http://52.198.106.239");
        assertEquals(BuildConfig.API_USER_ENDPOINT,"/api/user");
    }

}