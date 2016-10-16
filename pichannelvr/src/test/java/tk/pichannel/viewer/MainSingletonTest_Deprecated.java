package tk.pichannel.viewer;

import android.content.Context;
import android.test.mock.MockContext;

import com.android.volley.RequestQueue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;


/**
 * Created by HamnLee on 2016/9/30.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MainSingleton.class}) // Prepare the static classes for mocking
public class MainSingletonTest_Deprecated {

    @Mock
    Context mMockContext;

    @Mock
    RequestQueue mRequestQueue1;

    @Mock
    RequestQueue mRequestQueue2;

    @Before
    public void init() {
        mockStatic(MainSingleton.class);
    }

    @Test
    public void requestQueue_InstantiateOnlyOnce() {

        when(mMockContext.getApplicationContext()).thenReturn(new MockContext());

        when(MainSingleton.getInstance(mMockContext).getRequestQueue())
                .thenReturn(mRequestQueue1);
//        when(MainSingleton.getInstance(mMockContext).getRequestQueue())
//                .thenReturn(mRequestQueue2);

        assertEquals(mRequestQueue1.getSequenceNumber(),mRequestQueue2.getSequenceNumber());
    }

}