package jstaffor.android.jobsight.database.popups;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.database.DatabaseModel;
import jstaffor.android.jobsight.datamodel.DataModel;

public class DatabaseAccessPopupManage_Create extends DatabaseAccessPopup
{
    private static final String TAG = "DBAccessPopupManage_Cre";
    public DatabaseAccessPopupManage_Create(Context context)
    {
        super(context);
    }

    /**
     * Save job to database
     *
     * @param lParent
     * @param sParent
     * @param sChild
     * @param lTemplateSetting
     * @return //@return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public Long saveChildToDatabase(Long lParent, String sParent, String sChild, Long lTemplateSetting)
    {
        if(sParent == null || sChild == null || lTemplateSetting == null)
            throw new IllegalArgumentException("DatabaseAccessPopup.saveParentAndChildAndTemplateSettingToDatabase(String sParent, String sChild, Long lTemplateSetting) - inputs cannot be null");

        Long newRowChildId = -1L; //sqliteDatabase.insert @return the row ID of the newly inserted row, or -1 if an error occurred

        try
        {
            // Open connection to database and insert the new row, returning the primary key value of the new row
            sqliteDatabase = databaseModelHelper.getWritableDatabase();

            values = new ContentValues();
            values.put(DatabaseModel.CHILD.COLUMN_CHILD_NAME, sChild);
            values.put(DatabaseModel.CHILD.COLUMN_PARENT_ID, lParent);
            values.put(DatabaseModel.CHILD.COLUMN_TEMPLATE_SETTING, lTemplateSetting);

            if (AppSettings.DATABASE_DEBUG_MODE) {
                Log.d(TAG, "saveChildToDatabase(Long lParent, String sParent, String sChild, Long lTemplateSetting) | DatabaseModel.CHILD.COLUMN_CHILD_NAME, sChild | " + sChild);
                Log.d(TAG, "saveChildToDatabase(Long lParent, String sParent, String sChild, Long lTemplateSetting) | DatabaseModel.CHILD.COLUMN_PARENT_ID, lParent | " + lParent);
                Log.d(TAG, "saveChildToDatabase(Long lParent, String sParent, String sChild, Long lTemplateSetting) | DatabaseModel.CHILD.COLUMN_TEMPLATE_SETTING, lTemplateSetting | " + lTemplateSetting);
            }

            //Update database and retrieve new child id
            newRowChildId = sqliteDatabase.insert(DatabaseModel.CHILD.TABLE_NAME, null, values);

        }
        catch(Exception exception)
        {
            newRowChildId = -1L; //sqliteDatabase.insert @return the row ID of the newly inserted row, or -1 if an error occurred
        }
        finally
        {
            if (AppSettings.DATABASE_DEBUG_MODE)
            {
                Log.d(TAG, "saveChildToDatabase(Long lParent, String sParent, String sChild, Long lTemplateSetting) | finally | newRowChildId = " +newRowChildId);
                listDatabaseValues();
            }

            closeDownDatabaseConnections();
            return newRowChildId;
        }
    }

    /**
     * Save group to database
     *
     * @param sParent
     * @return //@return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public Long saveParentToDatabase(String sParent, Long USER_GUID)
    {

        if(sParent == null || USER_GUID == null)
            throw new IllegalArgumentException("DatabaseAccessPopupManage_Create.updateParentInDatabase(String sParent, Long USER_GUID) - input cannot be null");

        Long newRowParentId = -1L; //sqliteDatabase.insert @return the row ID of the newly inserted row, or -1 if an error occurred

        try
        {
            // Open connection to database and insert the new row, returning the primary key value of the new row
            sqliteDatabase = databaseModelHelper.getWritableDatabase();

            values = new ContentValues();
            values.put(DatabaseModel.PARENT.COLUMN_PARENT_NAME, sParent);
            values.put(DatabaseModel.PARENT.COLUMN_USER_ID, USER_GUID);

            if (AppSettings.DATABASE_DEBUG_MODE) {
                Log.d(TAG, "saveParentToDatabase(String sParent, Long USER_GUID) | DatabaseModel.PARENT.COLUMN_PARENT_NAME, sParent | " + sParent);
                Log.d(TAG, "saveParentToDatabase(String sParent, Long USER_GUID) | DatabaseModel.PARENT.COLUMN_USER_ID, USER_GUID | " + USER_GUID);
            }

            newRowParentId = sqliteDatabase.insert(DatabaseModel.PARENT.TABLE_NAME, null, values);
        }
        catch(Exception exception)
        {
            newRowParentId = -1L; //sqliteDatabase.insert @return the row ID of the newly inserted row, or -1 if an error occurred
        }
        finally
        {
            if (AppSettings.DATABASE_DEBUG_MODE)
            {
                Log.d(TAG, "saveParentToDatabase(String sParent, Long USER_GUID) | finally | newRowParentId = " +newRowParentId);
                listDatabaseValues();
            }

            closeDownDatabaseConnections();
            return newRowParentId;
        }
    }

    /**
     * Save template to database
     *
     * @param sTemplate
     * @param lTemplateSetting
     * @return //@return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public Long saveTemplateToDatabase(String sTemplate, Long lTemplateSetting, Long USER_GUID)
    {
        if(sTemplate == null || lTemplateSetting == null || USER_GUID == null)
            throw new IllegalArgumentException("DatabaseAccessPopupManage_Create.updateTemplateInDatabase(String sTemplate, Long lTemplateSetting, Long USER_GUID) - inputs cannot be null");

        Long newRowTemplateId = -1L; //sqliteDatabase.insert @return the row ID of the newly inserted row, or -1 if an error occurred

        try
        {
            // Open connection to database and insert the new row, returning the primary key value of the new row
            sqliteDatabase = databaseModelHelper.getWritableDatabase();

            values = new ContentValues();
            values.put(DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_NAME, sTemplate);
            values.put(DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_SETTING, lTemplateSetting);
            values.put(DatabaseModel.TEMPLATE.COLUMN_USER_ID, USER_GUID);

            if (AppSettings.DATABASE_DEBUG_MODE) {
                Log.d(TAG, "saveTemplateToDatabase(String sTemplate, Long lTemplateSetting, Long USER_GUID) | DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_NAME, sTemplate | " + sTemplate);
                Log.d(TAG, "saveTemplateToDatabase(String sTemplate, Long lTemplateSetting, Long USER_GUID) | DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_SETTING, lTemplateSetting | " + lTemplateSetting);
                Log.d(TAG, "saveTemplateToDatabase(String sTemplate, Long lTemplateSetting, Long USER_GUID) | DatabaseModel.TEMPLATE.COLUMN_USER_ID, USER_GUID | " + USER_GUID);
            }

            newRowTemplateId = sqliteDatabase.insert(DatabaseModel.TEMPLATE.TABLE_NAME, null, values);
        }
        catch(Exception exception)
        {
            newRowTemplateId = -1L; //sqliteDatabase.insert @return the row ID of the newly inserted row, or -1 if an error occurred
        }
        finally {
            if (AppSettings.DATABASE_DEBUG_MODE)
            {
                Log.d(TAG, "saveTemplateToDatabase(String sTemplate, Long lTemplateSetting, Long USER_GUID)  | finally | newRowTemplateId = " +newRowTemplateId);
                listDatabaseValues();
            }

            closeDownDatabaseConnections();
            return newRowTemplateId;
        }
    }
}