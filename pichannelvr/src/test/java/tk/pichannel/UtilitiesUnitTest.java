package tk.pichannel;


import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

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
}
