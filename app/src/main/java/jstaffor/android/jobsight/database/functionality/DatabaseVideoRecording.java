package jstaffor.android.jobsight.database.functionality;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.database.DatabaseModel;
import jstaffor.android.jobsight.database.DatabaseAccess;
import jstaffor.android.jobsight.datamodel.DataModel;
import jstaffor.android.jobsight.datamodel.viewdata.VideoRecording;

public class DatabaseVideoRecording extends DatabaseAccess
{
    private static final String TAG = "DatabaseVideoRecording";
    public DatabaseVideoRecording(Context context) {
        super(context);
    }

    /**
     *
     * @param COLUMN_VIDEO_RECORDING_CHILD_ID
     * @param COLUMN_VIDEO_RECORDING_LOCATION
     * @param COLUMN_VIDEO_THUMNAIL_LOCATION
     * @return true if save was successful.
     */
    public boolean createVideoRecordingEntry(Long COLUMN_VIDEO_RECORDING_CHILD_ID, String COLUMN_VIDEO_RECORDING_LOCATION, String COLUMN_VIDEO_THUMNAIL_LOCATION)
    {
        if(COLUMN_VIDEO_RECORDING_CHILD_ID == null || COLUMN_VIDEO_RECORDING_LOCATION == null || COLUMN_VIDEO_THUMNAIL_LOCATION == null)
            throw new IllegalArgumentException("DatabaseVideoRecording.createVideoRecordingEntry(Long COLUMN_VIDEO_RECORDING_CHILD_ID, String COLUMN_VIDEO_RECORDING_LOCATION, String COLUMN_VIDEO_THUMNAIL_LOCATION)- no inputs can be null");

        //Set default setting in case something goes wrong
        Long newRowGroupId = DataModel.DEFAULT_LONG_VALUE;

        try {
            // Open connection to database and insert the new row, returning the primary key value of the new row
            sqliteDatabase = databaseModelHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            values = new ContentValues();
            values.put(DatabaseModel.VIDEO_RECORDING.COLUMN_VIDEO_RECORDING_CHILD_ID, COLUMN_VIDEO_RECORDING_CHILD_ID);
            values.put(DatabaseModel.VIDEO_RECORDING.COLUMN_VIDEO_RECORDING_LOCATION, COLUMN_VIDEO_RECORDING_LOCATION);
            values.put(DatabaseModel.VIDEO_RECORDING.COLUMN_VIDEO_THUMNAIL_LOCATION, COLUMN_VIDEO_THUMNAIL_LOCATION);

            newRowGroupId = sqliteDatabase.insert(DatabaseModel.VIDEO_RECORDING.TABLE_NAME, null, values);

            if(AppSettings.DEBUG_MODE) {
                Log.d(TAG, "createVideoRecordingEntry() | Long COLUMN_VIDEO_RECORDING_CHILD_ID | " +COLUMN_VIDEO_RECORDING_CHILD_ID);
                Log.d(TAG, "createVideoRecordingEntry() | Long COLUMN_VIDEO_RECORDING_LOCATION | " +COLUMN_VIDEO_RECORDING_LOCATION);
                Log.d(TAG, "createVideoRecordingEntry() | Long COLUMN_VIDEO_THUMNAIL_LOCATION | " +COLUMN_VIDEO_THUMNAIL_LOCATION);
                Log.d(TAG, "createVideoRecordingEntry() | sqliteDatabase.insert(DatabaseModel.VIDEO_RECORDING.TABLE_NAME, null, values) | " +newRowGroupId);
            }
        }
        finally
        {
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
    public List<VideoRecording> getVideoRecordingDataFromDatabase(Long lChild)
    {
        if(lChild == null)
            throw new IllegalArgumentException("DatabaseVideoRecording.getVideoRecordingDataFromDatabase(Long lChild) - lChild cannot be null");

        final List<VideoRecording> lViewDataVideoRecording= new ArrayList<VideoRecording>();

        try {
            //Read the database for user ID
            sqliteDatabase = databaseModelHelper.getReadableDatabase();
            final String[] projection = {DatabaseModel.VIDEO_RECORDING.COLUMN_TIMESTAMP, DatabaseModel.VIDEO_RECORDING.COLUMN_VIDEO_RECORDING_LOCATION, DatabaseModel.VIDEO_RECORDING.COLUMN_VIDEO_THUMNAIL_LOCATION};

            // Filter results WHERE "title" = 'My Title'
            String selection = DatabaseModel.VIDEO_RECORDING.COLUMN_VIDEO_RECORDING_CHILD_ID + " = ?";
            String[] selectionArgs = {lChild.toString()};
            String sortOrder = DatabaseModel.VIDEO_RECORDING.COLUMN_TIMESTAMP + " ASC";

            cursor = sqliteDatabase.query(
                    DatabaseModel.VIDEO_RECORDING.TABLE_NAME,   // The table to query
                    projection,                                 // The array of columns to return (pass null to get all)
                    selection,                                  // The columns for the WHERE clause
                    selectionArgs,                              // The values for the WHERE clause
                    null,                               // don't group the rows
                    null,                                // don't filter by row groups
                    sortOrder                                   // The sort order
            );

            while (cursor.moveToNext())
            {
                lViewDataVideoRecording.add( new VideoRecording(
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.VIDEO_RECORDING.COLUMN_TIMESTAMP)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.VIDEO_RECORDING.COLUMN_VIDEO_RECORDING_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.VIDEO_RECORDING.COLUMN_VIDEO_THUMNAIL_LOCATION))
                ));

                if(AppSettings.DEBUG_MODE) {
                    Log.d(TAG, "getVideoRecordingDataFromDatabase() | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.VIDEO_RECORDING.COLUMN_TIMESTAMP))| " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.VIDEO_RECORDING.COLUMN_TIMESTAMP)));
                    Log.d(TAG, "getVideoRecordingDataFromDatabase() | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.VIDEO_RECORDING.COLUMN_VIDEO_RECORDING_LOCATION))| " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.VIDEO_RECORDING.COLUMN_VIDEO_RECORDING_LOCATION)));
                    Log.d(TAG, "getVideoRecordingDataFromDatabase() | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.VIDEO_RECORDING.COLUMN_VIDEO_THUMNAIL_LOCATION))| " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.VIDEO_RECORDING.COLUMN_VIDEO_THUMNAIL_LOCATION)));
                }
            }
        }
        finally
        {
            closeDownDatabaseConnections();
        }

        return lViewDataVideoRecording;
    }
}
