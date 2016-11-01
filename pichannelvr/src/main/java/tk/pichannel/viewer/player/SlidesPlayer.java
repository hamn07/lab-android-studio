package tk.pichannel.viewer.player;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import tk.pichannel.viewer.MainActivity;
import tk.pichannel.viewer.R;
import tk.pichannel.viewer.data.PichannelContentProvider;
import tk.pichannel.viewer.data.Post;
import tk.pichannel.viewer.data.PostTable;


public class SlidesPlayer {
    private static final String TAG = "SlidesPlayer";

    private static final long SLIDES_START_DELAY_MILLISECONDS = 500L;
    private static final long SLIDES_INTERVAL_MILLISECONDS = 10000L;
    private final int mDisplayWidth;

    private Context mContext;

    private Cursor mCursor;
    private Timer mTimer;
    private Handler mHandler = new Handler();

    private final TextView mTextView;
    private final ImageView mImageView;
    private Animation mAnimation;


    public SlidesPlayer(Context context, MainActivity mainActivity) {
        mContext = context;
        mAnimation = AnimationUtils.loadAnimation(mainActivity, R.anim.scale);
        mImageView = (ImageView) mainActivity.findViewById(R.id.iv);
        mTextView = (TextView) mainActivity.findViewById(R.id.tv);
        mDisplayWidth = mainActivity.getResources().getDisplayMetrics().widthPixels;
    }

    public void prepare() {

        Uri uri = PichannelContentProvider.Post.CONTENT_URI;
        String[] projection = {PostTable.COLUMN_IMAGE_FILE_NAME, PostTable.COLUMN_TEXT};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;

        mCursor = mContext.getContentResolver().query(uri,projection,selection,selectionArgs,sortOrder);
    }

    public void start() {

        if (mCursor.getCount()<1)
            return;

        // initialize Timer as Worker Thread to,
        //   1) fetch post from mCursor,
        //   2) decode image file as bitmap,
        //   3) render slide on UI thread,
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                final Post post = fetchPost(mCursor);

                final Bitmap bitmap = decodeFileAsBitmap(post.getImageFileName());

                renderSlideOnUiThread(bitmap, post.getText());

            }

            private Post fetchPost(Cursor cursor) {

                if (!cursor.moveToNext()) {
                    cursor.moveToFirst();
                }

                final int columnIndex_ImageFileName = cursor.getColumnIndex(PostTable.COLUMN_IMAGE_FILE_NAME);
                final int columnIndex_Text = cursor.getColumnIndex(PostTable.COLUMN_TEXT);

                final String imageFileName = cursor.getString(columnIndex_ImageFileName);
                final String text = cursor.getString(columnIndex_Text);

                Post post = new Post.Builder()
                        .imageFileName(imageFileName)
                        .text(text)
                        .build();

                return post;
            }

            private Bitmap decodeFileAsBitmap(String imageFileName) {

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap bitmap = BitmapFactory.decodeFile(mContext.getFilesDir().getAbsolutePath()+"/"+imageFileName, options);

                        int width = mDisplayWidth;
                        int height = (width*bitmap.getHeight())/bitmap.getWidth();
                        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

                        return bitmap;
            }

            private void renderSlideOnUiThread(final Bitmap bitmap, final String text) {

                mHandler.post(new Runnable() {

                    @Override
                    public void run() {

                        mImageView.setImageBitmap(bitmap);
                        mImageView.startAnimation(mAnimation);

                        mTextView.setText(text);
                    }
                });
            }

        }, SLIDES_START_DELAY_MILLISECONDS, SLIDES_INTERVAL_MILLISECONDS);
    }

    public void destroy() {

        if (mCursor!=null) {
            mCursor.close();
            mCursor=null;
        }

//        mHandler.removeCallbacks(mSwitchImageViewTask);

        if (mTimer!=null){
            mTimer.cancel();
            mTimer=null;
        }
    }

    Cursor getCursor() {
        return mCursor;
    }
}
