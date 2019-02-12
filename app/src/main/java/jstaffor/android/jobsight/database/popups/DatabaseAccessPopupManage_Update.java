package jstaffor.android.jobsight.database.popups;

import android.content.ContentValues;
import android.content.Context;

import jstaffor.android.jobsight.database.DatabaseModel;
import jstaffor.android.jobsight.datamodel.DataModel;

public class DatabaseAccessPopupManage_Update extends DatabaseAccessPopup
{
    private static final String TAG = "DatabaseAccessPopupManage_Update";
    public DatabaseAccessPopupManage_Update(Context context)
    {
        super(context);
    }

    public boolean updateParentInDatabase(Long lParent, String sParent, Long USER_GUID)
    {
        if(lParent == null || sParent == null || USER_GUID == null)
            throw new IllegalArgumentException("DatabaseAccessPopupManage_Update.updateParentInDatabase(Long lParent, String sParent, Long USER_GUID) - inputs cannot be null");

        Boolean parentUpdatedSuccessfully = false;

        try
        {
            // Open connection to database and insert the new row, returning the primary key value of the new row
            sqliteDatabase = databaseModelHelper.getWritableDatabase();

            values = new ContentValues();
            values.put(DatabaseModel.PARENT.COLUMN_PARENT_NAME, sParent);

            // Filter results WHERE "title" = 'My Title'
            String selection = DatabaseModel.PARENT.COLUMN_PARENT_ID + " = ? AND " + DatabaseModel.PARENT.COLUMN_USER_ID + " = ?";
            String[] selectionArgs = {lParent.toString(), USER_GUID.toString()};

            if(sqliteDatabase.update(DatabaseModel.PARENT.TABLE_NAME, values, selection, selectionArgs) == 1)
            {
                parentUpdatedSuccessfully = true;
            }
            else
            {
                parentUpdatedSuccessfully = false;
            }
        }
        catch(Exception exception)
        {
            parentUpdatedSuccessfully = false;
        }
        finally
        {
            closeDownDatabaseConnections();
            return parentUpdatedSuccessfully;
        }
    }

    public Boolean updateChildInDatabase(Long lParent, Long lChild, String sChild)
    {
        if(lChild == null || sChild == null || lParent == null)
            throw new IllegalArgumentException("DatabaseAccessPopupManage_Update.updateChildInDatabase(Long lParent, Long lChild, String sChild) - inputs cannot be null");

        Boolean childUpdatedSuccessfully = false;

        try
        {
            // Open connection to database and insert the new row, returning the primary key value of the new row
            sqliteDatabase = databaseModelHelper.getWritableDatabase();

            values = new ContentValues();
            values.put(DatabaseModel.CHILD.COLUMN_CHILD_NAME, sChild);

            // Filter results WHERE "title" = 'My Title'
            String selection = DatabaseModel.CHILD.COLUMN_PARENT_ID + " = ? AND " + DatabaseModel.CHILD.COLUMN_CHILD_ID + " = ?";
            String[] selectionArgs = {lParent.toString(), lChild.toString()};

            if(sqliteDatabase.update(DatabaseModel.CHILD.TABLE_NAME, values, selection, selectionArgs) == 1)
            {
                childUpdatedSuccessfully = true;
            }
            else
            {
                childUpdatedSuccessfully = false;
            }
        }
        catch(Exception exception)
        {
            childUpdatedSuccessfully = false;
        }
        finally
        {
            closeDownDatabaseConnections();
            return childUpdatedSuccessfully;
        }
    }

    public Boolean updateTemplateInDatabase(String sSelectedTemplate, Long lNewTemplateSetting, String sNewTemplate, Long USER_GUID)
    {
        if(sSelectedTemplate == null || lNewTemplateSetting == null || sNewTemplate == null || USER_GUID == null)
            throw new IllegalArgumentException("DatabaseAccessPopupManage_Update.updateTemplateInDatabase(String sSelectedTemplate, Long lNewTemplateSetting, String sNewTemplate, Long USER_GUID) - inputs cannot be null");

        //Get the ID of the selected template
        Long lSelectedTemplateID = getTemplateIDForTemplateNameAndUserID(sSelectedTemplate, USER_GUID);
        if(lSelectedTemplateID == -1L)
            return false;

        Boolean templateUpdatedSuccessfully = false;

        try
        {
            // Open connection to database and insert the new row, returning the primary key value of the new row
            sqliteDatabase = databaseModelHelper.getWritableDatabase();

            values = new ContentValues();
            values.put(DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_NAME, sNewTemplate);
            values.put(DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_SETTING, lNewTemplateSetting);

            String selection = DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_ID + " = ? AND " + DatabaseModel.TEMPLATE.COLUMN_USER_ID + " = ?";
            String[] selectionArgs = {lSelectedTemplateID.toString(), USER_GUID.toString()};

            if(sqliteDatabase.update(DatabaseModel.TEMPLATE.TABLE_NAME, values, selection, selectionArgs) == 1)
            {
                templateUpdatedSuccessfully = true;
            }
            else
            {
                templateUpdatedSuccessfully = false;
            }
        }
        catch(Exception exception)
        {
            templateUpdatedSuccessfully = false;
        }
        finally
        {
            closeDownDatabaseConnections();
            return templateUpdatedSuccessfully;
        }
    }
}