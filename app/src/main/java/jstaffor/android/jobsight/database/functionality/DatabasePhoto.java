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
import jstaffor.android.jobsight.datamodel.viewdata.Photo;

public class DatabasePhoto extends DatabaseAccess
{
    private static final String TAG = "DatabasePhoto";
    public DatabasePhoto(Context context) {
        super(context);
    }

    /**
     *
     * @param COLUMN_PHOTO_CHILD_ID
     * @param COLUMN_PHOTO_LOCATION
     * @return true if save was successful.
     */
    public boolean createPhotoEntry(Long COLUMN_PHOTO_CHILD_ID, String COLUMN_PHOTO_LOCATION)
    {
        if(COLUMN_PHOTO_CHILD_ID == null || COLUMN_PHOTO_LOCATION == null)
            throw new IllegalArgumentException("DatabasePhoto.createPhotoEntry(Long COLUMN_PHOTO_CHILD_ID, String COLUMN_PHOTO_LOCATION) - no inputs can be null");

        //Set default setting in case something goes wrong
        Long newRowGroupId = DataModel.DEFAULT_LONG_VALUE;

        try {
            // Open connection to database and insert the new row, returning the primary key value of the new row
            sqliteDatabase = databaseModelHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            values = new ContentValues();
            values.put(DatabaseModel.PHOTO.COLUMN_PHOTO_CHILD_ID, COLUMN_PHOTO_CHILD_ID);
            values.put(DatabaseModel.PHOTO.COLUMN_PHOTO_LOCATION, COLUMN_PHOTO_LOCATION);

            newRowGroupId = sqliteDatabase.insert(DatabaseModel.PHOTO.TABLE_NAME, null, values);

            if(AppSettings.DEBUG_MODE) {
                Log.d(TAG, "createPhotoEntry() | Long COLUMN_PHOTO_CHILD_ID | " +COLUMN_PHOTO_CHILD_ID);
                Log.d(TAG, "createPhotoEntry() | Long COLUMN_PHOTO_LOCATION | " +COLUMN_PHOTO_LOCATION);
                Log.d(TAG, "createPhotoEntry() | sqliteDatabase.insert(DatabaseModel.PHOTO.TABLE_NAME, null, values) | " +newRowGroupId);
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
     * @return List<Photo>
     */
    public List<Photo> getPhotoDataFromDatabase(Long lChild)
    {
        if(lChild == null)
            throw new IllegalArgumentException("DatabasePhoto.getPhotoDataFromDatabase(Long lChild) - lChild cannot be null");

        final List<Photo> lViewDataPhoto= new ArrayList<Photo>();

        try {
            //Read the database for user ID
            sqliteDatabase = databaseModelHelper.getReadableDatabase();
            final String[] projection = {DatabaseModel.PHOTO.COLUMN_TIMESTAMP, DatabaseModel.PHOTO.COLUMN_PHOTO_LOCATION};

            // Filter results WHERE "title" = 'My Title'
            String selection = DatabaseModel.PHOTO.COLUMN_PHOTO_CHILD_ID + " = ?";
            String[] selectionArgs = {lChild.toString()};
            String sortOrder = DatabaseModel.PHOTO.COLUMN_TIMESTAMP + " ASC";

            cursor = sqliteDatabase.query(
                    DatabaseModel.PHOTO.TABLE_NAME,     // The table to query
                    projection,                        // The array of columns to return (pass null to get all)
                    selection,                         // The columns for the WHERE clause
                    selectionArgs,                     // The values for the WHERE clause
                    null,                      // don't group the rows
                    null,                       // don't filter by row groups
                    sortOrder                          // The sort order
            );

            while (cursor.moveToNext())
            {
                lViewDataPhoto.add( new Photo(
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.PHOTO.COLUMN_TIMESTAMP)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.PHOTO.COLUMN_PHOTO_LOCATION))
                ));

                if(AppSettings.DEBUG_MODE) {
                    Log.d(TAG, "getPhotoDataFromDatabase() | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.PHOTO.COLUMN_TIMESTAMP))| " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.PHOTO.COLUMN_TIMESTAMP)));
                    Log.d(TAG, "getPhotoDataFromDatabase() | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.PHOTO.COLUMN_PHOTO_LOCATION))| " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.PHOTO.COLUMN_PHOTO_LOCATION)));
                }
            }
        }
        finally
        {
            closeDownDatabaseConnections();
        }

        return lViewDataPhoto;
    }
}
