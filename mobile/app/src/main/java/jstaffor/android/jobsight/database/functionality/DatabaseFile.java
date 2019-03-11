package jstaffor.android.jobsight.database.functionality;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.database.DatabaseAccess;
import jstaffor.android.jobsight.database.DatabaseModel;
import jstaffor.android.jobsight.datamodel.DataModel;
import jstaffor.android.jobsight.datamodel.viewdata.MyFile;

public class DatabaseFile extends DatabaseAccess
{
    private static final String TAG = "DatabaseFile";
    public DatabaseFile(Context context) {
        super(context);
    }

    /**
     *
     * @param COLUMN_FILE_RECORDING_CHILD_ID
     * @param COLUMN_FILE_RECORDING_LOCATION
     * @param COLUMN_FILE_NAME
     * @return true if save was successful.
     */
    public boolean createFileEntry(Long COLUMN_FILE_RECORDING_CHILD_ID, String COLUMN_FILE_RECORDING_LOCATION, String COLUMN_FILE_NAME)
    {
        if(COLUMN_FILE_RECORDING_CHILD_ID == null || COLUMN_FILE_RECORDING_LOCATION == null)
            throw new IllegalArgumentException("DatabaseFile.createFileEntry(Long COLUMN_FILE_RECORDING_CHILD_ID, String COLUMN_FILE_RECORDING_LOCATION) - no inputs can be null");

        //Set default setting in case something goes wrong
        Long newRowGroupId = DataModel.DEFAULT_LONG_VALUE;

        try {
            // Open connection to database and insert the new row, returning the primary key value of the new row
            sqliteDatabase = databaseModelHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            values = new ContentValues();
            values.put(DatabaseModel.FILE.COLUMN_FILE_RECORDING_CHILD_ID, COLUMN_FILE_RECORDING_CHILD_ID);
            values.put(DatabaseModel.FILE.COLUMN_FILE_RECORDING_LOCATION, COLUMN_FILE_RECORDING_LOCATION);
            values.put(DatabaseModel.FILE.COLUMN_FILE_NAME, COLUMN_FILE_NAME);

            if(AppSettings.DATABASE_DEBUG_MODE) {
                Log.d(TAG, "createFileEntry(Long COLUMN_FILE_RECORDING_CHILD_ID, String COLUMN_FILE_RECORDING_LOCATION, String COLUMN_FILE_NAME) | Long COLUMN_FILE_RECORDING_CHILD_ID | " + COLUMN_FILE_RECORDING_CHILD_ID);
                Log.d(TAG, "createFileEntry(Long COLUMN_FILE_RECORDING_CHILD_ID, String COLUMN_FILE_RECORDING_LOCATION, String COLUMN_FILE_NAME) | Long COLUMN_FILE_RECORDING_LOCATION | " + COLUMN_FILE_RECORDING_LOCATION);
                Log.d(TAG, "createFileEntry(Long COLUMN_FILE_RECORDING_CHILD_ID, String COLUMN_FILE_RECORDING_LOCATION, String COLUMN_FILE_NAME) | Long COLUMN_FILE_NAME | " + COLUMN_FILE_NAME);
            }

            newRowGroupId = sqliteDatabase.insert(DatabaseModel.FILE.TABLE_NAME, null, values);
        }
        finally
        {
            if (AppSettings.DATABASE_DEBUG_MODE)
            {
                Log.d(TAG, "createFileEntry() | finally | newRowGroupId = " +newRowGroupId);
                listDatabaseValues();
            }

            closeDownDatabaseConnections();
            if(newRowGroupId == -1L) //sqliteDatabase.insert @return the row ID of the newly inserted row, or -1 if an error occurred
                return false;
            else
                return true;
        }
    }

    /**
     * @param lChild
     * @return List<ViewDataVideo>
     */
    public List<MyFile> getFileDataFromDatabase(Long lChild)
    {
        if(lChild == null)
            throw new IllegalArgumentException("DatabaseVideoRecording.getFileDataFromDatabase(Long lChild) - lChild cannot be null");

        final List<MyFile> lViewDataMyFile = new ArrayList<MyFile>();

        try {
            //Read the database for user ID
            sqliteDatabase = databaseModelHelper.getReadableDatabase();
            final String[] projection = {DatabaseModel.FILE.COLUMN_TIMESTAMP, DatabaseModel.FILE.COLUMN_FILE_RECORDING_LOCATION, DatabaseModel.FILE.COLUMN_FILE_NAME};

            // Filter results WHERE "title" = 'My Title'
            String selection = DatabaseModel.FILE.COLUMN_FILE_RECORDING_CHILD_ID + " = ?";
            String[] selectionArgs = {lChild.toString()};
            String sortOrder = DatabaseModel.FILE.COLUMN_TIMESTAMP + " ASC";

            cursor = sqliteDatabase.query(
                    DatabaseModel.FILE.TABLE_NAME,   // The table to query
                    projection,                                 // The array of columns to return (pass null to get all)
                    selection,                                  // The columns for the WHERE clause
                    selectionArgs,                              // The values for the WHERE clause
                    null,                               // don't group the rows
                    null,                                // don't filter by row groups
                    sortOrder                                   // The sort order
            );

            while (cursor.moveToNext())
            {
                lViewDataMyFile.add( new MyFile(
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.FILE.COLUMN_TIMESTAMP)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.FILE.COLUMN_FILE_RECORDING_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.FILE.COLUMN_FILE_NAME))
                ));

                if(AppSettings.DATABASE_DEBUG_MODE) {
                    Log.d(TAG, "getFileDataFromDatabase(Long lChild) | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.FILE.COLUMN_TIMESTAMP))| " +cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.FILE.COLUMN_TIMESTAMP)));
                    Log.d(TAG, "getFileDataFromDatabase(Long lChild) | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.FILE.COLUMN_FILE_RECORDING_LOCATION))| " +cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.FILE.COLUMN_FILE_RECORDING_LOCATION)));
                    Log.d(TAG, "getFileDataFromDatabase(Long lChild) | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.FILE.COLUMN_FILE_NAME))| " +cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.FILE.COLUMN_FILE_NAME)));
                }
            }
        }
        finally
        {
            if (AppSettings.DATABASE_DEBUG_MODE)
            {
                Log.d(TAG, "getFileDataFromDatabase(Long lChild) | finally | lChild = " +lChild);
                listDatabaseValues();
            }

            closeDownDatabaseConnections();
        }

        return lViewDataMyFile;
    }
}
