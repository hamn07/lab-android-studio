package tk.pichannel.viewer.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.text.TextUtils;


public class PichannelContentProvider extends ContentProvider {

    private static final String TAG = PichannelContentProvider.class.getName();
    private PichannelDbOpenHelper pichannelDbOpenHelper;
    private SQLiteDatabase db;
    public static String AUTHORITY="tk.pichannel.provider";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int POSTS=1;
    private static final int POSTS_PER_USER_ID =2;

    static {
//        e.g., content://tk.pichannel.provider/post
        sUriMatcher.addURI(AUTHORITY, Post.TABLE_NAME, POSTS);
//        e.g., content://tk.pichannel.provider/post/hamn07
        sUriMatcher.addURI(AUTHORITY, Post.TABLE_NAME+"/*", POSTS_PER_USER_ID);
    }

    public static final class Post implements BaseColumns {

        public static final Uri CONTENT_URI =
                Uri.parse("content://"+AUTHORITY+"/"+PostTable.TABLE_NAME);

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.tk.pichannel.provider.post";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.tk.pichannel.provider.post";

        public static final String TABLE_NAME = PostTable.TABLE_NAME;
        public static final String ID = PostTable.COLUMN_ID;
        public static final String POST_UNIXTIMESTAMP_ORIGINAL = PostTable.COLUMN_POST_UNIXTIMESTAMP_ORIGINAL;
        public static final String USER_ID = PostTable.COLUMN_USER_ID;
        public static final String IMAGE_FILE_NAME = PostTable.COLUMN_IMAGE_FILE_NAME;
        public static final String TEXT = PostTable.COLUMN_TEXT;

        public static final String DEFAULT_SORT_ORDER = PostTable.COLUMN_POST_UNIXTIMESTAMP_ORIGINAL+" DESC ";


    }

    @Override
    public boolean onCreate() {

        pichannelDbOpenHelper = new PichannelDbOpenHelper(
                getContext(),
                PichannelDbOpenHelper.DATABASE_NAME,
                null,
                PichannelDbOpenHelper.SCHEMA_VERSION);

        return pichannelDbOpenHelper!=null;

    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sort) {
        SQLiteQueryBuilder queryBuilder=new SQLiteQueryBuilder();
//      TODO  checkColumns(projection);


        switch (sUriMatcher.match(uri)) {

//            get all posts
            case POSTS:

                queryBuilder.setTables(Post.TABLE_NAME);
                break;


//            get someone's posts
            case POSTS_PER_USER_ID:

                queryBuilder.setTables(Post.TABLE_NAME);
                queryBuilder.appendWhere(PostTable.COLUMN_USER_ID + "='"
                        + uri.getLastPathSegment()+"'");
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI for query: "+uri);
        }


        String orderBy;

        if (TextUtils.isEmpty(sort)) {
            orderBy = Post.DEFAULT_SORT_ORDER;
        }
        else {
            orderBy = sort;
        }

        Cursor c=queryBuilder.query(pichannelDbOpenHelper.getReadableDatabase(), projection, selection,
                        selectionArgs, null, null, orderBy);

        c.setNotificationUri(getContext().getContentResolver(), uri);

        return(c);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        switch (sUriMatcher.match(uri)) {

            // multiple rows
            case POSTS:
                return Post.CONTENT_TYPE;

            // multiple rows
            case POSTS_PER_USER_ID:
                return Post.CONTENT_TYPE;

            default:
                throw new IllegalArgumentException("Unsupported URI for type: "+uri);
        }
    }

    /**
     *
     * @param uri
     * @param contentValues
     * @return original uri means CONFLICT_IGNORE
     *         original uri appended a id means INSERT SUCCESS
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        switch (sUriMatcher.match(uri)) {

            case POSTS:
//                long rowID=
//                        pichannelDbOpenHelper.getWritableDatabase().insertWithOnConflict(
//                                PostTable.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                long rowID=0;

                try {
                    rowID = pichannelDbOpenHelper.getWritableDatabase().insertOrThrow(
                                     PostTable.TABLE_NAME, null, contentValues);

                } catch (SQLException e) {

                    if (e instanceof SQLiteConstraintException)
                        pichannelDbOpenHelper.getWritableDatabase().insertWithOnConflict(
                                PostTable.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                    else
                        e.printStackTrace();
                }

                if (rowID > 0) {

                    Uri insertedRowUri=
                            ContentUris.withAppendedId(PichannelContentProvider.Post.CONTENT_URI,
                                    rowID);

                    getContext().getContentResolver().notifyChange(insertedRowUri, null);

                    return(insertedRowUri);
                }
                else {
                    return uri;
                }

            case POSTS_PER_USER_ID:
                throw new IllegalArgumentException("Unsupported URI for insert: "+uri);

            default:
                throw new IllegalArgumentException("Unsupported URI for insert: "+uri);
        }





    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
