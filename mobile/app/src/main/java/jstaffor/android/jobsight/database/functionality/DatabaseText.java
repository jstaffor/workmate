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
import jstaffor.android.jobsight.datamodel.viewdata.Text;

public class DatabaseText extends DatabaseAccess
{
    private static final String TAG = "DatabaseText";
    public DatabaseText(Context context) {
        super(context);
    }

    /**
     * @param COLUMN_TEXT_CHILD_ID
     * @param COLUMN_TEXT_DATA
     * @return true if save was successful.
     */
    public boolean createTextEntry(Long COLUMN_TEXT_CHILD_ID, String COLUMN_TEXT_DATA)
    {
        if(COLUMN_TEXT_CHILD_ID == null || COLUMN_TEXT_DATA == null)
            throw new IllegalArgumentException("DatabaseText.createTextEntry(Long COLUMN_TEXT_CHILD_ID, String COLUMN_TEXT_DATA) - no inputs can be null");

        //Set default setting in case something goes wrong
        Long newRowGroupId = DataModel.DEFAULT_LONG_VALUE;

        try
        {
            // Open connection to database and insert the new row, returning the primary key value of the new row
            sqliteDatabase = databaseModelHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            values = new ContentValues();
            values.put(DatabaseModel.TEXT.COLUMN_TEXT_CHILD_ID, COLUMN_TEXT_CHILD_ID);
            values.put(DatabaseModel.TEXT.COLUMN_TEXT_DATA, COLUMN_TEXT_DATA);

            if(AppSettings.DATABASE_DEBUG_MODE) {
                Log.d(TAG, "createTextEntry(Long COLUMN_TEXT_CHILD_ID, String COLUMN_TEXT_DATA) | Long COLUMN_TEXT_CHILD_ID | " + COLUMN_TEXT_CHILD_ID);
                Log.d(TAG, "createTextEntry(Long COLUMN_TEXT_CHILD_ID, String COLUMN_TEXT_DATA) | Long COLUMN_TEXT_DATA | " + COLUMN_TEXT_DATA);
            }

            newRowGroupId = sqliteDatabase.insert(DatabaseModel.TEXT.TABLE_NAME, null, values);
        }
        finally
        {
            if (AppSettings.DATABASE_DEBUG_MODE)
            {
                Log.d(TAG, "createTextEntry() | finally | newRowGroupId = " +newRowGroupId);
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
     * @return List<Text>
     */
    public List<Text> getTextDataFromDatabase(Long lChild)
    {
        if(lChild == null)
            throw new IllegalArgumentException("DatabaseText.getTextDataFromDatabase(Long lChild) - lChild cannot be null");

        final List<Text> lViewDataText= new ArrayList<Text>();

        try {
            //Read the database for user ID
            sqliteDatabase = databaseModelHelper.getReadableDatabase();
            final String[] projection = {DatabaseModel.TEXT.COLUMN_TIMESTAMP, DatabaseModel.TEXT.COLUMN_TEXT_DATA};

            // Filter results WHERE "title" = 'My Title'
            String selection = DatabaseModel.TEXT.COLUMN_TEXT_CHILD_ID + " = ?";
            String[] selectionArgs = {lChild.toString()};
            String sortOrder = DatabaseModel.TEXT.COLUMN_TIMESTAMP + " ASC";

            if(AppSettings.DATABASE_DEBUG_MODE)
                Log.d(TAG, "getTextDataFromDatabase(Long lChild) | lChild | " + lChild);

            cursor = sqliteDatabase.query(
                    DatabaseModel.TEXT.TABLE_NAME,     // The table to query
                    projection,                        // The array of columns to return (pass null to get all)
                    selection,                         // The columns for the WHERE clause
                    selectionArgs,                     // The values for the WHERE clause
                    null,                      // don't group the rows
                    null,                       // don't filter by row groups
                    sortOrder                          // The sort order
            );

            while (cursor.moveToNext())
            {
                lViewDataText.add( new Text(
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.TEXT.COLUMN_TIMESTAMP)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.TEXT.COLUMN_TEXT_DATA))
                ));

                if(AppSettings.DATABASE_DEBUG_MODE) {
                    Log.d(TAG, "getTextDataFromDatabase(Long lChild) | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.TEXT.COLUMN_TIMESTAMP))| " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.TEXT.COLUMN_TIMESTAMP)));
                    Log.d(TAG, "getTextDataFromDatabase(Long lChild) | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.TEXT.COLUMN_TEXT_DATA))| " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.TEXT.COLUMN_TEXT_DATA)));
                }
            }
        }
        finally
        {
            if (AppSettings.DATABASE_DEBUG_MODE)
            {
                Log.d(TAG, "getTextDataFromDatabase(Long lChild) | finally | lChild = " +lChild);
                listDatabaseValues();
            }

            closeDownDatabaseConnections();
        }

        return lViewDataText;
    }
}
