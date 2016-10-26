package tk.pichannel.viewer.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.UserDictionary;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class PichannelDbOpenHelperTest {

    private PichannelDbOpenHelper pichannelDbOpenHelper;
    private SQLiteDatabase db;


    @Before
    public void setUp() throws Exception {

        pichannelDbOpenHelper = new PichannelDbOpenHelper(
                InstrumentationRegistry.getTargetContext(),
                PichannelDbOpenHelper.DATABASE_NAME,
                null,
                PichannelDbOpenHelper.SCHEMA_VERSION);

        db = pichannelDbOpenHelper.getWritableDatabase();



        ContentValues cv = new ContentValues();
        cv.put(PostTable.COLUMN_ID,Stub.ID);
        cv.put(PostTable.COLUMN_POST_UNIXTIMESTAMP_ORIGINAL,Stub.POST_UNIXTIMESTAMP_ORIGINAL);
        cv.put(PostTable.COLUMN_USER_ID,Stub.USER_ID);
        cv.put(PostTable.COLUMN_IMAGE_FILE_NAME,Stub.IMAGE_FILE_NAME);
        cv.put(PostTable.COLUMN_IMAGE_FOLDER_NAME,Stub.IMAGE_FOLDER_NAME);
        cv.put(PostTable.COLUMN_TEXT,Stub.TEXT);

        db.insert(PostTable.TABLE_NAME,null,cv);

    }

    @Test
    public void creationOfPichannelDatabase_isSuccess(){

        assertEquals(pichannelDbOpenHelper.getDatabaseName(),"pichannel.db");
    }

    @Test
    public void creationOfPostTable_isSuccess(){
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE name=?",new String[]{"post"});

        while (cursor.moveToNext()) {
            assertEquals(cursor.getString(0),"post");
        }
        cursor.close();
    }

    @Test
    public void insertionOfPostTable() {

        String table = PostTable.TABLE_NAME;
        String[] columns = new String[]{PostTable.COLUMN_IMAGE_FOLDER_NAME,PostTable.COLUMN_IMAGE_FILE_NAME};
        String selection = PostTable.COLUMN_ID + "=? AND "+ PostTable.COLUMN_USER_ID + "=?";
        String[] selectionArgs = new String[]{Stub.ID,Stub.USER_ID};

//        Cursor cursor = db.rawQuery("SELECT image_folder_name,image_file_name FROM post WHERE id=1 AND user_id='foo'",null);
        Cursor cursor = db.query(table, columns, selection, selectionArgs,null,null,null);

        int columnIndex_ImageFolderName = cursor.getColumnIndex(PostTable.COLUMN_IMAGE_FOLDER_NAME);
        int columnIndex_ImageFileName   = cursor.getColumnIndex(PostTable.COLUMN_IMAGE_FILE_NAME);

        cursor.moveToNext();

        assertEquals(cursor.getString(columnIndex_ImageFolderName), Stub.IMAGE_FOLDER_NAME);
        assertEquals(cursor.getString(columnIndex_ImageFileName)  , Stub.IMAGE_FILE_NAME);

        cursor.close();
    }

    @After
    public void tearDown() throws Exception {

        String table = PostTable.TABLE_NAME;
        String whereClause = PostTable.COLUMN_USER_ID +"=?";
        String[] whereArgs = new String[]{Stub.USER_ID};

        db.delete(table, whereClause, whereArgs);

        db.close();
    }

}