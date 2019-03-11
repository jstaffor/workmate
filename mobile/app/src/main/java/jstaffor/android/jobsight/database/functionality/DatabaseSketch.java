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
import jstaffor.android.jobsight.datamodel.viewdata.Sketch;

public class DatabaseSketch extends DatabaseAccess
{
    private static final String TAG = "DatabaseSketch";
    public DatabaseSketch(Context context) {
        super(context);
    }

    /**
     *
     * @param COLUMN_SIGNATURE_CHILD_ID
     * @param COLUMN_SIGNATURE_LOCATION
     * @return true if save was successful.
     */
    public boolean createSketchEntry(Long COLUMN_SIGNATURE_CHILD_ID, String COLUMN_SIGNATURE_LOCATION)
    {
        if(COLUMN_SIGNATURE_CHILD_ID == null || COLUMN_SIGNATURE_LOCATION == null)
            throw new IllegalArgumentException("DatabaseSketch.createSketchEntry(Long COLUMN_SIGNATURE_CHILD_ID, String COLUMN_SIGNATURE_LOCATION) - no inputs can be null");

        //Set default setting in case something goes wrong
        Long newRowGroupId = DataModel.DEFAULT_LONG_VALUE;

        try {
            // Open connection to database and insert the new row, returning the primary key value of the new row
            sqliteDatabase = databaseModelHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            values = new ContentValues();
            values.put(DatabaseModel.SKETCH.COLUMN_SKETCH_CHILD_ID, COLUMN_SIGNATURE_CHILD_ID);
            values.put(DatabaseModel.SKETCH.COLUMN_SKETCH_LOCATION, COLUMN_SIGNATURE_LOCATION);

            if(AppSettings.DATABASE_DEBUG_MODE) {
                Log.d(TAG, "createSketchEntry(Long COLUMN_SIGNATURE_CHILD_ID, String COLUMN_SIGNATURE_LOCATION) | Long COLUMN_SIGNATURE_CHILD_ID | " +COLUMN_SIGNATURE_CHILD_ID);
                Log.d(TAG, "createSketchEntry(Long COLUMN_SIGNATURE_CHILD_ID, String COLUMN_SIGNATURE_LOCATION) | Long COLUMN_SIGNATURE_LOCATION | " +COLUMN_SIGNATURE_LOCATION);
            }

            newRowGroupId = sqliteDatabase.insert(DatabaseModel.SKETCH.TABLE_NAME, null, values);
        }
        finally
        {
            if (AppSettings.DATABASE_DEBUG_MODE)
            {
                Log.d(TAG, "createSketchEntry() | finally | newRowGroupId = " +newRowGroupId);
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
     * @return List<Sketch>
     */
    public List<Sketch> getSketchDataFromDatabase(Long lChild)
    {
        if(lChild == null)
            throw new IllegalArgumentException("DatabaseSketch.getSketchDataFromDatabase(Long lChild) - lChild cannot be null");

        final List<Sketch> lViewDataSketch= new ArrayList<Sketch>();

        try {
            //Read the database for user ID
            sqliteDatabase = databaseModelHelper.getReadableDatabase();
            final String[] projection = {DatabaseModel.SKETCH.COLUMN_TIMESTAMP, DatabaseModel.SKETCH.COLUMN_SKETCH_LOCATION};

            // Filter results WHERE "title" = 'My Title'
            String selection = DatabaseModel.SKETCH.COLUMN_SKETCH_CHILD_ID + " = ?";
            String[] selectionArgs = {lChild.toString()};
            String sortOrder = DatabaseModel.SKETCH.COLUMN_TIMESTAMP + " ASC";

            if(AppSettings.DATABASE_DEBUG_MODE)
                Log.d(TAG, "getPhotoDataFromDatabase(Long lChild) | lChild | " + lChild);

            cursor = sqliteDatabase.query(
                    DatabaseModel.SKETCH.TABLE_NAME,     // The table to query
                    projection,                        // The array of columns to return (pass null to get all)
                    selection,                         // The columns for the WHERE clause
                    selectionArgs,                     // The values for the WHERE clause
                    null,                      // don't group the rows
                    null,                       // don't filter by row groups
                    sortOrder                          // The sort order
            );

            while (cursor.moveToNext())
            {
                lViewDataSketch.add( new Sketch(
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.SKETCH.COLUMN_TIMESTAMP)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.SKETCH.COLUMN_SKETCH_LOCATION))
                ));

                if(AppSettings.DATABASE_DEBUG_MODE) {
                    Log.d(TAG, "getSketchDataFromDatabase(Long lChild) | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.SKETCH.COLUMN_TIMESTAMP))| " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.SKETCH.COLUMN_TIMESTAMP)));
                    Log.d(TAG, "getSketchDataFromDatabase(Long lChild) | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.SKETCH.COLUMN_SKETCH_LOCATION))| " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.SKETCH.COLUMN_SKETCH_LOCATION)));
                }
            }
        }
        finally
        {
            if (AppSettings.DATABASE_DEBUG_MODE)
            {
                Log.d(TAG, "getSketchDataFromDatabase(Long lChild) | finally | lChild = " +lChild);
                listDatabaseValues();
            }

            closeDownDatabaseConnections();
        }

        return lViewDataSketch;
    }
}

