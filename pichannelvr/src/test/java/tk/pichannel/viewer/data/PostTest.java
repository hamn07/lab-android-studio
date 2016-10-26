package tk.pichannel.viewer.data;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by HamnLee on 2016/10/27.
 */
public class PostTest {

    @Test
    public void testBuild() {
        Post post = new Post.Builder()
                .id("23")
                .userId("hamn07")
                .postUnixtimestampeOriginal("234567")
                .imageSrc("http://pic/ad/cde.jpg")
                .text("hi")
                .build();

        assertEquals("23",post.getId());
        assertEquals("hamn07",post.getUserId());
        assertEquals("234567",post.getPostUnixtimestampeOriginal());
        assertEquals("adcde.jpg",post.getImageFileName());
        assertEquals("http://pic/ad/cde.jpg",post.getImageSrc());
        assertEquals("hi",post.getText());

    }
}