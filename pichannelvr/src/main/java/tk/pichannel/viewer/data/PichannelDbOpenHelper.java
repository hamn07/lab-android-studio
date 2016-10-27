package tk.pichannel.viewer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.StrictMode;
import android.util.Log;

/**
 * Created by HamnLee on 2016/10/17.
 */

public class PichannelDbOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="pichannel.db";
    public static final int SCHEMA_VERSION =5;
    private final String TAG = PichannelDbOpenHelper.class.getName();

    public PichannelDbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(PostTable.CREATE_TABLE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(PostTable.DELETE_TABLE_SQL);
        onCreate(sqLiteDatabase);
    }
}
