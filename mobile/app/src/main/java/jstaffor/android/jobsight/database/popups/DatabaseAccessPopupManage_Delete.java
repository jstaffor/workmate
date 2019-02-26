package jstaffor.android.jobsight.database.popups;

import android.content.ContentValues;
import android.content.Context;

import jstaffor.android.jobsight.database.DatabaseModel;

public class DatabaseAccessPopupManage_Delete extends DatabaseAccessPopup
{
    private static final String TAG = "DatabaseAccessPopupManage_Delete";
    public DatabaseAccessPopupManage_Delete(Context context)
    {
        super(context);
    }

    public Long getTemplateIDForTemplateNameAndUserID(String sTemplate, Long UserID)
    {
        return super.getTemplateIDForTemplateNameAndUserID(sTemplate, UserID);
    }

    public int deleteChildFromDatabase(Long lParent, Long lChild)
    {
        if(lParent == null || lChild == null)
            throw new IllegalArgumentException("DatabaseAccessPopupManage_Delete.deleteTemplateFromDatabase(Long lParent, Long lChild) - inputs cannot be null");

        int newRowChildId = 0; //sqliteDatabase.delete() - the number of rows affected if a whereClause is passed in, 0 otherwise.

        try
        {
            //Read the database for user ID
            sqliteDatabase = databaseModelHelper.getWritableDatabase();

            String selection = DatabaseModel.CHILD.COLUMN_PARENT_ID + " = ? AND " + DatabaseModel.CHILD.COLUMN_CHILD_ID + " = ?";
            String[] selectionArgs = {lParent.toString(), lChild.toString()};

            newRowChildId =  sqliteDatabase.delete(DatabaseModel.CHILD.TABLE_NAME, selection, selectionArgs);
        }
        catch(Exception exception)
        {
            newRowChildId = 0; //sqliteDatabase.delete() - the number of rows affected if a whereClause is passed in, 0 otherwise.
        }
        finally
        {
            closeDownDatabaseConnections();
            return newRowChildId;
        }
    }

    public int deleteParentFromDatabase(Long lParent, Long USER_GUID)
    {
        if(lParent == null || USER_GUID == null)
            throw new IllegalArgumentException("DatabaseAccessPopupManage_Delete.deleteParentFromDatabase(Long lParent, Long USER_GUID) - inputs cannot be null");

        int newRowParentId = 0; //sqliteDatabase.delete() - the number of rows affected if a whereClause is passed in, 0 otherwise.

        try
        {
            //Read the database for user ID
            sqliteDatabase = databaseModelHelper.getWritableDatabase();

            String selection = DatabaseModel.PARENT.COLUMN_PARENT_ID + " = ? AND " + DatabaseModel.PARENT.COLUMN_USER_ID + " = ?";
            String[] selectionArgs = {lParent.toString(), USER_GUID.toString()};

            newRowParentId =  sqliteDatabase.delete(DatabaseModel.PARENT.TABLE_NAME, selection, selectionArgs);
        }
        catch(Exception exception)
        {
            newRowParentId = 0; //sqliteDatabase.delete() - the number of rows affected if a whereClause is passed in, 0 otherwise.
        }
        finally
        {
            closeDownDatabaseConnections();
            return newRowParentId;
        }
    }

    public int deleteTemplateFromDatabase(Long lTemplate, Long USER_GUID)
    {
        if(lTemplate == null || USER_GUID == null)
            throw new IllegalArgumentException("DatabaseAccessPopupManage_Delete.deleteTemplateFromDatabase(Long lTemplate, Long USER_GUID) - inputs cannot be null");

        int newRowTemplateId = 0; //sqliteDatabase.delete() - the number of rows affected if a whereClause is passed in, 0 otherwise.

        try
        {
            //Read the database for user ID
            sqliteDatabase = databaseModelHelper.getWritableDatabase();

            String selection = DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_ID + " = ? AND " + DatabaseModel.TEMPLATE.COLUMN_USER_ID + " = ?";
            String[] selectionArgs = {lTemplate.toString(), USER_GUID.toString()};

            newRowTemplateId =  sqliteDatabase.delete(DatabaseModel.TEMPLATE.TABLE_NAME, selection, selectionArgs);
        }
        catch(Exception exception)
        {
            newRowTemplateId = 0; //sqliteDatabase.delete() - the number of rows affected if a whereClause is passed in, 0 otherwise.
        }
        finally
        {
            closeDownDatabaseConnections();
            return newRowTemplateId;
        }
    }
}