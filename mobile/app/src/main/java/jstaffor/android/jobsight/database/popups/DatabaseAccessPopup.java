package jstaffor.android.jobsight.database.popups;

import android.content.Context;
import android.util.Log;

import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.database.DatabaseModel;
import jstaffor.android.jobsight.database.DatabaseAccess;
import jstaffor.android.jobsight.datamodel.DataModel;

public class DatabaseAccessPopup extends DatabaseAccess
{
    private static final String TAG = "DatabaseAccessPopup";

    public DatabaseAccessPopup(Context context) {
        super(context);
    }

    protected Long getTemplateSettingForChild(Long lChild) {
        if (lChild == null)
            throw new IllegalArgumentException("DatabaseAccessPopup.getTemplateSettingForChild(Long lChild) - lChild cannot be null");

        //Set default setting in case something goes wrong
        Long localTemplateSettingForChild = DataModel.DEFAULT_VALUE_COLUMN_TEMPLATE_SETTING;

        try {
            //Read the database for user ID
            sqliteDatabase = databaseModelHelper.getReadableDatabase();
            final String[] projection = {DatabaseModel.CHILD.COLUMN_TEMPLATE_SETTING};

            // Filter results WHERE "title" = 'My Title'
            String selection = DatabaseModel.CHILD.COLUMN_CHILD_ID + " = ?";
            String[] selectionArgs = {lChild.toString()};

            if (AppSettings.DATABASE_DEBUG_MODE)
                Log.d(TAG, "getTemplateSettingForChild(Long lChild) | DatabaseModel.CHILD.COLUMN_CHILD_ID = ? | " +lChild);

            cursor = sqliteDatabase.query(
                    DatabaseModel.CHILD.TABLE_NAME, // The table to query
                    projection,                    // The array of columns to return (pass null to get all)
                    selection,                     // The columns for the WHERE clause
                    selectionArgs,                 // The values for the WHERE clause
                    null,                  // don't group the rows
                    null,                    // don't filter by row groups
                    null                      // The sort order
            );
            while (cursor.moveToNext()) {
                localTemplateSettingForChild = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseModel.CHILD.COLUMN_TEMPLATE_SETTING));
            }
        } catch (Exception exception)
        {
            localTemplateSettingForChild = DataModel.DEFAULT_VALUE_COLUMN_TEMPLATE_SETTING;
        } finally
        {
            if (AppSettings.DATABASE_DEBUG_MODE) {
                Log.d(TAG, "getTemplateSettingForChild(Long lChild) | finally | localTemplateSettingForChild = " + localTemplateSettingForChild);
                listDatabaseValues();
            }

            closeDownDatabaseConnections();
            return localTemplateSettingForChild;
        }
    }

    /**
     *
     * @param sTemplate
     * @param UserID
     * @return the ID, or -1 if an error occurred
     */
    protected Long getTemplateIDForTemplateNameAndUserID(String sTemplate, Long UserID)
    {
        if (sTemplate == null)
            throw new IllegalArgumentException("DatabaseAccessPopup.getTemplateIDForTemplateNameAndUserID(String sTemplate) - sTemplate cannot be null");

        // -1 == @return the row ID of the newly inserted row, or -1 if an error occurred
        Long lTemplateID = -1L;

        try
        {
            //Read the database for user ID
            sqliteDatabase = databaseModelHelper.getReadableDatabase();
            final String[] projection = {DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_ID};

            String selection = DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_NAME + " = ? AND " + DatabaseModel.TEMPLATE.COLUMN_USER_ID + " = ?";
            String[] selectionArgs = {sTemplate, UserID.toString()};

            if (AppSettings.DATABASE_DEBUG_MODE) {
                Log.d(TAG, " Long getTemplateIDForTemplateNameAndUserID(String sTemplate, Long UserID) | DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_NAME = ? | " + sTemplate);
                Log.d(TAG, " Long getTemplateIDForTemplateNameAndUserID(String sTemplate, Long UserID) | DatabaseModel.TEMPLATE.COLUMN_USER_ID = ? | " + UserID);
            }

            cursor = sqliteDatabase.query(
                    DatabaseModel.TEMPLATE.TABLE_NAME, // The table to query
                    projection,                    // The array of columns to return (pass null to get all)
                    selection,                     // The columns for the WHERE clause
                    selectionArgs,                 // The values for the WHERE clause
                    null,                  // don't group the rows
                    null,                    // don't filter by row groups
                    null                      // The sort order
            );
            while (cursor.moveToNext()) {
                lTemplateID = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_ID));
            }
        } catch (Exception exception) {
            lTemplateID = -1L;
        } finally
        {
            if (AppSettings.DATABASE_DEBUG_MODE)
            {
                Log.d(TAG, "getTemplateIDForTemplateNameAndUserID(String sTemplate, Long UserID) | finally | lTemplateID = " +lTemplateID);
                listDatabaseValues();
            }

            closeDownDatabaseConnections();
            return lTemplateID;
        }
    }
}