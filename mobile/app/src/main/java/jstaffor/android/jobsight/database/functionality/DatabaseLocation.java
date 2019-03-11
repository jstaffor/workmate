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
import jstaffor.android.jobsight.datamodel.viewdata.Location;

public class DatabaseLocation extends DatabaseAccess
{
    private static final String TAG = "DatabaseLocation";
    public DatabaseLocation(Context context) {
        super(context);
    }

    /**
     * @param COLUMN_LOCATION_CHILD_ID
     * @param COLUMN_LOCATION_LATITUDE
     * @param COLUMN_LOCATION_LONGITUDE
     * @return true if save was successful.
     */
    public boolean createLocationEntry(Long COLUMN_LOCATION_CHILD_ID, String COLUMN_LOCATION_ADDRESS, String COLUMN_LOCATION_LATITUDE, String COLUMN_LOCATION_LONGITUDE, String COLUMN_IMAGE_LOCATION)
    {
        if(COLUMN_LOCATION_CHILD_ID == null  || COLUMN_LOCATION_ADDRESS == null || COLUMN_LOCATION_LATITUDE == null || COLUMN_LOCATION_LONGITUDE == null || COLUMN_IMAGE_LOCATION == null)
            throw new IllegalArgumentException(
                    "DatabaseLocation.createLocationEntry(Long COLUMN_LOCATION_CHILD_ID, String COLUMN_LOCATION_ADDRESS, String COLUMN_LOCATION_LATITUDE, String COLUMN_LOCATION_LONGITUDE, String COLUMN_IMAGE_LOCATION) - inputs can be null");

        //Set default setting in case something goes wrong
        Long newRowGroupId = DataModel.DEFAULT_LONG_VALUE;

        try
        {
            // Open connection to database and insert the new row, returning the primary key value of the new row
            sqliteDatabase = databaseModelHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            values = new ContentValues();
            values.put(DatabaseModel.LOCATION.COLUMN_LOCATION_CHILD_ID, COLUMN_LOCATION_CHILD_ID);
            values.put(DatabaseModel.LOCATION.COLUMN_LOCATION_LATITUDE, COLUMN_LOCATION_LATITUDE);
            values.put(DatabaseModel.LOCATION.COLUMN_LOCATION_LONGITUDE, COLUMN_LOCATION_LONGITUDE);
            values.put(DatabaseModel.LOCATION.COLUMN_LOCATION_ADDRESS, COLUMN_LOCATION_ADDRESS);
            values.put(DatabaseModel.LOCATION.COLUMN_IMAGE_LOCATION, COLUMN_IMAGE_LOCATION);

            if(AppSettings.DATABASE_DEBUG_MODE) {
                Log.d(TAG, "createLocationEntry() | Long COLUMN_LOCATION_CHILD_ID | " +COLUMN_LOCATION_CHILD_ID);
                Log.d(TAG, "createLocationEntry() | Long COLUMN_LOCATION_LATITUDE | " +COLUMN_LOCATION_LATITUDE);
                Log.d(TAG, "createLocationEntry() | Long COLUMN_LOCATION_LONGITUDE | " +COLUMN_LOCATION_LONGITUDE);
                Log.d(TAG, "createLocationEntry() | Long COLUMN_LOCATION_ADDRESS | " +COLUMN_LOCATION_ADDRESS);
                Log.d(TAG, "createLocationEntry() | Long COLUMN_IMAGE_LOCATION | " +COLUMN_IMAGE_LOCATION);
            }

            newRowGroupId = sqliteDatabase.insert(DatabaseModel.LOCATION.TABLE_NAME, null, values);
        }
        finally
        {
            if (AppSettings.DATABASE_DEBUG_MODE)
            {
                Log.d(TAG, "createLocationEntry() | finally | newRowGroupId = " +newRowGroupId);
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
     * @return List<Location>
     */
    public List<Location> getLocationDataFromDatabase(Long lChild)
    {
        if(lChild == null)
            throw new IllegalArgumentException("DatabaseLocation.getLocationDataFromDatabase(Long lChild) - lChild cannot be null");

        final List<Location> lViewDataLocation = new ArrayList<Location>();

        try
        {
            //Read the database for user ID
            sqliteDatabase = databaseModelHelper.getReadableDatabase();
            final String[] projection = {
                    DatabaseModel.LOCATION.COLUMN_TIMESTAMP, DatabaseModel.LOCATION.COLUMN_LOCATION_ADDRESS,
                    DatabaseModel.LOCATION.COLUMN_LOCATION_LATITUDE, DatabaseModel.LOCATION.COLUMN_LOCATION_LONGITUDE,
                    DatabaseModel.LOCATION.COLUMN_IMAGE_LOCATION};

            // Filter results WHERE "title" = 'My Title'
            String selection = DatabaseModel.LOCATION.COLUMN_LOCATION_CHILD_ID + " = ?";
            String[] selectionArgs = {lChild.toString()};
            String sortOrder = DatabaseModel.LOCATION.COLUMN_TIMESTAMP + " ASC";

            if(AppSettings.DATABASE_DEBUG_MODE)
                Log.d(TAG, "getLocationDataFromDatabase(Long lChild) | lChild | " + lChild);

            cursor = sqliteDatabase.query(
                    DatabaseModel.LOCATION.TABLE_NAME,   // The table to query
                    projection,                                 // The array of columns to return (pass null to get all)
                    selection,                                  // The columns for the WHERE clause
                    selectionArgs,                              // The values for the WHERE clause
                    null,                               // don't group the rows
                    null,                                // don't filter by row groups
                    sortOrder                                   // The sort order
            );

            while (cursor.moveToNext())
            {
                lViewDataLocation.add( new Location(
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.LOCATION.COLUMN_TIMESTAMP)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.LOCATION.COLUMN_LOCATION_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.LOCATION.COLUMN_LOCATION_LATITUDE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.LOCATION.COLUMN_LOCATION_LONGITUDE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.LOCATION.COLUMN_IMAGE_LOCATION))
                ));

                if(AppSettings.DATABASE_DEBUG_MODE) {
                    Log.d(TAG, "getLocationDataFromDatabase(Long lChild)  | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.LOCATION.COLUMN_TIMESTAMP))| " +cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.LOCATION.COLUMN_TIMESTAMP)));
                    Log.d(TAG, "getLocationDataFromDatabase(Long lChild)  | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.LOCATION.COLUMN_LOCATION_ADDRESS))| " +cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.LOCATION.COLUMN_LOCATION_ADDRESS)));
                    Log.d(TAG, "getLocationDataFromDatabase(Long lChild) | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.LOCATION.COLUMN_LOCATION_LATITUDE))| " +cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.LOCATION.COLUMN_LOCATION_LATITUDE)));
                    Log.d(TAG, "getLocationDataFromDatabase(Long lChild)  | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.LOCATION.COLUMN_LOCATION_LONGITUDE))| " +cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.LOCATION.COLUMN_LOCATION_LONGITUDE)));
                    Log.d(TAG, "getLocationDataFromDatabase(Long lChild) | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.LOCATION.COLUMN_IMAGE_LOCATION))| " +cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.LOCATION.COLUMN_IMAGE_LOCATION)));
                }
            }
        }
        finally
        {
            if (AppSettings.DATABASE_DEBUG_MODE)
            {
                Log.d(TAG, "getLocationDataFromDatabase(Long lChild) | finally | lChild = " +lChild);
                listDatabaseValues();
            }

            closeDownDatabaseConnections();
        }

        return lViewDataLocation;
    }
}
