package tk.pichannel.viewer;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import com.android.volley.RequestQueue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class MainSingletonTest {

    private Context instrumentationCtx;

    @Before
    public void init() {
        instrumentationCtx = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void requestQueue_InstantiateOnlyOnce() {

        RequestQueue requestQueue1 = MainSingleton.getInstance(instrumentationCtx).getRequestQueue();
        RequestQueue requestQueue2 = MainSingleton.getInstance(instrumentationCtx).getRequestQueue();

        assertEquals(requestQueue1.hashCode(),requestQueue2.hashCode());

    }
}