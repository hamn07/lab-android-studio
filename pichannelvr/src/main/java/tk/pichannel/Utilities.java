/**
 * Created by HamnLee on 2016/9/26.
 */
package tk.pichannel;

import android.content.Context;

import java.io.File;

public class Utilities {

    static final int SUB_DIRECTORY_NAME_CHAR_LENGTH = 2;

    public static String getImageFileNameAsStringByURL(String urlString) {
        return urlString.substring(urlString.lastIndexOf('/') + 1);
    }
    public static String getImageFolderNameAsStringByURL(String urlString){

        int array_index_of_last_slash = urlString.lastIndexOf('/');

        int array_index_of_sub_directory_name_end = array_index_of_last_slash;
        int array_index_of_sub_directory_name_start = array_index_of_sub_directory_name_end - SUB_DIRECTORY_NAME_CHAR_LENGTH;

        return urlString.substring(array_index_of_sub_directory_name_start,array_index_of_sub_directory_name_end);
    }
    public static boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }
}
