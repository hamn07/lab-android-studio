package tk.pichannel.viewer.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

import tk.pichannel.viewer.R;

public class MusicPlayer implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private MediaPlayer mMediaPlayer;
    private final Context mContext;
    private final Uri mMusicUri;


    public MusicPlayer(Context context) {

        mContext = context;

        mMusicUri = Uri.parse("android.resource://"+mContext.getPackageName()+"/"+ R.raw.music);

    }

    public void prepare() {

        mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.setDataSource(mContext, mMusicUri);
            // TODO load music from remote URL
//            mMediaPlayer.setDataSource("https://dl.dropboxusercontent.com/u/16245733/Ben%20E.%20King%20-%20Stand%20by%20me.mp3");

        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
    }

    /**
     * Called when the media file is ready for playback.
     *
     * @param mp the MediaPlayer that is ready for playback
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public void start() {

        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);

        mMediaPlayer.prepareAsync();

    }

    public void destroy() {

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * Called to indicate an error.
     *
     * @param mp    the MediaPlayer the error pertains to
     * @param what  the type of error that has occurred:
     *              <ul>
     *              <li>{@link #MEDIA_ERROR_UNKNOWN}
     *              <li>{@link #MEDIA_ERROR_SERVER_DIED}
     *              </ul>
     * @param extra an extra code, specific to the error. Typically
     *              implementation dependent.
     *              <ul>
     *              <li>{@link #MEDIA_ERROR_IO}
     *              <li>{@link #MEDIA_ERROR_MALFORMED}
     *              <li>{@link #MEDIA_ERROR_UNSUPPORTED}
     *              <li>{@link #MEDIA_ERROR_TIMED_OUT}
     *              <li><code>MEDIA_ERROR_SYSTEM (-2147483648)</code> - low-level system error.
     *              </ul>
     * @return True if the method handled the error, false if it didn't.
     * Returning false, or not having an OnErrorListener at all, will
     * cause the OnCompletionListener to be called.
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }
}
