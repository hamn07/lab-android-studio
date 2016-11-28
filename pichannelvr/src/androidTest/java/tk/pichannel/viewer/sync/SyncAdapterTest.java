package tk.pichannel.viewer.sync;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import tk.pichannel.viewer.auth.Authenticator;
import tk.pichannel.viewer.auth.AuthenticatorService;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * Created by HamnLee on 2016/10/24.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class SyncAdapterTest {

    Context mContext = InstrumentationRegistry.getTargetContext();

    @Before
    public void setUp() throws Exception {
        SyncUtils.CreateSyncAccount(mContext);
    }

    @Test
    public void parseJSONArray() throws JSONException {
        final String json_string = "[{'name':'henry'},{'name':'jerry'}]";

        JSONArray jsonArray = new JSONArray(json_string);

        assertEquals(2,jsonArray.length());

        for (int i=0;i<jsonArray.length();i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);

            switch (i) {
                case 0:
                    assertEquals(jsonObject.getString("name"),"henry");
                    break;
                case 1:
                    assertEquals(jsonObject.getString("name"),"jerry");
                    break;
                default:
            }
        }
    }

    @Test
    public void triggerRefresh() {
        SyncUtils.TriggerRefresh();
    }

    @After
    public void tearDown() throws Exception {

    }

}