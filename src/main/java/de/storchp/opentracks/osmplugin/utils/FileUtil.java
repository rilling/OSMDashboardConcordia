package de.storchp.opentracks.osmplugin.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();

   private FileUtil() {
       throw new UnsupportedOperationException("Utility class");
   }
    public static DocumentFile getDocumentFileFromTreeUri(Context context, Uri uri) {
        try {
            return DocumentFile.fromTreeUri(context, uri);
        } catch (Exception e) {
            Log.w(TAG, "Error getting DocumentFile from Uri: " + uri);
        }
        return null;
    }

    public void FileUtil()
    {
    }
}
