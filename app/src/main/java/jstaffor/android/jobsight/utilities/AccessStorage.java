package jstaffor.android.jobsight.utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.UUID;

import jstaffor.android.jobsight.R;
import jstaffor.android.jobsight.appsettings.AppSettings;

public class AccessStorage
{
    private static final String TAG = "AccessInternalStorage";
    protected Context context;

    public AccessStorage(Context context) {
        if (context == null)
            throw new IllegalArgumentException("AccessInternalStorage.AccessInternalStorage(Context context) - context cannot be null");

        this.context = context;
    }

    /**
     * Copy myFile x to y
     *
     * @param x
     * @param y
     * @return y.getAbsolutePath()
     */
    public String copyFileFrom_x_to_y_(File x, File y)
    {
        if (x == null || y == null)
            throw new IllegalArgumentException("AccessInternalStorage.copyFileFrom_x_to_y_(MyFile x, MyFile y) throws IOException - inputs cannot be null");

        String absolutePath = null;
        FileChannel fileChannelX = null;
        FileChannel fileChannelY = null;

        try
        {
            y.createNewFile();  //Create an empty file
            fileChannelX = new FileInputStream(x).getChannel();
            fileChannelY = new FileOutputStream(y).getChannel();
            fileChannelX.transferTo(0, fileChannelX.size(), fileChannelY);

            absolutePath = y.getAbsolutePath();

            if(AppSettings.DEBUG_MODE) {
                Log.d(TAG, "copyFileFrom_x_to_y_(File x, File y) | FileChannel fileChannelX.size() | " + fileChannelX.size());
                Log.d(TAG, "copyFileFrom_x_to_y_(File x, File y) | FileChannel fileChannelY.size() | " + fileChannelY.size());
                Log.d(TAG, "copyFileFrom_x_to_y_(File x, File y) | y.getAbsolutePath() | " +absolutePath);
            }
        }
        catch (Exception exception)
        {
            Log.e(TAG, "copyFileFrom_x_to_y_(File x, File y)  | catch (Exception exception) | " +exception.getMessage());
            absolutePath = null;
        }
        finally
        {
            try
            {
                if (fileChannelX != null)
                    fileChannelX.close();

            } catch (Exception exception) {
                Log.e(TAG, "copyFileFrom_x_to_y_(File x, File y)  | fileChannelX.close(); | " +exception.getMessage());
            }

            try
            {
                if (fileChannelY != null)
                    fileChannelY.close();

            } catch (Exception exception) {
                Log.e(TAG, "copyFileFrom_x_to_y_(File x, File y)  | fileChannelY.close(); | " +exception.getMessage());
            }
        }

        return absolutePath;
    }

    /**
     * Deletes everything
     * If 'absolutePathToFileOrDir' is a dir, deletes everything located in the dir and the dir itself
     * If 'absolutePathToFileOrDir' is a myFile, deletes files
     *
     * @param absolutePathToFileOrDir
     * @return
     */
    public boolean deleteFileOrDir(String absolutePathToFileOrDir) {
        if (absolutePathToFileOrDir == null)
            throw new IllegalArgumentException("AccessInternalStorage.deleteFileOrDir(String absolutePathToFileOrDir) - input cannot be null");

        if (absolutePathToFileOrDir.equals(context.getFilesDir()))
            throw new IllegalArgumentException("AccessInternalStorage.deleteFileOrDir(String absolutePathToFileOrDir) - cannot delete root folder");

        boolean absolutePathToFileOrDirIsDeleted = false;

        try {
            final File tempFile = new File(absolutePathToFileOrDir);
            final boolean isDirectory = tempFile.isDirectory();

            if (isDirectory) {
                for (File child : tempFile.listFiles())
                {
                    final boolean wasDeletionSussessful = deleteFileOrDir(child.getAbsolutePath());

                    if(AppSettings.DEBUG_MODE) {
                        Log.d(TAG, "deleteFileOrDir(String absolutePathToFileOrDir) | deleteFileOrDir(child.getAbsolutePath())  child.getAbsolutePath() | " + child.getAbsolutePath());
                        Log.d(TAG, "deleteFileOrDir(String absolutePathToFileOrDir) | deleteFileOrDir(child.getAbsolutePath()) | " + wasDeletionSussessful);
                    }

                    if (!wasDeletionSussessful)
                        return false;
                }
            }

            final boolean wasDeletionSussessful = tempFile.delete();

            if(AppSettings.DEBUG_MODE) {
                Log.d(TAG, "deleteFileOrDir(String absolutePathToFileOrDir) | tempFile = new File(absolutePathToFileOrDir); | " +absolutePathToFileOrDir);
                Log.d(TAG, "deleteFileOrDir(String absolutePathToFileOrDir) | tempFile.delete(); | " + wasDeletionSussessful);
            }

            if (!isDirectory)
                MediaScannerConnection.scanFile(context, new String[]{absolutePathToFileOrDir}, null, null);

            absolutePathToFileOrDirIsDeleted = true;

        } catch (Exception exception) {
            Log.e(TAG, "deleteFileOrDir(String absolutePathToFileOrDir) | catch (Exception exception)| " +exception.getMessage());
            absolutePathToFileOrDirIsDeleted = false;
        }

        return absolutePathToFileOrDirIsDeleted;
    }

    /**
     *
     * @param uri
     * @return fileName, or null if there was an issue
     */
    public String getFileNameFromUri(Uri uri)
    {
        if (uri == null)
            throw new IllegalArgumentException("AccessInternalStorage.getFileNameFromUri(Uri uri) - input cannot be null");

        String fileName = null;

        if (uri.getScheme().equals("content"))
        {
            final Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null)
            {
                try
                {
                    if (cursor.moveToFirst())
                    {
                        fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                }
                catch (Exception exception)
                {
                    fileName = null;
                }
                finally
                {
                    cursor.close();
                }
            }
        }

        if(fileName == null)
        {
            fileName = uri.getPath();

            int cut = fileName.lastIndexOf('/');

            if (cut != -1)
            {
                fileName = fileName.substring(cut + 1);
            }
        }

        if(AppSettings.DEBUG_MODE)
            Log.d(TAG, "getFileNameFromUri(Uri uri) | fileName | " +fileName);

        return fileName;
    }

    /**
     *
     * @param uri
     * @return myFile extension, or null if there was an issue
     */
    public String getMimeTypeUsingUri(Uri uri)
    {
        if (uri == null)
            throw new IllegalArgumentException("AccessInternalStorage.getMimeTypeUsingUri(Uri uri) - input cannot be null");

        String extension = null;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT))
        {
            final MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            extension = mimeTypeMap.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        }
        else
        {
            extension = MimeTypeMap.getFileExtensionFromUrl( uri.getPath() );
        }

        if(extension != null)
            if( !extension.startsWith(".") )
                extension = "." + extension;

        if(AppSettings.DEBUG_MODE)
            Log.d(TAG, "getMimeTypeUsingUri(Uri uri) | extension | " +extension);

        return extension;
    }
}