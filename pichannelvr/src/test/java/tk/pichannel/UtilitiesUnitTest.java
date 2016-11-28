package tk.pichannel;


import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by HamnLee on 2016/9/26.
 */
public class UtilitiesUnitTest {


    @Test
    public void testParseURL() {
        String urlString = "http://nyz.com/ux/ui/tk/zn/abc.jpg";
        assertEquals("abc.jpg",Utilities.getImageFileNameAsStringByURL(urlString));
        assertEquals("zn", Utilities.getImageFolderNameAsStringByURL(urlString));
    }

    @Test
    public void exerciseBitmap() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("19add8f9e461fc8c41774800d67759d9cb1bbd17.jpg");
        File file = new File(resource.getPath());
        assertTrue(file.exists());
    }
}
