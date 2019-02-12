package jstaffor.android.jobsight.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.datamodel.DataModel;

public class DatabaseAccess
{
    private static final String TAG = "DatabaseAccess";
    protected DatabaseModelHelper databaseModelHelper;
    protected SQLiteDatabase sqliteDatabase;
    protected ContentValues values;
    protected Cursor cursor;

    public DatabaseAccess(Context context)
    {
        super();

        if(context == null)
            throw new IllegalArgumentException("DatabaseAccess.DatabaseAccess(Context context) - context cannot be null");

        this.databaseModelHelper = new DatabaseModelHelper(context);
    }

    public void initializeDatamodelUsingDatabaseData(Long USER_GUID)
    {
        if(USER_GUID == null)
            throw new IllegalArgumentException("DatabaseAccess.initializeDatamodelUsingDatabaseData(Long USER_GUID)- input cannot be null");

        try
        {
            //Read the database for user ID
            sqliteDatabase = databaseModelHelper.getReadableDatabase();
            final String[] projection = {DatabaseModel.USER.COLUMN_USER_ID};
            cursor = sqliteDatabase.query(
                    DatabaseModel.USER.TABLE_NAME, // The table to query
                    projection,                    // The array of columns to return (pass null to get all)
                    null,                     // The columns for the WHERE clause
                    null,                 // The values for the WHERE clause
                    null,                  // don't group the rows
                    null,                    // don't filter by row groups
                    null                      // The sort order
            );
            while (cursor.moveToNext()) {
                USER_GUID = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseModel.USER.COLUMN_USER_ID));

                if(AppSettings.DEBUG_MODE)
                    Log.d(TAG, "initializeDatamodelUsingDatabaseData(Long USER_GUID) | cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseModel.USER.COLUMN_USER_ID)) | " +USER_GUID);
            }
            //Close off open items
            closeDownDatabaseConnections();

        } finally {
            closeDownDatabaseConnections();
        }
    }

    /**
     * 1) Generate Map of COLUMN_PARENT_ID, COLUMN_PARENT_NAME
     * 2) Generate Map of COLUMN_CHILD_ID, COLUMN_CHILD_NAME
     * 3) Generate Map of COLUMN_TEMPLATE_NAME, COLUMN_TEMPLATE_SETTING
     */
    public DataModel populateDataModelUsingDatabaseData(Long USER_GUID)
    {
        if(USER_GUID == null)
            throw new IllegalArgumentException("DatabaseAccess.populateDataModelUsingDatabaseData(Long USER_GUID)- input cannot be null");

        final DataModel dataModel = new DataModel();

        try
        {
            //Read the database for user ID
            sqliteDatabase = databaseModelHelper.getReadableDatabase();

            //******************************************************
            //1) Generate Map of COLUMN_PARENT_ID, COLUMN_PARENT_NAME
            //******************************************************
            final String[] projectionParent = {DatabaseModel.PARENT.COLUMN_PARENT_ID, DatabaseModel.PARENT.COLUMN_PARENT_NAME};

            final String selectionParent = DatabaseModel.PARENT.COLUMN_USER_ID + " = ?";
            final String[] selectionArgsParent = {USER_GUID.toString()};

            cursor = sqliteDatabase.query(
                    DatabaseModel.PARENT.TABLE_NAME,        // The table to query
                    projectionParent,                            // The array of columns to return (pass null to get all)
                    selectionParent,                          // The columns for the WHERE clause
                    selectionArgsParent,                       // The values for the WHERE clause
                    null,                          // don't group the rows
                    null,                           // don't filter by row groups
                    DatabaseModel.PARENT.COLUMN_PARENT_ID   // The sort order
            );
            while (cursor.moveToNext()) {
                Long COLUMN_PARENT_ID = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseModel.PARENT.COLUMN_PARENT_ID));
                String COLUMN_PARENT_NAME = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.PARENT.COLUMN_PARENT_NAME));

                dataModel.getMapParentName_ParentID().put(COLUMN_PARENT_NAME, COLUMN_PARENT_ID);

                if(AppSettings.DEBUG_MODE) {
                    Log.d(TAG, "populateDataModelUsingDatabaseData(Long USER_GUID) | cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseModel.PARENT.COLUMN_PARENT_ID)) | " + COLUMN_PARENT_ID);
                    Log.d(TAG, "populateDataModelUsingDatabaseData(Long USER_GUID) | cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseModel.PARENT.COLUMN_PARENT_NAME)) | " + COLUMN_PARENT_NAME);
                }
            }

            if (cursor != null)
            {
                if (!cursor.isClosed())
                {
                    cursor.close();
                }
            }

            //******************************************************
            //2) Generate Map of COLUMN_PARENT_ID, COLUMN_CHILD_ID, COLUMN_CHILD_NAME
            //******************************************************
            final String[] projectionJOB = {DatabaseModel.CHILD.COLUMN_PARENT_ID, DatabaseModel.CHILD.COLUMN_CHILD_ID, DatabaseModel.CHILD.COLUMN_CHILD_NAME};

            cursor = sqliteDatabase.query(
                    DatabaseModel.CHILD.TABLE_NAME,      // The table to query
                    projectionJOB,                       // The array of columns to return (pass null to get all)
                    null,                        // The columns for the WHERE clause
                    null,                    // The values for the WHERE clause
                    null,                        // don't group the rows
                    null,                         // don't filter by row groups
                    DatabaseModel.CHILD.COLUMN_CHILD_ID      // The sort order
            );
            while (cursor.moveToNext()) {
                Long COLUMN_PARENT_ID = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseModel.CHILD.COLUMN_PARENT_ID));

                if (dataModel.getMapParentName_ParentID().values().contains(COLUMN_PARENT_ID)) {
                    Long COLUMN_CHILD_ID = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseModel.CHILD.COLUMN_CHILD_ID));
                    String COLUMN_CHILD_NAME = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.CHILD.COLUMN_CHILD_NAME));

                    Map<String, Long> mapChildNameChildID = (dataModel.getMapParentID_mapChildNameChildID()).get(COLUMN_PARENT_ID);

                    if (mapChildNameChildID != null) {
                        mapChildNameChildID.put(COLUMN_CHILD_NAME, COLUMN_CHILD_ID);
                    } else {
                        mapChildNameChildID = new Hashtable<String, Long>();
                        mapChildNameChildID.put(COLUMN_CHILD_NAME, COLUMN_CHILD_ID);
                    }

                    dataModel.getMapParentID_mapChildNameChildID().put(COLUMN_PARENT_ID, mapChildNameChildID);

                    if(AppSettings.DEBUG_MODE) {
                        Log.d(TAG, "populateDataModelUsingDatabaseData(Long USER_GUID) | cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseModel.CHILD.COLUMN_PARENT_ID)) | " + COLUMN_PARENT_ID);
                        Log.d(TAG, "populateDataModelUsingDatabaseData(Long USER_GUID) | cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseModel.CHILD.COLUMN_CHILD_ID)) | " + COLUMN_CHILD_ID);
                        Log.d(TAG, "populateDataModelUsingDatabaseData(Long USER_GUID) | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.CHILD.COLUMN_CHILD_NAME)) | " + COLUMN_CHILD_NAME);
                    }
                }
            }

            if (cursor != null)
            {
                if (!cursor.isClosed())
                {
                    cursor.close();
                }
            }

            //******************************************************
            //3) Generate Map of COLUMN_TEMPLATE_NAME, COLUMN_TEMPLATE_SETTING
            //******************************************************

            final String[] projectionTEMPLATE = {DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_NAME, DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_SETTING};
            final String selectionTEMPLATE = DatabaseModel.TEMPLATE.COLUMN_USER_ID + " = ?";
            final String[] selectionArgsTEMPLATE = {USER_GUID.toString()};

            cursor = sqliteDatabase.query(
                    DatabaseModel.TEMPLATE.TABLE_NAME,        // The table to query
                    projectionTEMPLATE,                            // The array of columns to return (pass null to get all)
                    selectionTEMPLATE,                          // The columns for the WHERE clause
                    selectionArgsTEMPLATE,                       // The values for the WHERE clause
                    null,                          // don't group the rows
                    null,                           // don't filter by row groups
                    DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_ID   // The sort order
            );

            while (cursor.moveToNext())
            {
                String COLUMN_TEMPLATE_NAME = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_NAME));
                Long COLUMN_TEMPLATE_SETTING = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_SETTING));

                dataModel.getMapTempName_TempSetting().put(COLUMN_TEMPLATE_NAME, COLUMN_TEMPLATE_SETTING);

                if(AppSettings.DEBUG_MODE) {
                    Log.d(TAG, "populateDataModelUsingDatabaseData(Long USER_GUID) | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_NAME)) | " + COLUMN_TEMPLATE_NAME);
                    Log.d(TAG, "populateDataModelUsingDatabaseData(Long USER_GUID) | cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_SETTING)) | " + COLUMN_TEMPLATE_SETTING);
                }
            }
        }
        finally
        {
            closeDownDatabaseConnections();
        }

        return dataModel;
    }

    protected void closeDownDatabaseConnections()
    {
        if (cursor != null)
        {
            if (!cursor.isClosed())
            {
                cursor.close();
            }
        }

        if (sqliteDatabase != null)
        {
            if (sqliteDatabase.isOpen())
            {
                sqliteDatabase.close();
            }
        }
    }
}
