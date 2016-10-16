package tk.pichannel;

import org.junit.Assert;

import org.junit.Test;

/**
 * Created by HamnLee on 2016/9/26.
 */
public class UtilitiesTest {

    @Test
    public void testParseURL() {
        String urlString = "http://nyz.com/ux/ui/tk/zn/abc.jpg";
        Assert.assertEquals("abc.jpg",Utilities.getImageFileNameAsStringByURL(urlString));
        Assert.assertEquals("zn", Utilities.getImageFolderNameAsStringByURL(urlString));
    }
}
