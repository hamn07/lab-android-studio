package tk.pichannel.viewer.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.UserDictionary;
import android.support.annotation.Nullable;
import android.text.TextUtils;



public class PichannelContentProvider extends ContentProvider {

    private PichannelDbOpenHelper pichannelDbOpenHelper;
    private SQLiteDatabase db;
    public static String AUTHORITY="tk.pichannel.provider";

    private static final UriMatcher uriMatcher;
    private static final int POSTS=1;
    private static final int POSTS_PER_USER_ID =2;
//    public static final String CONTENT_TYPE= ContentResolver.CURSOR_DIR_BASE_TYPE+"/posts";
//    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/post";

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, Post.TABLE_NAME, POSTS);
        uriMatcher.addURI(AUTHORITY, Post.TABLE_NAME+"/*", POSTS_PER_USER_ID);
    }

    public static final class Post implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+PostTable.TABLE_NAME);
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.pichannel.post";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.pichannel.post";

        public static final String TABLE_NAME = PostTable.TABLE_NAME;
        public static final String ID = PostTable.COLUMN_ID;
        public static final String POST_UNIXTIMESTAMP_ORIGINAL = PostTable.COLUMN_POST_UNIXTIMESTAMP_ORIGINAL;
        public static final String USER_ID = PostTable.COLUMN_USER_ID;
        public static final String IMAGE_FOLDER_NAME = PostTable.COLUMN_IMAGE_FOLDER_NAME;
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
//        checkColumns(projection);
        queryBuilder.setTables(Post.TABLE_NAME);

        switch (uriMatcher.match(uri)) {

            case POSTS:
                break;

            case POSTS_PER_USER_ID:
                queryBuilder.appendWhere(PostTable.COLUMN_USER_ID + "="
                        + uri.getLastPathSegment());
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: "+uri);
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
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri url, ContentValues contentValues) {

        long rowID=
                pichannelDbOpenHelper.getWritableDatabase().insert(Post.TABLE_NAME, null, contentValues);

        if (rowID > 0) {
            Uri insertedRowUri=
                    ContentUris.withAppendedId(PichannelContentProvider.Post.CONTENT_URI,
                            rowID);
            getContext().getContentResolver().notifyChange(insertedRowUri, null);

            return(insertedRowUri);
        }

        pichannelDbOpenHelper.getWritableDatabase()

//        INSERT INTO memos(id,text)
//        SELECT 5, 'text to insert'
//        WHERE NOT EXISTS(SELECT 1 FROM memos WHERE id = 5 AND text = 'text to insert');

        pichannelDbOpenHelper.getWritableDatabase().insertWithOnConflict()

        throw new SQLException("Failed to insert row into " + url);

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
