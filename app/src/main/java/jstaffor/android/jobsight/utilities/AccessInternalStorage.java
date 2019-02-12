package jstaffor.android.jobsight.utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.UUID;

import jstaffor.android.jobsight.R;
import jstaffor.android.jobsight.appsettings.AppSettings;

public class AccessInternalStorage extends AccessStorage
{
    private static final String TAG = "AccessInternalStorage";

    public AccessInternalStorage(Context context) {
        super(context);
    }

    /**
     * @param bitmapImage
     * @return directory.getAbsolutePath() or null if something when wrong
     */
    public String saveBitmapToInternalStorage(Bitmap bitmapImage, Long lParent, Long lChild) throws NullPointerException {
        if (bitmapImage == null)
            throw new IllegalArgumentException("AccessInternalStorage.saveBitmapToInternalStorage(Bitmap bitmapImage) throws NullPointerException - bitmapImage cannot be null");

        String absolutePath = "-1";
        FileOutputStream fos = null;

        try
        {
            File tempFile = getImageFileWithRandomName(lParent, lChild);

            fos = new FileOutputStream(tempFile);

            if (bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos) == false) {
                Log.e(TAG, "saveBitmapToInternalStorage(Bitmap bitmapImage, Long lParent, Long lChild) | if (bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos) == false) | " +false);
                throw new IllegalArgumentException();
            }

            absolutePath = tempFile.getPath();

            if(AppSettings.DEBUG_MODE) {
                Log.d(TAG, "saveBitmapToInternalStorage(Bitmap bitmapImage, Long lParent, Long lChild) | lParent | " + lParent);
                Log.d(TAG, "saveBitmapToInternalStorage(Bitmap bitmapImage, Long lParent, Long lChild) | lChild | " + lChild);
                Log.d(TAG, "saveBitmapToInternalStorage(Bitmap bitmapImage, Long lParent, Long lChild) | bitmapImage.getAllocationByteCount() | " + bitmapImage.getAllocationByteCount());
                Log.d(TAG, "saveBitmapToInternalStorage(Bitmap bitmapImage, Long lParent, Long lChild) | getImageFileWithRandomName(lParent, lChild).getAbsolutePath() | " + tempFile.getAbsolutePath() );
                Log.d(TAG, "saveBitmapToInternalStorage(Bitmap bitmapImage, Long lParent, Long lChild) | tempFile.getPath() | " + absolutePath);
            }
        } catch (Exception e) {
            Log.e(TAG, "saveBitmapToInternalStorage(Bitmap bitmapImage, Long lParent, Long lChild) | catch (Exception e) | " +e.getMessage());
            absolutePath = null;
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
                Log.e(TAG, "saveBitmapToInternalStorage(Bitmap bitmapImage, Long lParent, Long lChild)  | fos.close(); | " +e.getMessage());
                absolutePath = null;
            }
        }

        return absolutePath;
    }

    /**
     * @param contentUri
     * @return
     */
    public String getRealPathFromURI(Uri contentUri) {
        if (contentUri == null)
            throw new IllegalArgumentException("AccessInternalStorage.getRealPathFromURI(Uri contentUri) - contentUri cannot be null");

        String result = null;

        String[] proj = {MediaStore.Video.Media.DATA};

        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);

        Cursor cursor = loader.loadInBackground();

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        result = cursor.getString(column_index);

        if (result == null) {
            Log.e(TAG, " getRealPathFromURI(Uri contentUri) | cursor.getString(column_index) | " + "null");
            throw new IllegalArgumentException();
        }

        cursor.close();

        if(AppSettings.DEBUG_MODE)
            Log.d(TAG, "getRealPathFromURI(Uri contentUri) | cursor.getString(column_index) | " +result);

        return result;
    }

    /**
     * https://developer.android.com/reference/android/support/v4/content/FileProvider
     * This come from 'res/xml/file_paths.xml'
     *
     * @return
     * @throws IOException
     */
    private File getImageFileWithRandomName(Long lParent, Long lChild) throws IOException {
        if (lParent == null || lChild == null)
            throw new IllegalArgumentException("AccessInternalStorage.getImageFileWithRandomName(Long lParent, Long lChild) throws IOException - inputs cannot be null");

        return new File(createChildDirForParentChild(lParent, lChild), UUID.randomUUID().toString() + context.getString(R.string.accesslocalstorage_png_format));
    }

    /**
     * https://developer.android.com/reference/android/support/v4/content/FileProvider
     * This come from 'res/xml/file_paths.xml'
     *
     * @return MyFile
     */
    public File getVideoFileWithRandomName(Long lParent, Long lChild) throws IOException {
        if (lParent == null || lChild == null)
            throw new IllegalArgumentException("AccessInternalStorage.getVideoFileWithRandomName(Long lParent, Long lChild) throws IOException - inputs cannot be null");

        return new File(createChildDirForParentChild(lParent, lChild), UUID.randomUUID().toString() + context.getString(R.string.accesslocalstorage_png_format));
    }

    /**
     * Checks directory for 'lParent'
     *
     * https://developer.android.com/reference/android/support/v4/content/FileProvider
     * This come from 'res/xml/file_paths.xml'
     *
     * @param lParent
     * @return 'MyFile.getAbsolutePath()' or null if dir doesn't exist
     * @throws IOException          if 'lParent' is not a dir
     * @throws NullPointerException
     */
    public String doesDirectoryForParentExist(Long lParent) throws IOException, NullPointerException
    {
        if (lParent == null)
            throw new IllegalArgumentException("AccessInternalStorage.doesDirectoryForParentExist(Long lParent) throws IOException, NullPointerException - input cannot be null");

        //(1) Must be set in conjunction with 'This come from 'res/xml/file_paths.xml'
        final File rootDir = new File(context.getFilesDir(), "appfiles");
        if (!rootDir.exists())
            return null;

        if (!rootDir.isDirectory()) {
            Log.e(TAG, "doesDirectoryForParentExist(Long lParent)  | if (!rootDir.isDirectory()) | " +"'rootDir' is not a directory");
            throw new IOException();
        }

        final File parentDir = new File(rootDir.getAbsolutePath() + File.separator + lParent + File.separator);
        if (!parentDir.exists())
            return null;

        if (!parentDir.isDirectory()) {
            Log.e(TAG, "doesDirectoryForParentExist(Long lParent)  | if (!parentDir.isDirectory()) | " +"'parentDir' is not a directory");
            throw new IOException();
        }

        if(AppSettings.DEBUG_MODE) {
            Log.d(TAG, "doesDirectoryForParentExist(Long lParent) | rootDir =new File(context.getFilesDir(), appfiles).getAbsolutePath() | " + rootDir.getAbsolutePath());
            Log.d(TAG, "doesDirectoryForParentExist(Long lParent) | rootDir.exists() | " +rootDir.exists() );
            Log.d(TAG, "doesDirectoryForParentExist(Long lParent) | rootDir.isDirectory() | " +rootDir.isDirectory());
            Log.d(TAG, "doesDirectoryForParentExist(Long lParent) | parentDir = new File(rootDir.getAbsolutePath() + / + lParent + / ).getAbsolutePath() | " + parentDir.getAbsolutePath());
            Log.d(TAG, "doesDirectoryForParentExist(Long lParent) | parentDir.exists() | " +parentDir.exists() );
            Log.d(TAG, "doesDirectoryForParentExist(Long lParent) | parentDir.isDirectory() | " +parentDir.isDirectory());
            Log.d(TAG, "doesDirectoryForParentExist(Long lParent) | parentDir.getAbsolutePath() | " +parentDir.getAbsolutePath());
        }

        return parentDir.getAbsolutePath();
    }

    /**
     * Checks directory for 'lParent'\'lChild'
     *
     * https://developer.android.com/reference/android/support/v4/content/FileProvider
     * This come from 'res/xml/file_paths.xml'
     *
     * @param lChild
     * @return 'MyFile.getAbsolutePath()' or null if dir doesn't exist
     * @throws IOException          if 'lChild' is not a dir
     * @throws NullPointerException
     */
    public String doesDirectoryForChildExist(Long lParent, Long lChild) throws IOException, NullPointerException
    {
        if (lChild == null)
            throw new IllegalArgumentException("AccessInternalStorage.doesDirectoryForChildExist(Long lChild) throws IOException, NullPointerException - input cannot be null");

        //(1) Must be set in conjunction with 'This come from 'res/xml/file_paths.xml'
        final File childDir = new File(doesDirectoryForParentExist(lParent) + File.separator + lChild + File.separator);
        if (!childDir.exists())
            return null;

        if (!childDir.isDirectory()) {
            Log.e(TAG, "doesDirectoryForChildExist(Long lParent)  | if (!childDir.isDirectory()) | " +"'childDir' is not a directory");
            throw new IOException();
        }

        if(AppSettings.DEBUG_MODE) {
            Log.d(TAG, "doesDirectoryForChildExist(Long lParent, Long lChild) | childDir = new File(doesDirectoryForParentExist(lParent) + / + lChild + / ).getAbsolutePath() | " + childDir.getAbsolutePath());
            Log.d(TAG, "doesDirectoryForChildExist(Long lParent, Long lChild) | childDir.exists() | " +childDir.exists() );
            Log.d(TAG, "doesDirectoryForChildExist(Long lParent, Long lChild) | childDir.isDirectory() | " +childDir.isDirectory());
            Log.d(TAG, "doesDirectoryForChildExist(Long lParent, Long lChild) | childDir.getAbsolutePath() | " +childDir.getAbsolutePath());
        }

        return childDir.getAbsolutePath();
    }

    /**
     * Creates a directory for 'lParent'\'lChild'
     *
     * https://developer.android.com/reference/android/support/v4/content/FileProvider
     * This come from 'res/xml/file_paths.xml'
     *
     * @param lChild
     * @return 'childDir.getAbsolutePath()'
     * @throws IOException
     */
    public String createChildDirForParentChild(Long lParent, Long lChild) throws IOException
    {
        boolean wasRootDirMkdirSuccessful = true;
        boolean wasParentDirMkdirSuccessful = true;
        boolean wasChildDirMkdirSuccessful = true;

        if (lParent == null || lChild == null)
            throw new IllegalArgumentException("AccessInternalStorage.createChildDirForParentChild(Long lParent, Long lChild) throws IOException - inputs cannot be null");

        //(1) Must be set in conjunction with 'This come from 'res/xml/file_paths.xml'
        final File rootDir = new File(context.getFilesDir(), "appfiles");
        if (!rootDir.exists()) {
            wasRootDirMkdirSuccessful = rootDir.mkdir();
            if (!wasRootDirMkdirSuccessful) {
                Log.e(TAG, "createChildDirForParentChild(Long lParent, Long lChild) | if( !rootDir.mkdir() ) | " +wasRootDirMkdirSuccessful);
                throw new IOException();
            }
        }

        final File parentDir = new File(rootDir.getAbsolutePath() + File.separator + lParent + File.separator);
        if (!parentDir.exists()) {
            wasParentDirMkdirSuccessful = parentDir.mkdir();
            if (!wasParentDirMkdirSuccessful) {
                Log.e(TAG, "createChildDirForParentChild(Long lParent, Long lChild) | if( !parentDir.mkdir() ) | " +wasParentDirMkdirSuccessful);
                throw new IOException();
            }
        }

        final File childDir = new File(parentDir.getAbsolutePath() + File.separator + lChild + File.separator);
        if (!childDir.exists()) {
            wasChildDirMkdirSuccessful = childDir.mkdir();
            if (!wasChildDirMkdirSuccessful) {
                Log.e(TAG, "createChildDirForParentChild(Long lParent, Long lChild) | if( !childDir.mkdir() ) | " +wasChildDirMkdirSuccessful);
                throw new IOException();
            }
        }

        if(AppSettings.DEBUG_MODE) {
            Log.d(TAG, "createChildDirForParentChild(Long lParent, Long lChild) | rootDir = new File(context.getFilesDir(), appfiles).getAbsolutePath() | " + rootDir.getAbsolutePath());
            Log.d(TAG, "createChildDirForParentChild(Long lParent, Long lChild) | rootDir.exists() | " +rootDir.exists() );
            Log.d(TAG, "createChildDirForParentChild(Long lParent, Long lChild) | rootDir.mkdir() | " +wasRootDirMkdirSuccessful );
            Log.d(TAG, "createChildDirForParentChild(Long lParent, Long lChild) | rootDir.getAbsolutePath() | " +rootDir.getAbsolutePath() );
            Log.d(TAG, "createChildDirForParentChild(Long lParent, Long lChild) | parentDir.exists() | " +parentDir.exists() );
            Log.d(TAG, "createChildDirForParentChild(Long lParent, Long lChild) | parentDir.mkdir() | " +wasParentDirMkdirSuccessful );
            Log.d(TAG, "createChildDirForParentChild(Long lParent, Long lChild) | parentDir.getAbsolutePath() | " +parentDir.getAbsolutePath() );
            Log.d(TAG, "createChildDirForParentChild(Long lParent, Long lChild) | childDir.exists() | " +childDir.exists() );
            Log.d(TAG, "createChildDirForParentChild(Long lParent, Long lChild) | childDir.mkdir() | " +wasChildDirMkdirSuccessful );
            Log.d(TAG, "createChildDirForParentChild(Long lParent, Long lChild) | childDir.getAbsolutePath() | " +childDir.getAbsolutePath() );
        }

        return childDir.getAbsolutePath();
    }
}