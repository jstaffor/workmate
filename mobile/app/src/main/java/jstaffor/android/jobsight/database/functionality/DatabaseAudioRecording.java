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
import jstaffor.android.jobsight.datamodel.utilities.DataModelUtilities;
import jstaffor.android.jobsight.datamodel.viewdata.AudioRecording;

public class DatabaseAudioRecording extends DatabaseAccess
{
    private static final String TAG = "DatabaseAudioRecording";
    public DatabaseAudioRecording(Context context) {
        super(context);
    }

    /**
     *
     * @param COLUMN_AUDIO_RECORDING_CHILD_ID
     * @param COLUMN_AUDIO_RECORDING_LOCATION
     * @return true if save was successful.
     */
    public boolean createAudioRecordingEntry(Long COLUMN_AUDIO_RECORDING_CHILD_ID, String COLUMN_AUDIO_RECORDING_LOCATION, String COLUMN_AUDIO_RECORDING_IMAGE_LOCATION)
    {
        if(COLUMN_AUDIO_RECORDING_CHILD_ID == null || COLUMN_AUDIO_RECORDING_LOCATION == null || COLUMN_AUDIO_RECORDING_IMAGE_LOCATION == null)
            throw new IllegalArgumentException("DatabaseAudioRecording.createAudioRecordingEntry(Long COLUMN_AUDIO_RECORDING_CHILD_ID, String COLUMN_AUDIO_RECORDING_LOCATION, String COLUMN_AUDIO_RECORDING_IMAGE_LOCATION) - no inputs can be null");

        //Set default setting in case something goes wrong
        Long newRowGroupId = DataModel.DEFAULT_LONG_VALUE;

        try {
            // Open connection to database and insert the new row, returning the primary key value of the new row
            sqliteDatabase = databaseModelHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            values = new ContentValues();
            values.put(DatabaseModel.AUDIO_RECORDING.COLUMN_AUDIO_RECORDING_CHILD_ID, COLUMN_AUDIO_RECORDING_CHILD_ID);
            values.put(DatabaseModel.AUDIO_RECORDING.COLUMN_AUDIO_RECORDING_LOCATION, COLUMN_AUDIO_RECORDING_LOCATION);
            values.put(DatabaseModel.AUDIO_RECORDING.COLUMN_AUDIO_RECORDING_IMAGE_LOCATION, COLUMN_AUDIO_RECORDING_IMAGE_LOCATION);

            newRowGroupId = sqliteDatabase.insert(DatabaseModel.AUDIO_RECORDING.TABLE_NAME, null, values);

            if(AppSettings.DEBUG_MODE) {
                Log.d(TAG, "createAudioRecordingEntry() | Long COLUMN_AUDIO_RECORDING_CHILD_ID | " +COLUMN_AUDIO_RECORDING_CHILD_ID);
                Log.d(TAG, "createAudioRecordingEntry() | Long COLUMN_AUDIO_RECORDING_LOCATION | " +COLUMN_AUDIO_RECORDING_LOCATION);
                Log.d(TAG, "createAudioRecordingEntry() | Long COLUMN_AUDIO_RECORDING_IMAGE_LOCATION | " +COLUMN_AUDIO_RECORDING_IMAGE_LOCATION);
                Log.d(TAG, "createAudioRecordingEntry() | sqliteDatabase.insert(DatabaseModel.AUDIO_RECORDING.TABLE_NAME, null, values) | " +newRowGroupId);
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
     * @return List<AudioRecording>
     */
    public List<AudioRecording> getAudioRecordingDataFromDatabase(Long lChild)
    {
        if(lChild == null)
            throw new IllegalArgumentException("DatabaseAudioRecording.getAudioRecordingDataFromDatabase(Long lChild) - lChild cannot be null");

        final List<AudioRecording> lViewDataAudioRecording = new ArrayList<AudioRecording>();

        try
        {
            sqliteDatabase = databaseModelHelper.getReadableDatabase();
            final String[] projection = {
                    DatabaseModel.AUDIO_RECORDING.COLUMN_TIMESTAMP, DatabaseModel.AUDIO_RECORDING.COLUMN_AUDIO_RECORDING_LOCATION, DatabaseModel.AUDIO_RECORDING.COLUMN_AUDIO_RECORDING_IMAGE_LOCATION};

            // Filter results WHERE "title" = 'My Title'
            String selection = DatabaseModel.AUDIO_RECORDING.COLUMN_AUDIO_RECORDING_CHILD_ID + " = ?";
            String[] selectionArgs = {lChild.toString()};
            String sortOrder = DatabaseModel.AUDIO_RECORDING.COLUMN_TIMESTAMP + " ASC";

            cursor = sqliteDatabase.query(
                    DatabaseModel.AUDIO_RECORDING.TABLE_NAME,     // The table to query
                    projection,                        // The array of columns to return (pass null to get all)
                    selection,                         // The columns for the WHERE clause
                    selectionArgs,                     // The values for the WHERE clause
                    null,                      // don't group the rows
                    null,                       // don't filter by row groups
                    sortOrder                          // The sort order
            );

            while (cursor.moveToNext())
            {
                lViewDataAudioRecording.add( new AudioRecording(
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.AUDIO_RECORDING.COLUMN_TIMESTAMP)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.AUDIO_RECORDING.COLUMN_AUDIO_RECORDING_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.AUDIO_RECORDING.COLUMN_AUDIO_RECORDING_IMAGE_LOCATION))
                ));

                if(AppSettings.DEBUG_MODE) {
                    Log.d(TAG, "getAudioRecordingDataFromDatabase() | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.AUDIO_RECORDING.COLUMN_TIMESTAMP))| " +cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.AUDIO_RECORDING.COLUMN_TIMESTAMP)));
                    Log.d(TAG, "getAudioRecordingDataFromDatabase() | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.AUDIO_RECORDING.COLUMN_AUDIO_RECORDING_LOCATION))| " +cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.AUDIO_RECORDING.COLUMN_AUDIO_RECORDING_LOCATION)));
                    Log.d(TAG, "getAudioRecordingDataFromDatabase() | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.AUDIO_RECORDING.COLUMN_AUDIO_RECORDING_IMAGE_LOCATION))| " +cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.AUDIO_RECORDING.COLUMN_AUDIO_RECORDING_IMAGE_LOCATION)));
                }
            }
        }
        finally
        {
            closeDownDatabaseConnections();
        }

        return lViewDataAudioRecording;
    }
}

