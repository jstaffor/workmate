package jstaffor.android.jobsight.utilities;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import jstaffor.android.jobsight.appsettings.AppSettings;

public class AccessExternalStorage extends AccessStorage {

    private static final String TAG = "AccessExternalStorage";

    public AccessExternalStorage(Context context) {
        super(context);
    }

    /**
     * Returns the 'state' of Environment.getExternalStorageState()
     *
     * @return
     */
    public boolean isExternalStorageWritable() {
        boolean isExternalStorageWritable = false;

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state))
            isExternalStorageWritable = true;

        if (AppSettings.APP_DEBUG_MODE)
            Log.d(TAG, "isExternalStorageWritable | if (Environment.MEDIA_MOUNTED.equals(state)) | " + isExternalStorageWritable);

        return isExternalStorageWritable;
    }

    /**
     * Returns the 'state' of Environment.getExternalStorageState()
     *
     * @return
     */
    public boolean isExternalStorageReadable() {
        boolean isExternalStorageReadable = false;

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
            isExternalStorageReadable = true;

        if (AppSettings.APP_DEBUG_MODE)
            Log.d(TAG, "isExternalStorageReadable | if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) | " + isExternalStorageReadable);

        return isExternalStorageReadable;
    }

    /**
     * Attempts to create a File for dir Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), sChild)
     *
     * @param rootFileDir This will be the name of the root folder
     * @return File representing the root folder for saving data, else throws IOException
     * @throws IOException returned if 'externalStoragePublicDirectoryFile.mkdirs()' was true
     */
    public File getExternalStorageDir(String rootFileDir) throws IOException {
        if (rootFileDir == null)
            throw new IllegalArgumentException("AccessExternalStorage.getExternalStorageDir(String rootFileDir) - rootFileDir cannot be null");

        File externalStoragePublicDirectoryFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), rootFileDir);

        final boolean isExternalStorageDirAccessibleToWorkWith = externalStoragePublicDirectoryFile.mkdirs();

        if (AppSettings.APP_DEBUG_MODE) {
            Log.d(TAG, "getExternalStorageDir(String rootFileDir) | rootFileDir | " + rootFileDir);
            Log.d(TAG, "getExternalStorageDir(String rootFileDir) | boolean isExternalStorageDirAccessibleToWorkWith = externalStoragePublicDirectoryFile.mkdirs() | " + isExternalStorageDirAccessibleToWorkWith);
        }

        if (!isExternalStorageDirAccessibleToWorkWith) {
            Log.d(TAG, "getExternalStorageDir(String rootFileDir) | rootFileDir | " + rootFileDir);
            Log.d(TAG, "getExternalStorageDir(String rootFileDir) | boolean isExternalStorageDirAccessibleToWorkWith = externalStoragePublicDirectoryFile.mkdirs() | " + rootFileDir);
            throw new IOException("getExternalStorageDir(String rootFileDir) | boolean isExternalStorageDirAccessibleToWorkWith = externalStoragePublicDirectoryFile.mkdirs() | " + isExternalStorageDirAccessibleToWorkWith);
        }

        return externalStoragePublicDirectoryFile;
    }


    public Boolean createChildGeneric(File rootDir, String nameOfFolder, String fullNameOfNewFile, String currentFileToCopy) throws IOException
    {
        boolean wasRootDirMkdirSuccessful = true;
        boolean wasChildDirMkdirSuccessful = true;

        if (rootDir == null || nameOfFolder == null || fullNameOfNewFile == null || currentFileToCopy == null)
            throw new IllegalArgumentException("AccessExternalStorage.createChildGeneric(File rootDir, String nameOfFolder, String fullNameOfNewFile, File currentFileToCopy)- inputs cannot be null");

        if (!rootDir.exists()) {
            wasRootDirMkdirSuccessful = rootDir.mkdir();
            if (!wasRootDirMkdirSuccessful) {
                Log.e(TAG, "createChildGeneric(File rootDir, String nameOfFolder, String fullNameOfNewFile, File currentFileToCopy) | wasRootDirMkdirSuccessful = rootDir.mkdir(); | " +wasRootDirMkdirSuccessful);
                throw new IOException();
            }
        }

        final File childDir = new File(rootDir.getAbsolutePath() + File.separator + nameOfFolder + File.separator);
        if (!childDir.exists()) {
            wasChildDirMkdirSuccessful = childDir.mkdir();
            if (!wasChildDirMkdirSuccessful) {
                Log.e(TAG, "createChildGeneric(File rootDir, String nameOfFolder, String fullNameOfNewFile, File currentFileToCopy)| wasChildDirMkdirSuccessful = childDir.mkdir(); | " +wasChildDirMkdirSuccessful);
                throw new IOException();
            }
        }

        final File newFile = new File(childDir, fullNameOfNewFile);

        if(wasRootDirMkdirSuccessful && wasChildDirMkdirSuccessful)
        {
            if(null != copyFileFrom_x_to_y_(new File(currentFileToCopy), newFile))
                return true;
            else
                return false;
        }
        else
        {
            return false;
        }
    }

    public Boolean createChildText(File rootDir, String nameOfFolder, String fullNameOfNewFile, String textToWrite) throws IOException
    {
        boolean wasRootDirMkdirSuccessful = true;
        boolean wasChildDirMkdirSuccessful = true;

        if (rootDir == null || nameOfFolder == null || fullNameOfNewFile == null || textToWrite == null)
            throw new IllegalArgumentException("AccessExternalStorage.createChildText(File rootDir, String nameOfFolder, String fullNameOfNewFile, String textToWrite) throws IOException - inputs cannot be null");

        if (!rootDir.exists()) {
            wasRootDirMkdirSuccessful = rootDir.mkdir();
            if (!wasRootDirMkdirSuccessful) {
                Log.e(TAG, "createChildText(File rootDir, String nameOfFolder, String fullNameOfNewFile, String textToWrite) throws IOException | wasRootDirMkdirSuccessful = rootDir.mkdir(); | " +wasRootDirMkdirSuccessful);
                throw new IOException();
            }
        }

        final File childDir = new File(rootDir.getAbsolutePath() + File.separator + nameOfFolder + File.separator);
        if (!childDir.exists()) {
            wasChildDirMkdirSuccessful = childDir.mkdir();
            if (!wasChildDirMkdirSuccessful) {
                Log.e(TAG, "createChildText(File rootDir, String nameOfFolder, String fullNameOfNewFile, String textToWrite) throws IOException | wasChildDirMkdirSuccessful = childDir.mkdir(); | " +wasChildDirMkdirSuccessful);
                throw new IOException();
            }
        }

        final File newFile = new File(childDir, fullNameOfNewFile);
        newFile.setWritable(true);

        FileOutputStream fileOutputStream = null;
        try
        {
            fileOutputStream = new FileOutputStream(newFile);

            fileOutputStream.write(textToWrite.getBytes());

            return true;
        }
        catch (IOException ioException) {
            Log.e(TAG, "createChildText(File rootDir, String nameOfFolder, String fullNameOfNewFile, String textToWrite) throws IOException | catch (IOException ioException) | " +ioException.getMessage());
            Log.e(TAG, "createChildText(File rootDir, String nameOfFolder, String fullNameOfNewFile, String textToWrite) throws IOException | fileOutputStream.write(textToWrite.getBytes()); fileToWriteTextTo | " +newFile.getAbsolutePath());
            Log.e(TAG, "createChildText(File rootDir, String nameOfFolder, String fullNameOfNewFile, String textToWrite) throws IOException | fileOutputStream.write(textToWrite.getBytes()); testToWrite | " +textToWrite);
            throw new IOException();
        }
        catch (Exception exception) {
            Log.e(TAG, "createChildText(File rootDir, String nameOfFolder, String fullNameOfNewFile, String textToWrite) throws IOException | catch (Exception exception) | " +exception.getMessage());
            Log.e(TAG, "createChildText(File rootDir, String nameOfFolder, String fullNameOfNewFile, String textToWrite) throws IOException | fileOutputStream.write(textToWrite.getBytes()); fileToWriteTextTo | " +newFile.getAbsolutePath());
            Log.e(TAG, "createChildText(File rootDir, String nameOfFolder, String fullNameOfNewFile, String textToWrite) throws IOException | fileOutputStream.write(textToWrite.getBytes()); testToWrite | " +textToWrite);
            throw new IOException();
        }
        finally {
            try
            {
                if(fileOutputStream != null)
                    fileOutputStream.close();
            }
            catch (Exception exception) {
                Log.e(TAG, "createChildText(File rootDir, String nameOfFolder, String fullNameOfNewFile, String textToWrite) throws IOException | catch (Exception exception) | " +exception.getMessage());
                throw new IOException();
            }
        }
    }
}
