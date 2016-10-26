package tk.pichannel.viewer.data;

/**
 * Created by HamnLee on 2016/10/19.
 */

public class PostTable {
    public static final String TABLE_NAME = "post";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_POST_UNIXTIMESTAMP_ORIGINAL = "post_unixtimestamp_original";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_IMAGE_FOLDER_NAME = "image_folder_name";
    public static final String COLUMN_IMAGE_FILE_NAME = "image_file_name";
    public static final String COLUMN_TEXT = "text";

//    Version.1
//    static final String CREATE_TABLE_SQL="CREATE TABLE "+TABLE_NAME+" (" +
//            "    _ID INTEGER PRIMARY KEY," +
//            "    "+COLUMN_ID+" INTEGER, " +
//            "    "+COLUMN_POST_UNIXTIMESTAMP_ORIGINAL+" INTEGER," +
//            "    "+COLUMN_USER_ID+" TEXT," +
//            "    "+COLUMN_IMAGE_FOLDER_NAME+" TEXT," +
//            "    "+COLUMN_IMAGE_FILE_NAME+" TEXT," +
//            "    "+COLUMN_TEXT+" TEXT)";

//    Version.2
    static final String CREATE_TABLE_SQL="CREATE TABLE "+TABLE_NAME+" (" +
            "    "+COLUMN_ID+" INTEGER, " +
            "    "+COLUMN_POST_UNIXTIMESTAMP_ORIGINAL+" INTEGER," +
            "    "+COLUMN_USER_ID+" TEXT," +
            "    "+COLUMN_IMAGE_FOLDER_NAME+" TEXT," +
            "    "+COLUMN_IMAGE_FILE_NAME+" TEXT," +
            "    "+COLUMN_TEXT+" TEXT," +
            "    PRIMARY KEY ("+COLUMN_ID+","+COLUMN_USER_ID+")" +
            ")";

    static final String DELETE_TABLE_SQL =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
