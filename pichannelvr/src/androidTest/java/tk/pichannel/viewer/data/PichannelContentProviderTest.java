package tk.pichannel.viewer.data;

import android.content.ContentValues;
import android.database.Cursor;
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
public class PichannelContentProviderTest {
    @Before
    public void setUp() throws Exception {

        ContentValues cv = new ContentValues();
        cv.put(PostTable.COLUMN_ID,Stub.ID);
        cv.put(PostTable.COLUMN_POST_UNIXTIMESTAMP_ORIGINAL,Stub.POST_UNIXTIMESTAMP_ORIGINAL);
        cv.put(PostTable.COLUMN_USER_ID,Stub.USER_ID);
        cv.put(PostTable.COLUMN_IMAGE_FILE_NAME,Stub.IMAGE_FILE_NAME);
        cv.put(PostTable.COLUMN_IMAGE_FOLDER_NAME,Stub.IMAGE_FOLDER_NAME);
        cv.put(PostTable.COLUMN_TEXT,Stub.TEXT);

        InstrumentationRegistry.getTargetContext().getContentResolver().insert(
                PichannelContentProvider.Post.CONTENT_URI, cv);
    }

    @Test
    public void insertionOfPostTable() {

        String[] projection = {PostTable.COLUMN_IMAGE_FOLDER_NAME,PostTable.COLUMN_IMAGE_FILE_NAME};
        String selection = "id=? AND user_id=?";
        String[] selectionArgs = {Stub.ID,Stub.USER_ID};

        Cursor cursor = InstrumentationRegistry.getTargetContext().getContentResolver().query(
                PichannelContentProvider.Post.CONTENT_URI,projection,selection,selectionArgs,null);


        while (cursor.moveToNext()) {
            assertEquals(cursor.getString(0),Stub.IMAGE_FOLDER_NAME);
            assertEquals(cursor.getString(1),Stub.IMAGE_FILE_NAME);

        }
        cursor.close();
    }

    @After
    public void tearDown() throws Exception {

    }

}