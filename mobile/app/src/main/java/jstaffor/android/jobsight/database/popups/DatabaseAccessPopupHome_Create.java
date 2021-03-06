package jstaffor.android.jobsight.database.popups;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.database.DatabaseModel;
import jstaffor.android.jobsight.datamodel.DataModel;

public class DatabaseAccessPopupHome_Create extends DatabaseAccessPopup
{
    private static final String TAG = "DBAccessPopupHome_Creat";
    public DatabaseAccessPopupHome_Create(Context context)
    {
        super(context);
    }

    /**
     *
     * @param sParent
     * @param USER_GUID
     * @return newRowParentId or -1 if error occurred
     */
    public Long createParentInDatabase(String sParent, Long USER_GUID)
    {
        if(sParent == null || USER_GUID == null)
            throw new IllegalArgumentException("DatabaseAccessPopup.createParentInDatabase(String sParent, Long USER_GUID) - inputs cannot be null");

        Long newRowParentId = -1L; //sqliteDatabase.insert @return the row ID of the newly inserted row, or -1 if an error occurred

        try
        {
            // Open connection to database and insert the new row, returning the primary key value of the new row
            sqliteDatabase = databaseModelHelper.getWritableDatabase();

            values = new ContentValues();
            values.put(DatabaseModel.PARENT.COLUMN_PARENT_NAME, sParent);
            values.put(DatabaseModel.PARENT.COLUMN_USER_ID, USER_GUID);

            if (AppSettings.DATABASE_DEBUG_MODE) {
                Log.d(TAG, "createParentInDatabase(String sParent, Long USER_GUID) | DatabaseModel.PARENT.COLUMN_PARENT_NAME, sParent | " + sParent);
                Log.d(TAG, "createParentInDatabase(String sParent, Long USER_GUID) | DatabaseModel.PARENT.COLUMN_USER_ID, USER_GUID | " + USER_GUID);
            }

            newRowParentId = sqliteDatabase.insert(DatabaseModel.PARENT.TABLE_NAME, null, values);

        }
        catch(Exception exception)
        {
            newRowParentId = -1L;
        }
        finally
        {
            if (AppSettings.DATABASE_DEBUG_MODE)
            {
                Log.d(TAG, "createParentInDatabase(String sParent, Long USER_GUID) | finally | newRowParentId = " +newRowParentId);
                listDatabaseValues();
            }

            closeDownDatabaseConnections();
            return newRowParentId;
        }
    }

    /**
     *
     * @param lParent
     * @param sChild
     * @param lTemplateSetting
     * @return newRowChildId or -1 if error occurred
     */
    public Long saveParentAndChildAndTemplateSettingToDatabase(Long lParent, String sChild, Long lTemplateSetting)
    {
        if(lParent == null || sChild == null || lTemplateSetting == null)
            throw new IllegalArgumentException("DatabaseAccessPopup.saveParentAndChildAndTemplateSettingToDatabase(Long lParent, String sChild, Long lTemplateSetting) - inputs cannot be null");

        Long newRowChildId = -1L; //sqliteDatabase.insert @return the row ID of the newly inserted row, or -1 if an error occurred

        try
        {
            // Open connection to database and insert the new row, returning the primary key value of the new row
            sqliteDatabase = databaseModelHelper.getWritableDatabase();

            values = new ContentValues();
            values.put(DatabaseModel.CHILD.COLUMN_PARENT_ID, lParent);
            values.put(DatabaseModel.CHILD.COLUMN_CHILD_NAME, sChild);
            values.put(DatabaseModel.CHILD.COLUMN_TEMPLATE_SETTING, lTemplateSetting);


            if (AppSettings.DATABASE_DEBUG_MODE) {
                Log.d(TAG, "saveParentAndChildAndTemplateSettingToDatabase(Long lParent, String sChild, Long lTemplateSetting) | DatabaseModel.CHILD.COLUMN_PARENT_ID, lParent | " + lParent);
                Log.d(TAG, "saveParentAndChildAndTemplateSettingToDatabase(Long lParent, String sChild, Long lTemplateSetting) | DatabaseModel.CHILD.COLUMN_CHILD_NAME, sChild | " + sChild);
                Log.d(TAG, "saveParentAndChildAndTemplateSettingToDatabase(Long lParent, String sChild, Long lTemplateSetting) | DatabaseModel.CHILD.COLUMN_TEMPLATE_SETTING, lTemplateSetting | " + lTemplateSetting);
            }

            //Update database and retrieve new child id
            newRowChildId = sqliteDatabase.insert(DatabaseModel.CHILD.TABLE_NAME, null, values);

        }
        catch(Exception exception)
        {
            newRowChildId = -1L;
        }
        finally
        {
            if (AppSettings.DATABASE_DEBUG_MODE)
            {
                Log.d(TAG, "saveParentAndChildAndTemplateSettingToDatabase(Long lParent, String sChild, Long lTemplateSetting) | finally | newRowChildId = " +newRowChildId);
                listDatabaseValues();
            }

            closeDownDatabaseConnections();
            return newRowChildId;
        }
    }
}