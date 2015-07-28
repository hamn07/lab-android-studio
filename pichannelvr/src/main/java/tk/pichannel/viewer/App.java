package tk.pichannel.viewer;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by Henry on 2015/7/28.
 */
public class App extends Application {
    private Drawable drawableNextImage;

    public void setDrawableNextImage(Drawable drawableNextImage) {
        this.drawableNextImage = drawableNextImage;
    }

    public Drawable getDrawableNextImage() {
        return drawableNextImage;
    }
}
