package tk.pichannel.viewer.player;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
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
    private static final int ERROR_DECODED_BITMAP_WIDTH = -1;
    private final int mDisplayWidth;
    private final int mDisplayHeight;

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
        mDisplayHeight = mainActivity.getResources().getDisplayMetrics().heightPixels;
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
        //   3) render slide on UI thread with bitmap & text,
        mTimer = new Timer();
        mTimer.schedule(new SlideWorkerTask(),
                SLIDES_START_DELAY_MILLISECONDS, SLIDES_INTERVAL_MILLISECONDS);
    }

    public void destroy() {

        if (mCursor!=null) {
            mCursor.close();
            mCursor=null;
        }

        if (mTimer!=null){
            mTimer.cancel();
            mTimer=null;
        }
    }

    private class SlideWorkerTask extends TimerTask {

        Bitmap bitmap;

        Post post;

        @Override
        public void run() {

            fetchPost(mCursor);

            Log.i(TAG, "((post content)) = "+post.toString());
            decodeFileAsBitmap(post.getImageFileName());

            renderSlideOnUiThread();

        }

        private void fetchPost(Cursor cursor) {

            if (!cursor.moveToNext()) {
                cursor.moveToFirst();
            }

            final int columnIndex_ImageFileName = cursor.getColumnIndex(PostTable.COLUMN_IMAGE_FILE_NAME);
            final int columnIndex_Text = cursor.getColumnIndex(PostTable.COLUMN_TEXT);

            final String imageFileName = cursor.getString(columnIndex_ImageFileName);
            final String text = cursor.getString(columnIndex_Text);

            this.post = new Post.Builder()
                    .imageFileName(imageFileName)
                    .text(text)
                    .build();

        }

        private void decodeFileAsBitmap(String imageFileName) {

            final String imageFilePath = mContext.getFilesDir().getAbsolutePath()+"/"+imageFileName;



            // check image dimensions only.
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imageFilePath, options);

            // calculate inSampleSize
            int width = mDisplayWidth;
            int height = mDisplayWidth;
            options.inSampleSize = calculateInSampleSize(options,width,height);

            // decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            this.bitmap = BitmapFactory.decodeFile(imageFilePath,options);

            if (options.outWidth == ERROR_DECODED_BITMAP_WIDTH) {
                new File(imageFilePath).delete();
                this.bitmap = null;
                return;
            }

            rotateBitmap(imageFilePath);

            scaleBitmap();

        }

        private void scaleBitmap() {
            int scaledWidth = mDisplayWidth;
            int scaledHeight = (scaledWidth*bitmap.getHeight())/bitmap.getWidth();

            this.bitmap = Bitmap.createScaledBitmap(bitmap,scaledWidth,scaledHeight,true);
        }

        private void rotateBitmap(String imageFilePath) {
            ExifInterface exifInterface = null;
            try {
                exifInterface = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            Matrix matrix = new Matrix();
            float degree;
            switch (orientation) {
                case ExifInterface.ORIENTATION_UNDEFINED:
                    return;
                case ExifInterface.ORIENTATION_NORMAL:
                    return;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree=90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree=180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree=270;
                    break;
                default:
                    return;
            }
            matrix.setRotate(degree);

            this.bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);

        }

        private int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) >= reqHeight
                        && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }

        private void renderSlideOnUiThread() {

            if (bitmap == null) {
                return;
            }

            mHandler.post(new Runnable() {

                @Override
                public void run() {


                    mImageView.setImageBitmap(bitmap);
                    mImageView.startAnimation(mAnimation);

                    mTextView.setText(post.getText());
                }
            });
        }
    }
}
